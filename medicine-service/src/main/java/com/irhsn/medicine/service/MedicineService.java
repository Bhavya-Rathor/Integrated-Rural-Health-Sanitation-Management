package com.irhsn.medicine.service;

import com.irhsn.medicine.entity.Medicine;
import com.irhsn.medicine.enums.DosageForm;
import com.irhsn.medicine.exception.BadRequestException;
import com.irhsn.medicine.exception.NotFoundException;
import com.irhsn.medicine.repository.MedicineRepository;
import com.irhsn.medicine.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Manager of the medicine catalogue.
// @Service = Spring creates one at startup and gives it to the controllers.
@Service
public class MedicineService {

    // The two "clerks" (repositories) this manager uses.
    private final MedicineRepository medicineRepository;
    private final StockRepository stockRepository;

    // Constructor injection: Spring passes the repositories in automatically.
    public MedicineService(MedicineRepository medicineRepository,
                           StockRepository stockRepository) {
        this.medicineRepository = medicineRepository;
        this.stockRepository = stockRepository;
    }

    // ===== LIST MEDICINES =====
    // Search text typed -> search by name.
    // Form picked       -> filter by form.
    // Nothing           -> show all.
    public List<Medicine> findAll(String search, DosageForm form) {
        if (search != null && !search.isBlank()) {
            return medicineRepository.findByNameContainingIgnoreCase(search);
        }
        if (form != null) {
            return medicineRepository.findByForm(form);
        }
        return medicineRepository.findAll();
    }

    // ===== GET ONE =====
    // findById gives an Optional (a box that may be empty).
    // orElseThrow = if empty, throw NotFoundException (becomes 404 later).
    public Medicine getById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Medicine " + id + " not found"));
    }

    // ===== ADD OR EDIT =====
    // No id -> INSERT new row. Has id -> UPDATE existing row.
    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    // ===== DELETE =====
    // 1. Check it exists (404 if not).
    // 2. Refuse if it still has stock batches (400).
    // 3. Delete.
    public void delete(Long id) {
        getById(id);
        if (stockRepository.existsByMedicineId(id)) {
            throw new BadRequestException(
                    "Cannot delete: this medicine still has stock batches");
        }
        medicineRepository.deleteById(id);
    }
}