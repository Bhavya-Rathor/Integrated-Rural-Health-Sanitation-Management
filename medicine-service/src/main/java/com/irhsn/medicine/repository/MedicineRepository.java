package com.irhsn.medicine.repository;


import com.irhsn.medicine.entity.Medicine;
import com.irhsn.medicine.enums.DosageForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// "extends JpaRepository<Medicine, Long>" means:
// this worker handles the Medicine table, whose ID type is Long.
// That one line gives me save, findById, findAll, deleteById, count... for free.
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    // Search box: find medicines whose name contains the typed text,
    // ignoring capital/small letters. "para" finds "Paracetamol 500 mg".
    List<Medicine> findByNameContainingIgnoreCase(String text);

    // Filter dropdown: only tablets, only syrups, etc.
    List<Medicine> findByForm(DosageForm form);
}
