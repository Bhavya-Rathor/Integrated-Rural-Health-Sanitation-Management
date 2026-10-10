package com.irhsn.medicine.service;

import com.irhsn.medicine.client.CentreClient;
import com.irhsn.medicine.entity.Medicine;
import com.irhsn.medicine.entity.ReplenishmentRequest;
import com.irhsn.medicine.entity.Stock;
import com.irhsn.medicine.enums.Priority;
import com.irhsn.medicine.enums.RequestSource;
import com.irhsn.medicine.enums.RequestStatus;
import com.irhsn.medicine.exception.BadRequestException;
import com.irhsn.medicine.exception.NotFoundException;
import com.irhsn.medicine.repository.ReplenishmentRequestRepository;
import com.irhsn.medicine.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Manager of restock requests ("please send more medicine" slips).
@Service
public class ReplenishmentService {

    // ===== THE RULEBOOK FOR STATUS CHANGES =====
    // For each current status, which next statuses are allowed.
    // Anything not listed here is refused.
    private static final Map<RequestStatus, Set<RequestStatus>> ALLOWED = Map.of(
            RequestStatus.PENDING, Set.of(RequestStatus.APPROVED, RequestStatus.REJECTED),
            RequestStatus.APPROVED, Set.of(RequestStatus.DISPATCHED),
            RequestStatus.DISPATCHED, Set.of(RequestStatus.RECEIVED));

    private final ReplenishmentRequestRepository requestRepository;
    private final StockRepository stockRepository;
    private final MedicineService medicineService;
    private final CentreClient centreClient; // to get the village from Bhavya's service

    public ReplenishmentService(ReplenishmentRequestRepository requestRepository,
                                StockRepository stockRepository,
                                MedicineService medicineService,
                                CentreClient centreClient) {
        this.requestRepository = requestRepository;
        this.stockRepository = stockRepository;
        this.medicineService = medicineService;
        this.centreClient = centreClient;
    }

    // ===== 1. AUTOMATIC REQUEST =====
    // Called after every issue. If stock at this centre dropped below
    // the warning line, create a PENDING request automatically.
    public void checkAndRaise(Long medicineId, Long centreId) {
        Medicine medicine = medicineService.getById(medicineId);
        int total = stockRepository.totalQuantity(medicineId, centreId);

        // Still enough? Do nothing.
        if (total >= medicine.getReorderLevel()) {
            return;
        }
        // Already a PENDING request for this medicine at this centre? Don't duplicate.
        if (requestRepository.existsByMedicineIdAndCentreIdAndStatus(
                medicineId, centreId, RequestStatus.PENDING)) {
            return;
        }

        ReplenishmentRequest request = new ReplenishmentRequest();
        request.setMedicine(medicine);
        request.setCentreId(centreId);
        request.setVillage(centreClient.getCentre(centreId).getVillage()); // "Unknown" if Bhavya's service is down
        request.setCurrentQty(total);
        // Ask for enough to get back up to double the warning line.
        // e.g. reorder 100, have 30 -> ask for 200 - 30 = 170.
        request.setRequestedQty(Math.max(2 * medicine.getReorderLevel() - total, 1));
        request.setPriority(decidePriority(medicine, total));
        request.setSource(RequestSource.AUTO);
        request.setStatus(RequestStatus.PENDING);
        requestRepository.save(request);
    }

    // ===== 2. MANUAL REQUEST =====
    // A health worker fills the "report low stock" form.
    public ReplenishmentRequest createManual(Long medicineId, Long centreId,
                                             int requestedQty, String remarks) {
        Medicine medicine = medicineService.getById(medicineId);
        int total = stockRepository.totalQuantity(medicineId, centreId);

        ReplenishmentRequest request = new ReplenishmentRequest();
        request.setMedicine(medicine);
        request.setCentreId(centreId);
        request.setVillage(centreClient.getCentre(centreId).getVillage());
        request.setCurrentQty(total);
        request.setRequestedQty(requestedQty);
        request.setPriority(decidePriority(medicine, total));
        request.setSource(RequestSource.MANUAL);
        request.setStatus(RequestStatus.PENDING);
        request.setRemarks(remarks);
        return requestRepository.save(request);
    }

    // ===== 3. OFFICER CHANGES THE STATUS =====
    // Called when the officer clicks Approve/Reject/... on the dashboard.
    // @Transactional = all or nothing: if adding the new batch fails,
    // the status change is undone too.
    @Transactional
    public ReplenishmentRequest updateStatus(Long id, RequestStatus newStatus, String remarks) {
        ReplenishmentRequest request = getById(id);

        // Check the rulebook: is this move allowed?
        Set<RequestStatus> allowedNext = ALLOWED.getOrDefault(request.getStatus(), Set.of());
        if (!allowedNext.contains(newStatus)) {
            throw new BadRequestException("Cannot change status from "
                    + request.getStatus() + " to " + newStatus);
        }

        request.setStatus(newStatus);
        if (remarks != null && !remarks.isBlank()) {
            request.setRemarks(remarks);
        }

        // Medicine arrived -> put it on the shelf as a new batch.
        if (newStatus == RequestStatus.RECEIVED) {
            Stock batch = new Stock();
            batch.setMedicine(request.getMedicine());
            batch.setCentreId(request.getCentreId());
            batch.setBatchNo("REQ-" + request.getId());          // e.g. REQ-7
            batch.setQuantity(request.getRequestedQty());
            batch.setReceivedDate(LocalDate.now());
            batch.setExpiryDate(LocalDate.now().plusYears(1));   // default: 1 year
            stockRepository.save(batch);
        }
        return requestRepository.save(request);
    }

    // ===== HELPERS =====

    // Get one request, or 404.
    public ReplenishmentRequest getById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request " + id + " not found"));
    }

    // All requests, or only one status (for the requests page and /pending).
    public List<ReplenishmentRequest> findByStatus(RequestStatus status) {
        if (status == null) {
            return requestRepository.findAll();
        }
        return requestRepository.findByStatus(status);
    }

    // How many requests have this status (for /summary).
    public long countByStatus(RequestStatus status) {
        return requestRepository.countByStatus(status);
    }

    // ===== 4. PRIORITY RULE =====
    // HIGH   = completely out, OR an essential medicine
    // MEDIUM = below half the warning line
    // LOW    = below the warning line, but not badly
    private Priority decidePriority(Medicine medicine, int total) {
        if (total == 0 || medicine.isEssential()) {
            return Priority.HIGH;
        }
        if (total < medicine.getReorderLevel() / 2) {
            return Priority.MEDIUM;
        }
        return Priority.LOW;
    }
}