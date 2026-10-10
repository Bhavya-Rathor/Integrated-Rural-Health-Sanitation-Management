-- =====================================================================
-- Water & Public Health service – sample data (PostgreSQL)
-- Same 5 villages as Sanitation: Katpadi, Gudiyatham, Anaicut, Pallikonda, Kaniyambadi
-- nearby_facility_id values point at Sanitation's sample facilities (1, 4, 8)
-- Safe on every restart: fixed ids + ON CONFLICT DO NOTHING, then setval
-- =====================================================================

-- ---------- Water sources ----------
INSERT INTO water_source
    (id, name, type, village, location_desc, households_served, functional)
VALUES
    (1, 'Katpadi Overhead Tank Supply',   'TAP_SUPPLY', 'Katpadi',     'Ward 3 overhead tank',     120, true),
    (2, 'Katpadi School Borewell',        'BOREWELL',   'Katpadi',     'Govt primary school',       30, true),
    (3, 'Gudiyatham Handpump HP-03',      'HANDPUMP',   'Gudiyatham',  'Near panchayat office',     45, true),
    (4, 'Gudiyatham Open Well',           'OPEN_WELL',  'Gudiyatham',  'East street',               20, true),
    (5, 'Anaicut Tap Supply',             'TAP_SUPPLY', 'Anaicut',     'Main road distribution',    90, true),
    (6, 'Pallikonda Bus Stand Handpump',  'HANDPUMP',   'Pallikonda',  'Bus stand',                 35, true),
    (7, 'Pallikonda Colony Tank',         'TANK',       'Pallikonda',  'Colony road',               60, false),
    (8, 'Kaniyambadi Borewell',           'BOREWELL',   'Kaniyambadi', 'Temple street',             50, true)
ON CONFLICT (id) DO NOTHING;

-- ---------- Water quality tests ----------
-- result / failed_parameters match what WaterQualityStandard would compute
INSERT INTO water_quality_test
    (id, source_id, test_date, tested_by, ph, tds, residual_chlorine, turbidity, coliform_present, result, failed_parameters)
VALUES
    (1,  1, '2026-10-04', 'FTK - ASHA worker Meena',      7.3, 280, 0.4,  0.6, false, 'SAFE',   NULL),
    (2,  1, '2026-10-10', 'FTK - ASHA worker Meena',      7.1, 300, 0.1,  0.9, false, 'UNSAFE', 'Chlorine 0.1 mg/L'),
    (3,  2, '2026-10-06', 'FTK - Headmaster Prakash',     7.6, 450, NULL, 0.7, false, 'SAFE',   NULL),
    (4,  3, '2026-09-30', 'FTK - Anganwadi worker Selvi', 7.4, 480, NULL, 1.2, false, 'SAFE',   NULL),
    (5,  3, '2026-10-09', 'FTK - Anganwadi worker Selvi', 7.5, 910, NULL, 2.1, true,  'UNSAFE', 'TDS 910.0 mg/L, Coliform present'),
    (6,  4, '2026-10-08', 'District water lab, Vellore',  6.2, 640, NULL, 6.5, true,  'UNSAFE', 'pH 6.2, TDS 640.0 mg/L, Turbidity 6.5 NTU, Coliform present'),
    (7,  5, '2026-10-07', 'PHC nurse Jaya',               7.0, 210, 0.5,  0.4, false, 'SAFE',   NULL),
    (8,  6, '2026-10-05', 'FTK - ASHA worker Meena',      7.8, 520, NULL, 1.0, false, 'UNSAFE', 'TDS 520.0 mg/L'),
    (9,  6, '2026-10-10', 'FTK - ASHA worker Meena',      7.7, 470, NULL, 0.9, false, 'SAFE',   NULL),
    (10, 8, '2026-10-08', 'VWSC member Gopal',            7.2, 390, NULL, 0.8, false, 'SAFE',   NULL),
    (11, 7, '2026-09-26', 'VWSC member Gopal',            7.0, 350, 0.0,  3.0, true,  'UNSAFE', 'Chlorine 0.0 mg/L, Coliform present')
