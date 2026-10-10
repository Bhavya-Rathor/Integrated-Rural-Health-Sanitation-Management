package com.irhsm.Resident.dto;

import com.irhsm.Resident.entity.FollowUpStatus;
import com.irhsm.Resident.entity.Priority;
import com.irhsm.Resident.entity.VisitRecord;
import com.irhsm.Resident.entity.VisitType;

import java.time.LocalDate;

public record VisitDto(Long id, Long residentId, LocalDate visitDate, VisitType visitType,
                       String sourceService, Long sourceRefId, Long centreId,
                       String complaint, String diagnosis, String notes,
                       boolean followUpRequired, LocalDate followUpDate,
                       FollowUpStatus followUpStatus, Priority priority) {

    public static VisitDto from(VisitRecord v) {
        return new VisitDto(
                v.getId(),
                v.getResident().getId(),
                v.getVisitDate(),
                v.getVisitType(),
                v.getSourceService(),
                v.getSourceRefId(),
                v.getCentreId(),
                v.getComplaint(),
                v.getDiagnosis(),
                v.getNotes(),
                v.isFollowUpRequired(),
                v.getFollowUpDate(),
                v.getFollowUpStatus(),
                v.getPriority());
    }

    // Used when another service (Appointment) POSTs a new visit to us
    public VisitRecord toEntity() {
        VisitRecord v = new VisitRecord();
        v.setVisitDate(visitDate);
        v.setVisitType(visitType);
        v.setSourceService(sourceService);
        v.setSourceRefId(sourceRefId);
        v.setCentreId(centreId);
        v.setComplaint(complaint);
        v.setDiagnosis(diagnosis);
        v.setNotes(notes);
        v.setFollowUpRequired(followUpRequired);
        v.setFollowUpDate(followUpDate);
        v.setPriority(priority != null ? priority : Priority.MEDIUM);
        return v;
    }
}