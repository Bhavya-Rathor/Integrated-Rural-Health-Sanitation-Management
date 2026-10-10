INSERT INTO health_centre (id, name, type, village, district, phone, opening_time, closing_time, open24x7, status) VALUES
(1, 'AAM Katpadi',     'SHC_AAM', 'Katpadi',     'Vellore', '9876500001', '09:00', '16:00', false, 'ACTIVE'),
(2, 'PHC Gudiyatham',  'PHC',     'Gudiyatham',  'Vellore', '9876500002', '08:00', '20:00', false, 'ACTIVE'),
(3, 'AAM Anaicut',     'SHC_AAM', 'Anaicut',     'Vellore', '9876500003', '09:00', '16:00', false, 'ACTIVE'),
(4, 'CHC Pernambut',   'CHC',     'Pernambut',   'Vellore', '9876500004', '00:00', '23:59', true,  'ACTIVE'),
(5, 'AAM Kaniyambadi', 'SHC_AAM', 'Kaniyambadi', 'Vellore', '9876500005', '09:00', '16:00', false, 'UNDER_REPAIR')
ON CONFLICT (id) DO NOTHING;

INSERT INTO facility (id, name, status, priority, remarks, lastupdated, centre_id) VALUES
(1, 'Teleconsult kit',   'AVAILABLE',    'LOW',    NULL,                     '2026-10-01', 1),
(2, 'Cold chain fridge', 'UNAVAILABLE',  'HIGH',   'Fridge not cooling',     '2026-10-09', 1),
(3, 'Laboratory',        'AVAILABLE',    'LOW',    NULL,                     '2026-10-02', 2),
(4, 'Delivery room',     'AVAILABLE',    'LOW',    NULL,                     '2026-10-02', 2),
(5, 'Ambulance',         'UNDER_REPAIR', 'HIGH',   'Engine fault',           '2026-10-08', 4),
(6, 'Laboratory',        'AVAILABLE',    'LOW',    NULL,                     '2026-10-03', 4),
(7, 'Water purifier',    'UNAVAILABLE',  'MEDIUM', 'Filter needs replacing', '2026-10-10', 5)
ON CONFLICT (id) DO NOTHING;

INSERT INTO health_worker (id, name, role, qualification, phone, active, centre_id) VALUES
(1, 'Dr. Priya Raman',  'MEDICAL_OFFICER', 'MBBS',         '9840000001', true,  2),
(2, 'Karthik S',        'CHO',             'B.Sc Nursing', '9840000002', true,  1),
(3, 'Lakshmi Devi',     'ANM',             'ANM Diploma',  '9840000003', true,  1),
(4, 'Ravi Kumar',       'CHO',             'B.Sc Nursing', '9840000004', true,  3),
(5, 'Dr. Arun Prakash', 'MEDICAL_OFFICER', 'MBBS, MD',     '9840000005', true,  4),
(6, 'Meena K',          'PHARMACIST',      'D.Pharm',      '9840000006', true,  2),
(7, 'Suresh M',         'MPW',             'MPW Training', '9840000007', true,  5),
(8, 'Anitha R',         'LAB_TECH',        'DMLT',         '9840000008', false, 4)
ON CONFLICT (id) DO NOTHING;

INSERT INTO centre_schedule (id, day_of_week, start_time, end_time, service, centre_id, worker_id) VALUES
(1, 'MONDAY',    '09:00', '13:00', 'General OPD',  2, 1),
(2, 'WEDNESDAY', '09:00', '13:00', 'General OPD',  2, 1),
(3, 'MONDAY',    '09:00', '16:00', 'NCD clinic',   1, 2),
(4, 'TUESDAY',   '09:00', '13:00', 'ANC clinic',   1, 3),
(5, 'THURSDAY',  '09:00', '13:00', 'Immunisation', 1, 3),
(6, 'MONDAY',    '09:00', '16:00', 'General OPD',  3, 4),
(7, 'SATURDAY',  '08:00', '14:00', 'General OPD',  4, 5),
(8, 'SUNDAY',    '08:00', '14:00', 'General OPD',  4, 5)
ON CONFLICT (id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('health_centre', 'id'),   (SELECT MAX(id) FROM health_centre));
SELECT setval(pg_get_serial_sequence('facility', 'id'),        (SELECT MAX(id) FROM facility));
SELECT setval(pg_get_serial_sequence('health_worker', 'id'),   (SELECT MAX(id) FROM health_worker));
SELECT setval(pg_get_serial_sequence('centre_schedule', 'id'), (SELECT MAX(id) FROM centre_schedule));