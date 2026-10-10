-- Residents (ids 1-25, 5 per village)
INSERT INTO resident (id, full_name, gender, date_of_birth, phone, village, household_no, abha_number, registered_on, active) VALUES
(1, 'Lakshmi R', 'FEMALE', '1968-04-12', '9876543210', 'Katpadi', 'H-001', '91234567890123', '2026-09-01', true),
(2, 'Murugan S', 'MALE', '1965-09-03', '9876500011', 'Katpadi', 'H-001', NULL, '2026-09-01', true),
(3, 'Divya M', 'FEMALE', '1995-02-20', '9876500012', 'Katpadi', 'H-002', NULL, '2026-09-01', true),
(4, 'Arjun K', 'MALE', '2016-07-15', NULL, 'Katpadi', 'H-002', NULL, '2026-09-01', true),
(5, 'Selvi P', 'FEMALE', '1958-11-30', '9876500014', 'Katpadi', 'H-003', NULL, '2026-09-01', true),
(6, 'Ravi Kumar', 'MALE', '1979-01-25', '9876500015', 'Sathuvachari', 'H-010', '91234567890124', '2026-09-02', true),
(7, 'Meena Devi', 'FEMALE', '1983-06-08', '9876500016', 'Sathuvachari', 'H-010', NULL, '2026-09-02', true),
(8, 'Karthik R', 'MALE', '2010-03-19', NULL, 'Sathuvachari', 'H-010', NULL, '2026-09-02', true),
(9, 'Anjali S', 'FEMALE', '1999-12-02', '9876500018', 'Sathuvachari', 'H-011', NULL, '2026-09-02', true),
(10, 'Ganesan V', 'MALE', '1950-05-14', '9876500019', 'Sathuvachari', 'H-012', NULL, '2026-09-02', true),
(11, 'Priya N', 'FEMALE', '1990-08-21', '9876500020', 'Bagayam', 'H-020', NULL, '2026-09-03', true),
(12, 'Suresh B', 'MALE', '1972-10-11', '9876500021', 'Bagayam', 'H-020', NULL, '2026-09-03', true),
(13, 'Kavya S', 'FEMALE', '2019-04-04', NULL, 'Bagayam', 'H-021', NULL, '2026-09-03', true),
(14, 'Mani T', 'MALE', '1962-02-28', '9876500023', 'Bagayam', 'H-022', NULL, '2026-09-03', true),
(15, 'Revathi G', 'FEMALE', '1987-09-09', '9876500024', 'Bagayam', 'H-022', NULL, '2026-09-03', true),
(16, 'Prakash J', 'MALE', '1975-03-17', '9876500025', 'Gudiyatham', 'H-030', NULL, '2026-09-04', true),
(17, 'Saranya L', 'FEMALE', '1993-07-07', '9876500026', 'Gudiyatham', 'H-030', NULL, '2026-09-04', true),
(18, 'Vignesh P', 'MALE', '2008-11-11', NULL, 'Gudiyatham', 'H-031', NULL, '2026-09-04', true),
(19, 'Parvathi K', 'FEMALE', '1955-01-05', '9876500028', 'Gudiyatham', 'H-032', NULL, '2026-09-04', true),
(20, 'Ramesh A', 'MALE', '1980-12-24', '9876500029', 'Gudiyatham', 'H-033', NULL, '2026-09-04', true),
(21, 'Geetha M', 'FEMALE', '1970-06-16', '9876500030', 'Ponnai', 'H-040', NULL, '2026-09-05', true),
(22, 'Senthil R', 'MALE', '1968-08-08', '9876500031', 'Ponnai', 'H-040', NULL, '2026-09-05', true),
(23, 'Nandhini S', 'FEMALE', '2001-05-30', '9876500032', 'Ponnai', 'H-041', NULL, '2026-09-05', true),
(24, 'Bala K', 'MALE', '2014-09-12', NULL, 'Ponnai', 'H-041', NULL, '2026-09-05', true),
(25, 'Kamala V', 'FEMALE', '1949-10-01', '9876500034', 'Ponnai', 'H-042', NULL, '2026-09-05', true)
ON CONFLICT (id) DO NOTHING;

