-- Sample data for the Health Camp service (runs at every start).
-- ON CONFLICT (id) DO NOTHING = skip rows that already exist, so restarting never duplicates.
-- Dates are relative to today, so "upcoming" and "pending" always look realistic in the demo.

-- 4 camps: 2 done, 2 planned
INSERT INTO health_camp (id, name, camp_type, village, venue, camp_date, start_time, end_time, centre_id, target_count, status) VALUES
 (1, 'NCD screening camp - Katpadi', 'NCD',      'Katpadi',    'Govt. Higher Secondary School', CURRENT_DATE - 5,  '09:00', '14:00', 1, 60, 'COMPLETED'),
 (2, 'Eye camp - Gudiyatham',        'EYE',      'Gudiyatham', 'Panchayat Hall',                CURRENT_DATE - 2,  '10:00', '15:00', 2, 40, 'COMPLETED'),
 (3, 'Maternal health camp - Anaicut','MATERNAL','Anaicut',    'Anganwadi Centre',              CURRENT_DATE + 4,  '09:30', '13:00', 3, 30, 'PLANNED'),
 (4, 'General health camp - Katpadi','GENERAL',  'Katpadi',    'Community Hall, Ward 3',        CURRENT_DATE + 10, '09:00', '16:00', 1, 80, 'PLANNED')
ON CONFLICT (id) DO NOTHING;

-- Residents registered (resident_id = id in the Resident service)
INSERT INTO camp_beneficiary (id, camp_id, resident_id, resident_name, village, age, gender, attendance, registered_at) VALUES
 (1, 1, 1, 'Lakshmi R.',   'Katpadi',    58, 'F', 'ATTENDED',   (CURRENT_DATE - 7)::timestamp + interval '11 hours'),
 (2, 1, 2, 'Murugan S.',   'Katpadi',    63, 'M', 'ATTENDED',   (CURRENT_DATE - 7)::timestamp + interval '11 hours'),
 (3, 1, 3, 'Selvi K.',     'Katpadi',    45, 'F', 'ATTENDED',   (CURRENT_DATE - 6)::timestamp + interval '10 hours'),
 (4, 1, 4, 'Ravi P.',      'Katpadi',    51, 'M', 'ATTENDED',   (CURRENT_DATE - 6)::timestamp + interval '10 hours'),
 (5, 2, 5, 'Saraswathi M.','Gudiyatham', 67, 'F', 'ATTENDED',   (CURRENT_DATE - 4)::timestamp + interval '9 hours'),
 (6, 2, 6, 'Kumar V.',     'Gudiyatham', 39, 'M', 'ATTENDED',   (CURRENT_DATE - 4)::timestamp + interval '9 hours'),
 (7, 3, 7, 'Priya D.',     'Anaicut',    24, 'F', 'REGISTERED', (CURRENT_DATE - 1)::timestamp + interval '12 hours'),
 (8, 3, 8, 'Divya S.',     'Anaicut',    28, 'F', 'REGISTERED', (CURRENT_DATE - 1)::timestamp + interval '12 hours')
ON CONFLICT (id) DO NOTHING;

-- Camp team (worker_id = id in the Health Centre service)
INSERT INTO camp_staff (id, camp_id, worker_id, worker_name, role, duty) VALUES
 (1, 1, 1, 'Dr. Meena',     'DOCTOR', 'Consultation'),
 (2, 1, 3, 'Kavitha (ANM)', 'NURSE',  'BP and sugar desk'),
 (3, 2, 2, 'Dr. Arun',      'DOCTOR', 'Eye examination'),
 (4, 3, 4, 'Revathi',       'ASHA',   'Registration')
ON CONFLICT (id) DO NOTHING;

-- Screenings: 2 NORMAL, 3 ABNORMAL, 1 CRITICAL
INSERT INTO screening (id, beneficiary_id, screening_type, reading, result, screened_by, remarks, screened_at) VALUES
 (1, 1, 'BP',          '152/96 mmHg', 'ABNORMAL', 'Kavitha (ANM)', 'Stage 1 hypertension',   (CURRENT_DATE - 5)::timestamp + interval '10 hours'),
 (2, 2, 'BLOOD_SUGAR', '262 mg/dL',   'CRITICAL', 'Kavitha (ANM)', 'Random sugar very high', (CURRENT_DATE - 5)::timestamp + interval '10 hours 30 minutes'),
 (3, 3, 'BP',          '118/76 mmHg', 'NORMAL',   'Kavitha (ANM)', NULL,                     (CURRENT_DATE - 5)::timestamp + interval '11 hours'),
 (4, 4, 'BMI',         '31.2',        'ABNORMAL', 'Dr. Meena',     'Obese - diet advice',    (CURRENT_DATE - 5)::timestamp + interval '11 hours 30 minutes'),
 (5, 5, 'EYE',         '6/36 both eyes','ABNORMAL','Dr. Arun',     'Suspected cataract',     (CURRENT_DATE - 2)::timestamp + interval '11 hours'),
 (6, 6, 'EYE',         '6/6',         'NORMAL',   'Dr. Arun',      NULL,                     (CURRENT_DATE - 2)::timestamp + interval '11 hours 30 minutes')
ON CONFLICT (id) DO NOTHING;

-- Follow-ups for the 4 non-normal screenings: 3 PENDING (shown on the dashboard), 1 DONE
INSERT INTO follow_up (id, screening_id, resident_id, resident_name, village, action, priority, due_date, status, outcome, closed_on) VALUES
 (1, 1, 1, 'Lakshmi R.',    'Katpadi',    'RECHECK',         'MEDIUM', CURRENT_DATE + 2, 'PENDING', NULL, NULL),
 (2, 2, 2, 'Murugan S.',    'Katpadi',    'REFER_TO_CENTRE', 'HIGH',   CURRENT_DATE - 3, 'PENDING', NULL, NULL),
 (3, 4, 4, 'Ravi P.',       'Katpadi',    'RECHECK',         'MEDIUM', CURRENT_DATE + 2, 'DONE',    'Diet chart given, BMI 30.8', CURRENT_DATE - 1),
 (4, 5, 5, 'Saraswathi M.', 'Gudiyatham', 'RECHECK',         'MEDIUM', CURRENT_DATE + 5, 'PENDING', NULL, NULL)
ON CONFLICT (id) DO NOTHING;

-- Move each id counter past the sample rows, so the next new row gets the next free id
SELECT setval(pg_get_serial_sequence('health_camp', 'id'),      (SELECT MAX(id) FROM health_camp));
SELECT setval(pg_get_serial_sequence('camp_beneficiary', 'id'), (SELECT MAX(id) FROM camp_beneficiary));
SELECT setval(pg_get_serial_sequence('camp_staff', 'id'),       (SELECT MAX(id) FROM camp_staff));
SELECT setval(pg_get_serial_sequence('screening', 'id'),        (SELECT MAX(id) FROM screening));
SELECT setval(pg_get_serial_sequence('follow_up', 'id'),        (SELECT MAX(id) FROM follow_up));
