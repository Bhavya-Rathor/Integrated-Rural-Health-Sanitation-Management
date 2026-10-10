package com.irhsn.medicine.repository;

import com.irhsn.medicine.entity.ReplenishmentRequest;
import com.irhsn.medicine.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplenishmentRequestRepository extends JpaRepository<ReplenishmentRequest, Long> {

    // All requests with a given status, e.g. all PENDING ones
    // (used by /pending for Tvisha's dashboard).
    List<ReplenishmentRequest> findByStatus(RequestStatus status);

    // How many requests have a given status (used by /summary).
    long countByStatus(RequestStatus status);

    // Is there already a PENDING request for this medicine at this centre?
    // Used to stop the system from creating duplicate requests.
    boolean existsByMedicineIdAndCentreIdAndStatus(
            Long medicineId, Long centreId, RequestStatus status);
}