package com.irhsn.medicine.repository;

import com.irhsn.medicine.entity.IssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {

    // A patient's medicine history, newest first.
    List<IssueRecord> findByResidentIdOrderByIssuedAtDesc(Long residentId);

    // Everything given out for one appointment.
    List<IssueRecord> findByAppointmentId(Long appointmentId);
}