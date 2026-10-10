-- =========================================================
-- Sample data for the Medicine & Inventory service
-- Runs on every startup. ON CONFLICT (id) DO NOTHING = skip
-- rows that already exist, so restarts never duplicate data.
-- Centres 1-3 and residents 1-6 must match Bhavya's and
-- Tvisha's sample data IDs.
-- =========================================================

-- ---------- Medicines (catalogue) ----------
INSERT INTO medicine (id, name, generic_name, form, strength, unit, reorder_level, essential) VALUES
                                                                                                  (1,  'Paracetamol 500 mg',    'Paracetamol',    'TABLET',     '500 mg',  'tablet', 200, false),
                                                                                                  (2,  'Amoxicillin 250 mg',    'Amoxicillin',    'CAPSULE',    '250 mg',  'capsule',150, false),
                                                                                                  (3,  'Metformin 500 mg',      'Metformin',      'TABLET',     '500 mg',  'tablet', 150, true),
                                                                                                  (4,  'Amlodipine 5 mg',       'Amlodipine',     'TABLET',     '5 mg',    'tablet', 150, true),
                                                                                                  (5,  'ORS sachet',            'Oral rehydration salts', 'CONSUMABLE', '20.5 g', 'sachet', 100, true),
                                                                                                  (6,  'Iron-Folic Acid',       'Ferrous sulphate + folic acid', 'TABLET', '100 mg', 'tablet', 200, true),
                                                                                                  (7,  'Albendazole 400 mg',    'Albendazole',    'TABLET',     '400 mg',  'tablet', 50,  false),
                                                                                                  (8,  'Cetirizine 10 mg',      'Cetirizine',     'TABLET',     '10 mg',   'tablet', 80,  false),
                                                                                                  (9,  'Zinc 20 mg',            'Zinc sulphate',  'TABLET',     '20 mg',   'tablet', 100, false),
                                                                                                  (10, 'Cough syrup 100 ml',    'Dextromethorphan','SYRUP',     '100 ml',  'bottle', 30,  false),
                                                                                                  (11, 'Povidone-iodine ointment','Povidone-iodine','OINTMENT', '5%',      'tube',   20,  false),
                                                                                                  (12, 'Sterile gauze pack',    'Gauze',          'CONSUMABLE', '10 x 10 cm','pack', 40,  false)
    ON CONFLICT (id) DO NOTHING;

-- ---------- Stock batches (centre 1, 2, 3) ----------
-- Centre 2 is the "limited medicine stock" centre from the problem statement:
-- Metformin and Amlodipine there are below their reorder level.
-- Some batches expire within 30 days, to show the "expiring soon" list.
INSERT INTO stock (id, medicine_id, centre_id, batch_no, quantity, expiry_date, received_date) VALUES
                                                                                                   (1,  1, 1, 'PCM-2601', 120, CURRENT_DATE + 20,  CURRENT_DATE - 160),
                                                                                                   (2,  1, 1, 'PCM-2609', 400, CURRENT_DATE + 400, CURRENT_DATE - 20),
                                                                                                   (3,  2, 1, 'AMX-2603', 250, CURRENT_DATE + 210, CURRENT_DATE - 60),
                                                                                                   (4,  3, 1, 'MET-2604', 300, CURRENT_DATE + 300, CURRENT_DATE - 45),
                                                                                                   (5,  4, 1, 'AML-2605', 260, CURRENT_DATE + 330, CURRENT_DATE - 40),
                                                                                                   (6,  5, 1, 'ORS-2602', 180, CURRENT_DATE + 25,  CURRENT_DATE - 140),
                                                                                                   (7,  1, 2, 'PCM-2607', 350, CURRENT_DATE + 280, CURRENT_DATE - 30),
                                                                                                   (8,  3, 2, 'MET-2601', 40,  CURRENT_DATE + 90,  CURRENT_DATE - 120),
                                                                                                   (9,  4, 2, 'AML-2602', 0,   CURRENT_DATE + 60,  CURRENT_DATE - 150),
                                                                                                   (10, 6, 2, 'IFA-2606', 500, CURRENT_DATE + 360, CURRENT_DATE - 25),
                                                                                                   (11, 8, 2, 'CTZ-2604', 90,  CURRENT_DATE + 15,  CURRENT_DATE - 100),
                                                                                                   (12, 1, 3, 'PCM-2608', 300, CURRENT_DATE + 320, CURRENT_DATE - 15),
                                                                                                   (13, 5, 3, 'ORS-2605', 45,  CURRENT_DATE + 200, CURRENT_DATE - 50),
                                                                                                   (14, 7, 3, 'ALB-2603', 120, CURRENT_DATE + 250, CURRENT_DATE - 70),
                                                                                                   (15, 10,3, 'CSY-2602', 40,  CURRENT_DATE + 180, CURRENT_DATE - 80),
                                                                                                   (16, 12,3, 'GZP-2601', 60,  CURRENT_DATE + 500, CURRENT_DATE - 90)
    ON CONFLICT (id) DO NOTHING;