-- Medical profiles (one per resident)
INSERT INTO medical_info (id, resident_id, blood_group, height_cm, weight_kg, allergies, chronic_conditions, current_medications, last_updated) VALUES
(1, 1, 'B+', 155, 68, 'None', 'Hypertension', 'Amlodipine 5 mg', '2026-09-28'),
(2, 2, 'O+', 168, 75, 'None', 'Diabetes', 'Metformin 500 mg', '2026-09-30'),
(3, 3, 'A+', 160, 55, 'Penicillin', 'None', NULL, '2026-09-12'),
(4, 4, 'O+', 120, 22, 'None', 'None', NULL, '2026-09-15'),
(5, 5, 'AB+', 150, 60, 'None', 'Hypertension, Diabetes', 'Amlodipine 5 mg, Metformin 500 mg', '2026-09-18'),
(6, 6, 'B-', 172, 70, 'None', 'None', NULL, '2026-09-20'),
(7, 7, 'A-', 158, 62, 'None', 'None', 'Iron and folic acid', '2026-09-22'),
(8, 8, 'O+', 145, 38, 'None', 'None', NULL, '2026-09-22'),
(9, 9, 'B+', 162, 54, 'None', 'None', NULL, '2026-09-24'),
(10, 10, 'O-', 165, 64, 'None', 'Hypertension', 'Telmisartan 40 mg', '2026-09-25'),
(11, 11, 'A+', 157, 58, 'None', 'None', NULL, '2026-09-26'),
(12, 12, 'B+', 170, 78, 'Sulfa drugs', 'None', NULL, '2026-09-27'),
(13, 13, 'O+', 100, 16, 'None', 'None', NULL, '2026-10-02'),
(14, 14, 'A+', 166, 72, 'None', 'Diabetes', 'Metformin 500 mg', '2026-10-01'),
(15, 15, 'AB-', 159, 57, 'None', 'None', NULL, '2026-09-29'),
(16, 16, 'O+', 171, 74, 'None', 'None', NULL, '2026-10-01'),
(17, 17, 'B+', 161, 53, 'Peanuts', 'None', NULL, '2026-10-02'),
(18, 18, 'A+', 160, 48, 'None', 'None', NULL, '2026-10-03'),
(19, 19, 'O+', 152, 63, 'None', 'Hypertension', 'Amlodipine 5 mg', '2026-10-05'),
(20, 20, 'B+', 169, 76, 'None', 'None', NULL, '2026-10-04'),
(21, 21, 'A+', 156, 66, 'None', 'None', 'Thyroxine 50 mcg', '2026-10-04'),
(22, 22, 'O+', 167, 80, 'None', 'Diabetes', 'Metformin 500 mg', '2026-10-07'),
(23, 23, 'B+', 158, 50, 'None', 'None', 'Iron tablets', '2026-10-03'),
(24, 24, 'A+', 130, 27, 'None', 'None', NULL, '2026-10-08'),
(25, 25, 'O+', 148, 58, 'None', 'Hypertension', 'Amlodipine 5 mg', '2026-10-06')
ON CONFLICT (id) DO NOTHING;

