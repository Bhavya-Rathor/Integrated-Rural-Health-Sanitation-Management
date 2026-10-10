package com.irhsm.healthCampService.dto;

import com.irhsm.healthCampService.entity.FollowUp;
import com.irhsm.healthCampService.entity.Screening;

// The shared team format for /pending (same 7 fields in every service).
// For health camps, a pending item is a follow-up that still needs action.
public record PendingItemDto(
        String source,
        Long id,
        String title,
        String village,
        String status,
        String priority,
        String date
) {
    public static PendingItemDto from(FollowUp f) {
        Screening s = f.getScreening();
        String action = switch (f.getAction()) {
            case REFER_TO_CENTRE -> "Refer to centre";
            case HOME_VISIT -> "Home visit";
            case RECHECK -> "Recheck";
        };
        // e.g. "Refer to centre: Lakshmi R. (BLOOD_SUGAR 260 mg/dL)"
        String title = action + ": " + f.getResidentName()
                + " (" + s.getScreeningType() + " " + s.getReading() + ")";
        String date = s.getScreenedAt() != null ? s.getScreenedAt().toLocalDate().toString() : null;
        return new PendingItemDto("camps", f.getId(), title, f.getVillage(),
                f.getStatus().name(), f.getPriority().name(), date);
    }
}
