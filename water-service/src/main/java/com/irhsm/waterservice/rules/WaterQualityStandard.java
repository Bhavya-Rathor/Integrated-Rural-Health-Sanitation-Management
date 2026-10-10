package com.irhsm.waterservice.rules;

import com.irhsm.waterservice.dto.WaterTestRequest;

import java.util.ArrayList;
import java.util.List;

// Limits from BIS IS 10500:2012 (drinking water specification)
public final class WaterQualityStandard {

    public static final double PH_MIN = 6.5;
    public static final double PH_MAX = 8.5;
    public static final double TDS_MAX = 500;          // mg/L, acceptable limit
    public static final double CHLORINE_MIN = 0.2;     // mg/L, when water is chlorinated
    public static final double TURBIDITY_MAX = 5;      // NTU, permissible limit

    private WaterQualityStandard() { }                 // utility class: never instantiated

    // Returns every parameter that breaks the standard; an empty list means SAFE
    public static List<String> failedParameters(WaterTestRequest r) {
        List<String> failed = new ArrayList<>();
        if (r.ph() < PH_MIN || r.ph() > PH_MAX) {
            failed.add("pH " + r.ph());
        }
        if (r.tds() > TDS_MAX) {
            failed.add("TDS " + r.tds() + " mg/L");
        }
        if (r.residualChlorine() != null && r.residualChlorine() < CHLORINE_MIN) {
            failed.add("Chlorine " + r.residualChlorine() + " mg/L");
        }
        if (r.turbidity() > TURBIDITY_MAX) {
            failed.add("Turbidity " + r.turbidity() + " NTU");
        }
        if (Boolean.TRUE.equals(r.coliformPresent())) {
            failed.add("Coliform present");
        }
        return failed;
    }
}