-- ---------- Issue records (medicine given to residents) ----------
INSERT INTO issue_record (id, stock_id, quantity, resident_id, appointment_id, issued_by, issued_at) VALUES
                                                                                                         (1, 1,  10, 1, 1,    'Pharmacist Ravi',  NOW() - INTERVAL '6 days'),
                                                                                                         (2, 3,  15, 2, 2,    'Pharmacist Ravi',  NOW() - INTERVAL '5 days'),
                                                                                                         (3, 8,  30, 3, 3,    'Nurse Lakshmi',    NOW() - INTERVAL '4 days'),
                                                                                                         (4, 9,  20, 4, 4,    'Nurse Lakshmi',    NOW() - INTERVAL '3 days'),
                                                                                                         (5, 13, 10, 5, NULL, 'ASHA worker Meena',NOW() - INTERVAL '2 days'),
                                                                                                         (6, 7,  12, 6, 5,    'Nurse Lakshmi',    NOW() - INTERVAL '1 day')
    ON CONFLICT (id) DO NOTHING;

-- ---------- Replenishment requests ----------
-- Villages: replace with the 5 village names your team agreed on,
-- matching each centre in Bhavya's data.
INSERT INTO replenishment_request
(id, medicine_id, centre_id, village, current_qty, requested_qty, status, priority, source, remarks, created_at, updated_at) VALUES
                                                                                                                                 (1, 4, 2, 'Katpadi',    0,   300, 'PENDING',  'HIGH',   'AUTO',   NULL,                              NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days'),
                                                                                                                                 (2, 3, 2, 'Katpadi',    40,  260, 'PENDING',  'HIGH',   'AUTO',   NULL,                              NOW() - INTERVAL '4 days', NOW() - INTERVAL '4 days'),
                                                                                                                                 (3, 5, 3, 'Gudiyatham', 45,  200, 'PENDING',  'HIGH',   'MANUAL', 'ORS running out before monsoon',  NOW() - INTERVAL '1 day',  NOW() - INTERVAL '1 day'),
                                                                                                                                 (4, 2, 1, 'Arcot',      120, 180, 'APPROVED', 'LOW',    'AUTO',   'Dispatch from district store',    NOW() - INTERVAL '8 days', NOW() - INTERVAL '6 days'),
                                                                                                                                 (5, 1, 1, 'Arcot',      150, 250, 'RECEIVED', 'LOW',    'AUTO',   'Received in full',                NOW() - INTERVAL '20 days',NOW() - INTERVAL '15 days')
    ON CONFLICT (id) DO NOTHING;

-- ---------- Move the id counters past the sample ids ----------
-- Without this, the next medicine you add from a form would try to use id 1
-- and fail, because id 1 already exists.
SELECT setval(pg_get_serial_sequence('medicine', 'id'), (SELECT MAX(id) FROM medicine));
SELECT setval(pg_get_serial_sequence('stock', 'id'), (SELECT MAX(id) FROM stock));
SELECT setval(pg_get_serial_sequence('issue_record', 'id'), (SELECT MAX(id) FROM issue_record));
SELECT setval(pg_get_serial_sequence('replenishment_request', 'id'), (SELECT MAX(id) FROM replenishment_request));