-- Visits: ids 1-6 are overdue follow-ups (show on dashboard), 7-8 are due later (must NOT show)
INSERT INTO visit_record (id, resident_id, visit_date, visit_type, source_service, source_ref_id, centre_id, complaint, diagnosis, notes, follow_up_required, follow_up_date, follow_up_status, priority) VALUES
(1, 1, '2026-09-28', 'CENTRE', 'APPOINTMENT', 1, 1, 'Headache and high BP', 'Hypertension', 'BP 160/100', true, '2026-10-05', 'PENDING', 'HIGH'),
(2, 2, '2026-09-30', 'CAMP', 'CAMP', NULL, NULL, 'High sugar at camp screening', 'Diabetes', 'RBS 260', true, '2026-10-07', 'PENDING', 'HIGH'),
(3, 10, '2026-09-25', 'HOME_VISIT', 'MANUAL', NULL, NULL, 'Dizziness', 'Hypertension', NULL, true, '2026-10-06', 'PENDING', 'MEDIUM'),
(4, 14, '2026-10-01', 'CENTRE', 'APPOINTMENT', 3, 2, 'Foot wound healing slowly', 'Diabetic foot', 'Dressing done', true, '2026-10-08', 'PENDING', 'MEDIUM'),
(5, 13, '2026-10-02', 'CENTRE', 'APPOINTMENT', 4, 2, 'Fever and cough', 'Viral fever', NULL, true, '2026-10-09', 'PENDING', 'LOW'),
(6, 23, '2026-10-03', 'HOME_VISIT', 'MANUAL', NULL, NULL, 'Fatigue', 'Mild anaemia', 'Iron tablets given', true, '2026-10-10', 'PENDING', 'LOW'),
(7, 19, '2026-10-05', 'CAMP', 'CAMP', NULL, NULL, 'BP screening', 'Hypertension', NULL, true, '2026-10-25', 'PENDING', 'MEDIUM'),
(8, 25, '2026-10-06', 'CENTRE', 'APPOINTMENT', 5, 3, 'Knee pain', 'Osteoarthritis', NULL, true, '2026-10-25', 'PENDING', 'LOW'),
(9, 1, '2026-09-10', 'CENTRE', 'APPOINTMENT', 2, 1, 'Routine BP check', 'Hypertension', 'Stable', false, NULL, 'NONE', 'MEDIUM'),
(10, 3, '2026-09-12', 'CENTRE', 'APPOINTMENT', 6, 1, 'Skin rash', 'Allergic dermatitis', 'Avoid penicillin', false, NULL, 'NONE', 'MEDIUM'),
(11, 4, '2026-09-15', 'CAMP', 'CAMP', NULL, NULL, 'School health screening', 'Healthy', NULL, false, NULL, 'NONE', 'LOW'),
(12, 5, '2026-09-18', 'HOME_VISIT', 'MANUAL', NULL, NULL, 'Joint pain', 'Arthritis', NULL, false, NULL, 'NONE', 'MEDIUM'),
(13, 6, '2026-09-20', 'CENTRE', 'APPOINTMENT', 7, 1, 'Back pain', 'Muscle strain', NULL, false, NULL, 'NONE', 'LOW'),
(14, 7, '2026-09-22', 'CENTRE', 'APPOINTMENT', 8, 1, 'Antenatal check-up', 'Normal pregnancy', 'Iron and folic acid given', false, NULL, 'NONE', 'MEDIUM'),
(15, 8, '2026-09-22', 'CAMP', 'CAMP', NULL, NULL, 'Eye screening', 'Mild myopia', 'Referred for glasses', false, NULL, 'NONE', 'LOW'),
(16, 9, '2026-09-24', 'CENTRE', 'APPOINTMENT', 9, 2, 'Stomach ache', 'Gastritis', NULL, false, NULL, 'NONE', 'LOW'),
(17, 11, '2026-09-26', 'HOME_VISIT', 'MANUAL', NULL, NULL, 'Postnatal visit', 'Mother and baby healthy', NULL, false, NULL, 'NONE', 'MEDIUM'),
(18, 12, '2026-09-27', 'CENTRE', 'APPOINTMENT', 10, 2, 'Fever', 'Typhoid suspected', 'Blood test advised', false, NULL, 'NONE', 'HIGH'),
(19, 15, '2026-09-29', 'CAMP', 'CAMP', NULL, NULL, 'Anaemia screening', 'Normal', NULL, false, NULL, 'NONE', 'LOW'),
(20, 16, '2026-10-01', 'CENTRE', 'APPOINTMENT', 11, 3, 'Cough for 2 weeks', 'Bronchitis', NULL, false, NULL, 'NONE', 'MEDIUM'),
(21, 17, '2026-10-02', 'CENTRE', 'APPOINTMENT', 12, 3, 'Allergic reaction', 'Peanut allergy', 'Antihistamine given', false, NULL, 'NONE', 'HIGH'),
(22, 18, '2026-10-03', 'CAMP', 'CAMP', NULL, NULL, 'Dental screening', 'Dental caries', NULL, false, NULL, 'NONE', 'LOW'),
(23, 20, '2026-10-04', 'HOME_VISIT', 'MANUAL', NULL, NULL, 'Diarrhoea', 'Gastroenteritis', 'ORS given', false, NULL, 'NONE', 'MEDIUM'),
(24, 21, '2026-10-04', 'CENTRE', 'APPOINTMENT', 13, 3, 'Thyroid follow-up', 'Hypothyroidism', NULL, false, NULL, 'NONE', 'LOW'),
(25, 22, '2026-10-07', 'CAMP', 'CAMP', NULL, NULL, 'Sugar screening', 'Diabetes', NULL, false, NULL, 'NONE', 'MEDIUM'),
(26, 24, '2026-10-08', 'CAMP', 'CAMP', NULL, NULL, 'Immunisation', 'Up to date', NULL, false, NULL, 'NONE', 'LOW')
ON CONFLICT (id) DO NOTHING;

-- Move each ID counter past the inserted rows, so new records don't clash
SELECT setval(pg_get_serial_sequence('resident', 'id'), (SELECT MAX(id) FROM resident));
SELECT setval(pg_get_serial_sequence('medical_info', 'id'), (SELECT MAX(id) FROM medical_info));
SELECT setval(pg_get_serial_sequence('visit_record', 'id'), (SELECT MAX(id) FROM visit_record));