ON CONFLICT (id) DO NOTHING;

-- ---------- Public health issues ----------
-- #1 is the demo item: HIGH + OPEN, coliform, linked to Sanitation facility 4
INSERT INTO public_health_issue
    (id, source_id, test_id, title, description, village, priority, status, reported_on, resolved_on, officer_remarks, nearby_facility_id, nearby_facility_name)
VALUES
    (1, 3, 5,  'Unsafe water at Gudiyatham Handpump HP-03',     'Failed: TDS 910.0 mg/L, Coliform present',                                   'Gudiyatham', 'HIGH',   'OPEN',        '2026-10-09', NULL,         NULL,                                  4, 'Gudiyatham Community Sanitary Complex'),
    (2, 4, 6,  'Unsafe water at Gudiyatham Open Well',          'Failed: pH 6.2, TDS 640.0 mg/L, Turbidity 6.5 NTU, Coliform present',      'Gudiyatham', 'HIGH',   'ASSIGNED',    '2026-10-08', NULL,         'Well closed, tanker supply arranged', 4, 'Gudiyatham Community Sanitary Complex'),
    (3, 1, 2,  'Unsafe water at Katpadi Overhead Tank Supply',  'Failed: Chlorine 0.1 mg/L',                                                  'Katpadi',    'MEDIUM', 'IN_PROGRESS', '2026-10-10', NULL,         NULL,                                  1, 'Katpadi Ward 3 Community Toilet'),
    (4, 6, 8,  'Unsafe water at Pallikonda Bus Stand Handpump', 'Failed: TDS 520.0 mg/L',                                                     'Pallikonda', 'MEDIUM', 'RESOLVED',    '2026-10-05', '2026-10-10', 'Retest safe on 10 Oct',               8, 'Pallikonda Bus Stand Toilet'),
    (5, 7, 11, 'Unsafe water at Pallikonda Colony Tank',        'Failed: Chlorine 0.0 mg/L, Coliform present',                                'Pallikonda', 'HIGH',   'RESOLVED',    '2026-09-26', '2026-10-01', 'Tank cleaned and disinfected',        8, 'Pallikonda Bus Stand Toilet')
ON CONFLICT (id) DO NOTHING;

-- ---------- Corrective actions ----------
INSERT INTO corrective_action
    (id, issue_id, action_type, description, assigned_to, action_date, status)
VALUES
    (1, 2, 'ALTERNATE_SUPPLY', 'Tanker water supply to east street',      'BDO office',          '2026-10-09', 'IN_PROGRESS'),
    (2, 3, 'CHLORINATION',     'Increase chlorine dosing at tank outlet', 'Pump operator Mani',  '2026-10-10', 'PLANNED'),
    (3, 4, 'AWARENESS',        'Advise households to boil water',         'ASHA worker Meena',   '2026-10-05', 'DONE'),
    (4, 4, 'PIPE_REPAIR',      'Replace cracked riser pipe',              'PHED field engineer', '2026-10-07', 'DONE'),
    (5, 5, 'CHLORINATION',     'Clean and disinfect the tank',            'VWSC member Gopal',   '2026-09-27', 'DONE')
ON CONFLICT (id) DO NOTHING;

-- ---------- Move the auto-id counters past the fixed ids ----------
SELECT setval(pg_get_serial_sequence('water_source', 'id'),        (SELECT MAX(id) FROM water_source));
SELECT setval(pg_get_serial_sequence('water_quality_test', 'id'),  (SELECT MAX(id) FROM water_quality_test));
SELECT setval(pg_get_serial_sequence('public_health_issue', 'id'), (SELECT MAX(id) FROM public_health_issue));
SELECT setval(pg_get_serial_sequence('corrective_action', 'id'),   (SELECT MAX(id) FROM corrective_action));
