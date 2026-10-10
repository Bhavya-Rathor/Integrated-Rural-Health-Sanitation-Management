package com.irhsn.dashboard.dto;

import java.util.Map;

// One tile on the dashboard.
// available = false when the service is down or has no /summary.
// counts    = whatever /summary returned, e.g. {total=12, pending=3}
public record ServiceTile(String key, String label, boolean available, Map<String, Object> counts) {
}