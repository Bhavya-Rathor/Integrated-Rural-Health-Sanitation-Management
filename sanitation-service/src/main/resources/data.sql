-- =====================================================================
-- Sanitation service – sample data (PostgreSQL)
-- Villages: Katpadi, Gudiyatham, Anaicut, Pallikonda, Kaniyambadi
--   (replace with the team's final 5 if different – use Find & Replace)
-- Safe to run on every restart:
--   * fixed ids + ON CONFLICT (id) DO NOTHING  -> no duplicate rows
--   * setval(...) at the end                   -> new rows from the app
--                                                 get id 12, 15, 8 ... not 1
-- =====================================================================

-- ---------- Facilities ----------
INSERT INTO sanitation_facility
    (id, name, type, village, location_desc, capacity, facility_condition, last_cleaned_on, caretaker_name)
VALUES
    (1,  'Katpadi Ward 3 Community Toilet',      'COMMUNITY_TOILET',       'Katpadi',     'Near bus stand',              8,  'NEEDS_REPAIR',   '2026-10-05', 'Murugan'),
    (2,  'Katpadi School Handwash Station',      'HANDWASH_STATION',       'Katpadi',     'Govt primary school',         6,  'FUNCTIONAL',     '2026-10-10', 'Lakshmi'),
    (3,  'Katpadi Market Waste Point',           'WASTE_COLLECTION_POINT', 'Katpadi',     'Weekly market ground',        4,  'FUNCTIONAL',     '2026-10-09', 'Selvam'),
    (4,  'Gudiyatham Community Sanitary Complex','COMMUNITY_TOILET',       'Gudiyatham',  'Near panchayat office',       10, 'NON_FUNCTIONAL', '2026-09-28', 'Ravi'),
    (5,  'Gudiyatham Main Street Drain',         'DRAINAGE',               'Gudiyatham',  'Main street, east end',       1,  'NEEDS_REPAIR',   '2026-10-02', 'Ravi'),
    (6,  'Anaicut Ladies Toilet Block',          'COMMUNITY_TOILET',       'Anaicut',     'Next to anganwadi',           6,  'FUNCTIONAL',     '2026-10-09', 'Devi'),
    (7,  'Anaicut PHC Handwash Station',         'HANDWASH_STATION',       'Anaicut',     'Primary health centre gate',  4,  'FUNCTIONAL',     '2026-10-10', 'Kumar'),
    (8,  'Pallikonda Bus Stand Toilet',          'COMMUNITY_TOILET',       'Pallikonda',  'Bus stand, platform side',    8,  'NEEDS_REPAIR',   '2026-10-03', 'Arjun'),
    (9,  'Pallikonda Colony Drain',              'DRAINAGE',               'Pallikonda',  'Colony road',                 1,  'FUNCTIONAL',     '2026-10-07', 'Arjun'),
    (10, 'Kaniyambadi Waste Collection Point',   'WASTE_COLLECTION_POINT', 'Kaniyambadi', 'Temple street',               3,  'FUNCTIONAL',     '2026-10-08', 'Saroja'),
    (11, 'Kaniyambadi Community Toilet',         'COMMUNITY_TOILET',       'Kaniyambadi', 'Behind high school',          6,  'FUNCTIONAL',     '2026-10-10', 'Saroja')
ON CONFLICT (id) DO NOTHING;

-- ---------- Cleanliness inspections ----------
-- Low scores / no water line up with the NEEDS_REPAIR and NON_FUNCTIONAL facilities above
INSERT INTO cleanliness_inspection
    (id, facility_id, inspection_date, inspector_name, cleanliness_score, water_available, remarks)
VALUES
    (1,  1,  '2026-09-30', 'Sanitation in-charge Ravi', 4, true,  'Clean, tank full'),
    (2,  1,  '2026-10-08', 'ASHA worker Meena',         2, false, 'Tank empty, door broken, drain blocked'),
    (3,  2,  '2026-10-10', 'Headmaster Prakash',        5, true,  'Soap and water available'),
    (4,  3,  '2026-10-09', 'Sanitation in-charge Ravi', 4, true,  'Bins emptied on time'),
    (5,  4,  '2026-09-25', 'Block coordinator Anitha',  2, true,  'Seats damaged'),
    (6,  4,  '2026-10-06', 'Block coordinator Anitha',  1, false, 'Complex locked, pipes broken'),
    (7,  5,  '2026-10-02', 'ASHA worker Kavitha',       2, true,  'Silt build-up, slow flow'),
    (8,  6,  '2026-10-09', 'Anganwadi worker Selvi',    5, true,  'Very clean'),
    (9,  7,  '2026-10-10', 'PHC nurse Jaya',            4, true,  'Working well'),
    (10, 8,  '2026-10-07', 'ASHA worker Meena',         3, false, 'No water since 5 Oct'),
    (11, 9,  '2026-10-07', 'Sanitation in-charge Ravi', 4, true,  'Flowing freely'),
    (12, 10, '2026-10-08', 'VWSC member Gopal',         3, true,  'Slight overflow on market day'),
    (13, 11, '2026-10-10', 'VWSC member Gopal',         5, true,  'Recently painted, clean'),
    (14, 6,  '2026-09-18', 'Anganwadi worker Selvi',    3, true,  'Door latch broken')
ON CONFLICT (id) DO NOTHING;

-- ---------- Complaints ----------
-- #1 is the demo item: HIGH + OPEN in Katpadi, same village as Sambhav's low-stock centre
INSERT INTO complaint
    (id, facility_id, title, description, reported_by, priority, status, reported_on, resolved_on, officer_remarks)
VALUES
    (1, 1,  'Toilet block maintenance needed',     'Door broken, drain blocked, no water in tank', 'ASHA worker Meena',      'HIGH',   'OPEN',        '2026-10-09', NULL,         NULL),
    (2, 4,  'Sanitary complex locked, pipes broken','Villagers using open ground again',           'Block coordinator Anitha','HIGH',   'ASSIGNED',    '2026-10-06', NULL,         'Plumber assigned by BDO office'),
    (3, 5,  'Drain overflowing near houses',       'Sewage water entering two houses',             'ASHA worker Kavitha',    'MEDIUM', 'IN_PROGRESS', '2026-10-03', NULL,         'Desilting started'),
    (4, 8,  'No water in toilet tank',             'Tank empty since 5 October',                   'ASHA worker Meena',      'MEDIUM', 'OPEN',        '2026-10-08', NULL,         NULL),
    (5, 2,  'Soap dispenser empty',                'Children washing hands without soap',          'Headmaster Prakash',     'LOW',    'OPEN',        '2026-10-10', NULL,         NULL),
    (6, 6,  'Door latch broken',                   'Latch on second cubicle broken',               'Anganwadi worker Selvi', 'LOW',    'RESOLVED',    '2026-09-18', '2026-09-22', 'Latch replaced'),
    (7, 10, 'Waste not collected for a week',      'Bins overflowing near temple',                 'VWSC member Gopal',      'MEDIUM', 'RESOLVED',    '2026-09-25', '2026-09-28', 'Collection schedule restored')
ON CONFLICT (id) DO NOTHING;

-- ---------- Move the auto-id counters past the fixed ids ----------
SELECT setval(pg_get_serial_sequence('sanitation_facility', 'id'),    (SELECT MAX(id) FROM sanitation_facility));
SELECT setval(pg_get_serial_sequence('cleanliness_inspection', 'id'), (SELECT MAX(id) FROM cleanliness_inspection));
SELECT setval(pg_get_serial_sequence('complaint', 'id'),              (SELECT MAX(id) FROM complaint));
