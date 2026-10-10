package com.irhsn.medicine.repository;

import com.irhsn.medicine.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    // FEFO (first expiry, first out):
    // give me the batches of this medicine at this centre
    // that still have stock left (quantity > 0),
    // sorted so the batch expiring soonest comes first.
    List<Stock> findByMedicineIdAndCentreIdAndQuantityGreaterThanOrderByExpiryDateAsc(
            Long medicineId, Long centreId, Integer quantity);

    // All batches at one centre (for the stock page).
    List<Stock> findByCentreId(Long centreId);

    // Batches that expire before a given date (for the "expiring soon" list).
    List<Stock> findByExpiryDateBefore(LocalDate date);

    // Total units of one medicine at one centre, added up across all batches.
    // This one is too complex for a method name, so I write the query myself.
    // It's JPQL (uses class and field names, not table names).
    // COALESCE(..., 0) = if there are no batches, return 0 instead of null.
    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Stock s " +
            "WHERE s.medicine.id = :medicineId AND s.centreId = :centreId")
    Integer totalQuantity(@Param("medicineId") Long medicineId,
                          @Param("centreId") Long centreId);

    // Does this medicine have ANY stock batches at any centre?
    // true/false. Used to block deleting a medicine that still has stock.
    boolean existsByMedicineId(Long medicineId);
    // All batches of one medicine (at every centre), soonest expiry first.
    // Used on the medicine detail page.
    List<Stock> findByMedicineIdOrderByExpiryDateAsc(Long medicineId);
}