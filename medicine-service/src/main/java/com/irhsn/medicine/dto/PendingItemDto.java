package com.irhsn.medicine.dto;

import java.time.LocalDate;

// The team-agreed shape for one pending item.
// Every service's /pending returns a list of exactly these 7 fields.
public record PendingItemDto(
        String source,    // which service: "medicine"
        Long id,          // the request id (used later in PUT /{id}/status)
        String title,     // e.g. "Restock Paracetamol 500 mg at Kalpakkam"
        String village,
        String status,    // "PENDING"
        String priority,  // "HIGH" / "MEDIUM" / "LOW"
        LocalDate date    // when it was raised
) {
}