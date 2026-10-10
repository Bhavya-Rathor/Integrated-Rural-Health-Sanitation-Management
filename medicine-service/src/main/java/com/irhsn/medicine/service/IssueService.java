package com.irhsn.medicine.service;

import com.irhsn.medicine.entity.IssueRecord;
import com.irhsn.medicine.entity.Stock;
import com.irhsn.medicine.exception.InsufficientStockException;
import com.irhsn.medicine.repository.IssueRecordRepository;
import com.irhsn.medicine.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Gives medicine to patients, oldest-expiry batch first (FEFO).
@Service
public class IssueService {

    private final StockRepository stockRepository;
    private final IssueRecordRepository issueRecordRepository;
    private final MedicineService medicineService;
    private final ReplenishmentService replenishmentService; // for the auto-restock check

    public IssueService(StockRepository stockRepository,
                        IssueRecordRepository issueRecordRepository,
                        MedicineService medicineService,
                        ReplenishmentService replenishmentService) {
        this.stockRepository = stockRepository;
        this.issueRecordRepository = issueRecordRepository;
        this.medicineService = medicineService;
        this.replenishmentService = replenishmentService;
    }

    // Give `quantity` units of a medicine to a patient at a centre.
    // @Transactional = ALL OR NOTHING: if anything fails part-way,
    // every database change in this method is undone.
    @Transactional
    public List<IssueRecord> issue(Long medicineId, Long centreId, int quantity,
                                   Long residentId, Long appointmentId, String issuedBy) {

        medicineService.getById(medicineId); // 404 if the medicine doesn't exist

        // STEP 1: is there enough in total?
        int available = stockRepository.totalQuantity(medicineId, centreId);
        if (available < quantity) {
            throw new InsufficientStockException("Only " + available
                    + " units available, " + quantity + " requested");
        }

        // STEP 2: batches with stock left, soonest expiry first (FEFO).
        List<Stock> batches = stockRepository
                .findByMedicineIdAndCentreIdAndQuantityGreaterThanOrderByExpiryDateAsc(
                        medicineId, centreId, 0);

        // STEP 3: take from each batch in order until we have enough.
        List<IssueRecord> records = new ArrayList<>();
        int remaining = quantity; // how many we still need to give

        for (Stock batch : batches) {
            if (remaining == 0) {
                break; // got everything, stop
            }
            // Never give out expired medicine: skip it.
            if (batch.getExpiryDate().isBefore(LocalDate.now())) {
                continue;
            }

            // Take the smaller of: what we still need, what this batch has.
            int take = Math.min(remaining, batch.getQuantity());
            batch.setQuantity(batch.getQuantity() - take);
            stockRepository.save(batch);

            // STEP 4: write a logbook line for this batch.
            IssueRecord record = new IssueRecord();
            record.setStock(batch);
            record.setQuantity(take);
            record.setResidentId(residentId);
            record.setAppointmentId(appointmentId);
            record.setIssuedBy(issuedBy);
            records.add(issueRecordRepository.save(record));

            remaining -= take;
        }

        // Only expired batches were left, so we couldn't give it all.
        // Throwing here undoes everything above (thanks to @Transactional).
        if (remaining > 0) {
            throw new InsufficientStockException(
                    "Not enough unexpired stock: short by " + remaining);
        }

        // STEP 5: stock went down, so check if a restock request is needed.
        replenishmentService.checkAndRaise(medicineId, centreId);

        return records;
    }

    // A patient's medicine history, newest first (optional, for Tvisha's resident page).
    public List<IssueRecord> historyForResident(Long residentId) {
        return issueRecordRepository.findByResidentIdOrderByIssuedAtDesc(residentId);
    }
}