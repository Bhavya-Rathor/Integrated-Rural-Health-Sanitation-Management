package com.irhsn.medicine.service;

import com.irhsn.medicine.entity.Medicine;
import com.irhsn.medicine.entity.Stock;
import com.irhsn.medicine.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Manager of the medicine on the shelves (batches at centres).
@Service
public class StockService {

    private final StockRepository stockRepository;
    private final MedicineService medicineService; // to check the medicine exists

    // Spring passes both in automatically (constructor injection).
    public StockService(StockRepository stockRepository, MedicineService medicineService) {
        this.stockRepository = stockRepository;
        this.medicineService = medicineService;
    }

    // ===== 1. RECEIVE A NEW BATCH =====
    // A box of medicine arrives at a centre.
    // Load the real Medicine first (404 if the id is wrong),
    // attach it to the batch, fill today's date if missing, then save.
    public Stock receiveStock(Long medicineId, Stock batch) {
        Medicine medicine = medicineService.getById(medicineId);
        batch.setMedicine(medicine);
        if (batch.getReceivedDate() == null) {
            batch.setReceivedDate(LocalDate.now());
        }
        return stockRepository.save(batch);
    }

    // ===== 2. LIST BATCHES =====
    // No centre chosen -> every batch. Centre chosen -> only that centre's.
    public List<Stock> findByCentre(Long centreId) {
        if (centreId == null) {
            return stockRepository.findAll();
        }
        return stockRepository.findByCentreId(centreId);
    }

    // ===== 3. TOTAL AT ONE CENTRE =====
    // e.g. Paracetamol at centre 2: batch A 100 + batch B 50 = 150.
    public int totalQuantity(Long medicineId, Long centreId) {
        return stockRepository.totalQuantity(medicineId, centreId);
    }

    // ===== 4. EXPIRING SOON =====
    // Batches expiring within the next N days that still have units left.
    public List<Stock> expiringWithin(int days) {
        LocalDate limit = LocalDate.now().plusDays(days);
        return stockRepository.findByExpiryDateBefore(limit).stream()
                .filter(batch -> batch.getQuantity() > 0)   // keep only non-empty batches
                .toList();
    }

    // ===== 5. HOW MANY ARE RUNNING LOW? =====
    // Counts (medicine, centre) pairs whose total is below the reorder level.
    // Used for the "lowStock" number on Tvisha's dashboard.
    public long countLowStock() {
        // Key looks like "3-2" = medicine 3 at centre 2.
        Map<String, Integer> totals = new HashMap<>();        // key -> total units
        Map<String, Integer> reorderLevels = new HashMap<>(); // key -> warning line

        // Step A: add every batch's units to its pair's running total.
        for (Stock batch : stockRepository.findAll()) {
            String key = batch.getMedicine().getId() + "-" + batch.getCentreId();
            totals.merge(key, batch.getQuantity(), Integer::sum);
            reorderLevels.put(key, batch.getMedicine().getReorderLevel());
        }

        // Step B: count the pairs whose total is under their warning line.
        return totals.entrySet().stream()
                .filter(entry -> entry.getValue() < reorderLevels.get(entry.getKey()))
                .count();
    }
    // All batches of one medicine, soonest expiry first (detail page).
    public List<Stock> findByMedicine(Long medicineId) {
        return stockRepository.findByMedicineIdOrderByExpiryDateAsc(medicineId);
    }
}