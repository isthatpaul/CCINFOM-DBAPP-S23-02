-- ================================
-- UtilityType
-- ================================
INSERT INTO UtilityType (UtilityTypeID, UtilityTypeName, Description, UnitOfMeasure, CreatedDate, ModifiedDate, IsActive) VALUES
(101, 'Electricity', 'Electric power supply by Meralco', 'kWh', '2024-01-01', '2024-01-01', 1),
(102, 'Water', 'Potable water supply by Maynilad', 'Cubic Meter', '2024-01-01', '2024-01-01', 1);

-- ================================
-- Customer
-- ================================
INSERT INTO Customer (CustomerID, AccountNumber, FirstName, LastName, Street, City, Province, ZipCode, ContactNumber, CreatedDate, BillingStatus) VALUES
(1101, 'ACC001', 'John', 'Smith', '123 Main St', 'San Fernando', 'Pampanga', '2000', '09171234567', '2024-01-15', 'Active'),
(1102, 'ACC002', 'Maria', 'Garcia', '456 Oak Ave', 'Angeles City', 'Pampanga', '2009', '09181234568', '2024-01-16', 'Active'),
(1103, 'ACC003', 'Robert', 'Johnson', '789 Pine Rd', 'Mabalacat', 'Pampanga', '2010', '09191234569', '2024-01-17', 'Active'),
(1104, 'ACC004', 'Lisa', 'Williams', '321 Elm St', 'San Fernando', 'Pampanga', '2000', '09201234570', '2024-01-18', 'Suspended'), 
(1105, 'ACC005', 'David', 'Martinez', '654 Maple Dr', 'Angeles City', 'Pampanga', '2009', '09211234571', '2024-01-19', 'Active'),
(1106, 'ACC006', 'Sarah', 'Anderson', '987 Cedar Ln', 'Mabalacat', 'Pampanga', '2010', '09221234572', '2024-01-20', 'Active'),
(1107, 'ACC007', 'Michael', 'Taylor', '147 Birch Ct', 'San Fernando', 'Pampanga', '2000', '09231234573', '2024-01-21', 'Active'),
(1108, 'ACC008', 'Jennifer', 'Thomas', '258 Walnut Way', 'Angeles City', 'Pampanga', '2009', '09241234574', '2024-01-22', 'Active'),
(1109, 'ACC009', 'James', 'Moore', '369 Spruce Pl', 'Mabalacat', 'Pampanga', '2010', '09251234575', '2024-01-23', 'Overdue'),
(1110, 'ACC010', 'Patricia', 'Jackson', '741 Ash Blvd', 'San Fernando', 'Pampanga', '2000', '09261234576', '2024-01-24', 'Active'),
(1111, 'ACC011', 'Linda', 'Harris', '852 Willow Way', 'Angeles City', 'Pampanga', '2009', '09271234577', '2024-03-01', 'Active'),
(1112, 'ACC012', 'William', 'Clark', '963 Redwood Dr', 'Mabalacat', 'Pampanga', '2010', '09281234578', '2024-03-02', 'Active');


-- ================================
-- Department
-- ================================
INSERT INTO Department (DepartmentID, DepartmentName, Description) VALUES
(501, 'Billing', 'Handles customer billing and invoicing'),
(502, 'Collections', 'Manages payment collection and processing'),
(503, 'Meter Reading', 'Conducts meter readings and maintenance'),
(504, 'Customer Service', 'Provides customer support and inquiries'),
(505, 'Technical Support', 'Handles technical issues and repairs'),
(506, 'Administration', 'General administrative functions'),
(507, 'Finance', 'Financial management and accounting'),
(508, 'IT Department', 'Information technology support'),
(509, 'Field Operations', 'Oversees field technicians and operations'),
(510, 'Management', 'Executive management and oversight');

-- ================================
-- Employee
-- ================================
INSERT INTO Employee (EmployeeID, DepartmentID, FirstName, LastName, Street, City, Province, ZipCode, ContactNumber, Position, HireDate, LastLoginDate) VALUES
(2101, 501, 'Carlos', 'Reyes', '100 Employee St', 'San Fernando', 'Pampanga', '2000', '09171111111', 'Billing Clerk', '2023-01-10', '2025-11-18'),
(2102, 502, 'Anna', 'Santos', '101 Employee St', 'Angeles City', 'Pampanga', '2009', '09171111112', 'Collection Officer', '2023-02-15', '2025-11-18'),
(2103, 503, 'Pedro', 'Cruz', '102 Employee St', 'Mabalacat', 'Pampanga', '2010', '09171111113', 'Meter Reader', '2023-03-20', '2025-11-18'),
(2104, 504, 'Rosa', 'Lopez', '103 Employee St', 'San Fernando', 'Pampanga', '2000', '09171111114', 'Customer Service Rep', '2023-04-25', '2025-11-19'),
(2105, 505, 'Juan', 'Fernandez', '104 Employee St', 'Angeles City', 'Pampanga', '2009', '09171111115', 'Technician', '2023-05-30', '2025-11-17'),
(2106, 506, 'Carmen', 'Rivera', '105 Employee St', 'Mabalacat', 'Pampanga', '2010', '09171111116', 'Admin Assistant', '2023-06-05', '2025-11-19'),
(2107, 510, 'Elena', 'Ramirez', '109 Employee St', 'San Fernando', 'Pampanga', '2000', '09171111120', 'Operations Manager', '2023-10-25', '2025-11-19'),
(2108, 509, 'Joshua', 'Samonte', '110 Coder Ave', 'Angeles City', 'Pampanga', '2009', '09172222222', 'Field Supervisor', '2023-11-01', '2025-11-19'),
(2109, 508, 'Kamillu', 'Luis', '111 Java St', 'Mabalacat', 'Pampanga', '2010', '09173333333', 'IT Specialist', '2023-11-05', '2025-11-19'),
(2110, 508, 'Paul', 'Crisologo', '112 Admin Rd', 'San Fernando', 'Pampanga', '2000', '09174444444', 'System Administrator', '2023-11-10', '2025-11-19');

-- ================================
-- Staff (User Accounts)
-- Now has 10 records. Added 'isthatpaul' as an Admin. Password for all is 'password123'.
-- ================================
INSERT INTO Staff (StaffID, EmployeeID, Username, Password, Role, AssignedBranch) VALUES
(101, 2101, 'creyes', 'Reyes2312', 'Billing', 'San Fernando Branch'),
(102, 2102, 'asantos', 'a0213Santos', 'Collector', 'Angeles Branch'),
(103, 2103, 'pcruz', 'paolo726CruZZzz', 'Technician', 'Mabalacat Branch'),
(104, 2104, 'rlopez', 'lopez01922', 'Customer Service', 'San Fernando Branch'),
(105, 2105, 'jfernandez', 'Fem1972', 'Technician', 'Angeles Branch'),
(106, 2106, 'crivera', 'password123', 'Admin', 'Mabalacat Branch'),
(107, 2107, 'eramirez', 'xyza6262', 'Manager', 'Main Office'),
(108, 2108, 'jsamonte', 'gio17samonte', 'Supervisor', 'Field Office'),
(109, 2109, 'kluis', 'Kam67luis', 'IT Admin', 'Main Office'),
(110, 2110, 'isthatpaul', 'Paul13Gwapo', 'Admin', 'Main Office');

-- ================================
-- Rate
-- ================================
INSERT INTO Rate (RateID, UtilityTypeID, RatePerUnit, EffectiveDate) VALUES
(1001, 101, 10.50, '2023-01-01'),  -- Electricity (Old)
(1002, 102, 22.00, '2023-01-01'),  -- Water (Old)
(1003, 101, 11.50, '2024-01-01'),  -- Electricity (Previous Year)
(1004, 102, 24.00, '2024-01-01'),  -- Water (Previous Year)
(1005, 101, 12.75, '2025-01-01'),  -- Electricity (Current Year)
(1006, 102, 25.50, '2025-01-01'),  -- Water (Current Year)
(1007, 101, 13.50, '2026-01-01'),  -- Electricity (Future)
(1008, 102, 27.00, '2026-01-01'),  -- Water (Future)
(1009, 101, 12.00, '2024-06-01'),  -- Mid-year adjustment elec
(1010, 102, 25.00, '2024-06-01');  -- Mid-year adjustment water

-- ================================
-- Meter
-- ================================
INSERT INTO Meter (MeterID, UtilityTypeID, MeterSerialNumber, MeterStatus) VALUES
(3001, 101, 'MTR-E-001', 'Active'),
(3002, 102, 'MTR-W-001', 'Active'),
(3003, 101, 'MTR-E-002', 'Active'),
(3004, 102, 'MTR-W-002', 'Active'),
(3005, 101, 'MTR-E-003', 'Active'),
(3006, 102, 'MTR-W-003', 'Active'),
(3007, 101, 'MTR-E-004', 'Maintenance'),
(3008, 102, 'MTR-W-004', 'Active'),
(3009, 101, 'MTR-E-005', 'Active'),
(3010, 102, 'MTR-W-005', 'Active'),
(3011, 101, 'MTR-E-006', 'Active'),
(3012, 102, 'MTR-W-006', 'Active');

-- ================================
-- MeterAssignment
-- ================================
INSERT INTO MeterAssignment (AssignmentID, CustomerID, MeterID, AssignmentDate, InstallationDate, AssignedByStaffID, Status, LastUpdated) VALUES
(4001, 1101, 3001, '2024-01-15', '2024-01-15', 101, 'Active', '2024-01-15'),
(4002, 1101, 3002, '2024-01-15', '2024-01-15', 101, 'Active', '2024-01-15'),
(4003, 1102, 3003, '2024-01-16', '2024-01-16', 101, 'Active', '2024-01-16'),
(4004, 1102, 3004, '2024-01-16', '2024-01-16', 101, 'Active', '2024-01-16'),
(4005, 1103, 3005, '2024-01-17', '2024-01-17', 103, 'Active', '2024-01-17'),
(4006, 1104, 3006, '2024-01-18', '2024-01-18', 103, 'Active', '2024-01-18'),
(4007, 1105, 3007, '2024-01-19', '2024-01-19', 103, 'Inactive', '2024-02-01'),
(4008, 1106, 3008, '2024-01-20', '2024-01-20', 103, 'Active', '2024-01-20'),
(4009, 1107, 3009, '2024-01-21', '2024-01-21', 103, 'Active', '2024-01-21'),
(4010, 1108, 3010, '2024-01-22', '2024-01-22', 103, 'Active', '2024-01-22'),
(4011, 1111, 3011, '2024-03-03', '2024-03-03', 103, 'Active', '2024-03-03'),
(4012, 1112, 3012, '2024-03-04', '2024-03-04', 103, 'Active', '2024-03-04');


-- ================================
-- Consumption
-- ================================
INSERT INTO Consumption (ConsumptionID, MeterID, ReadingDate, ConsumptionValue) VALUES
(5001, 3001, '2025-10-31', 250.50),  -- Customer 1101 - Electricity
(5002, 3002, '2025-10-31', 15.75),   -- Customer 1101 - Water
(5003, 3003, '2025-10-31', 310.25),  -- Customer 1102 - Electricity
(5004, 3004, '2025-10-31', 22.50),   -- Customer 1102 - Water
(5005, 3005, '2025-10-31', 450.00),  -- Customer 1103 - Electricity
(5006, 3006, '2025-10-31', 50.00),   -- Customer 1104 - Water
(5007, 3008, '2025-10-31', 28.30),   -- Customer 1106 - Water
(5008, 3009, '2025-10-31', 520.00),  -- Customer 1107 - Electricity
(5009, 3010, '2025-10-31', 35.60),   -- Customer 1108 - Water
(5010, 3005, '2025-09-30', 420.00),  -- Customer 1103 - Electricity (Previous Month, Overdue)
(5011, 3011, '2025-10-31', 150.00),  -- Customer 1111 - Electricity
(5012, 3012, '2025-10-31', 45.00);   -- Customer 1112 - Water


-- ================================
-- Bill
-- ================================
INSERT INTO Bill (BillID, CustomerID, ConsumptionID, RateID, AmountDue, DueDate, Status, GeneratedByStaffID, TechnicianID) VALUES
(6001, 1101, 5001, 1005, 3193.88, '2025-11-20', 'Unpaid', 101, 103),           -- 250.50 * 12.75
(6002, 1101, 5002, 1006, 401.63,  '2025-11-20', 'Paid', 101, 103),             -- 15.75 * 25.50
(6003, 1102, 5003, 1005, 3955.69, '2025-11-20', 'Partially Paid', 101, 103),    -- 310.25 * 12.75
(6004, 1102, 5004, 1006, 573.75,  '2025-11-20', 'Paid', 101, 103),             -- 22.50 * 25.50
(6005, 1103, 5005, 1005, 5737.50, '2025-11-20', 'Unpaid', 101, 103),           -- 450.00 * 12.75
(6006, 1104, 5006, 1006, 1275.00, '2025-11-20', 'Unpaid', 101, 105),           -- 50.00 * 25.50
(6007, 1106, 5007, 1006, 721.65,  '2025-11-20', 'Paid', 101, 103),             -- 28.30 * 25.50
(6008, 1107, 5008, 1005, 6630.00, '2025-11-20', 'Unpaid', 101, 105),           -- 520.00 * 12.75
(6009, 1108, 5009, 1006, 907.80,  '2025-11-20', 'Partially Paid', 101, 103),    -- 35.60 * 25.50
(6010, 1103, 5010, 1005, 5355.00, '2025-10-20', 'Overdue', 101, 103),           -- 420.00 * 12.75 (Previous Month's Bill)
(6011, 1111, 5011, 1005, 1912.50, '2025-11-25', 'Unpaid', 101, 103),           -- 150.00 * 12.75
(6012, 1112, 5012, 1006, 1147.50, '2025-11-25', 'Paid', 101, 103);             -- 45.00 * 25.50

-- ================================
-- Payment
-- ================================
INSERT INTO Payment (PaymentID, BillID, PaymentDate, AmountPaid, PaymentMethod, ReceiptNumber, ProcessedByStaffID, CollectorID, Status) VALUES
(7001, 6002, '2025-11-10', 401.63, 'GCash', 'RCT-001', 102, 102, 'Completed'),
(7002, 6003, '2025-11-11', 2000.00, 'Bank Transfer', 'RCT-002', 102, 102, 'Completed'),
(7003, 6004, '2025-11-12', 573.75, 'Cash', 'RCT-003', 102, 102, 'Completed'),
(7004, 6007, '2025-11-14', 721.65, 'Credit Card', 'RCT-004', 102, 102, 'Completed'),
(7005, 6009, '2025-11-15', 500.00, 'Cash', 'RCT-005', 102, 102, 'Completed'),
(7006, 6010, '2025-10-25', 3000.00, 'Bank Transfer', 'RCT-006', 102, 102, 'Completed'), -- Payment for overdue bill
(7007, 6012, '2025-11-18', 1147.50, 'Check', 'RCT-007', 104, 102, 'Completed'),
(7008, 6001, '2025-11-19', 1000.00, 'GCash', 'RCT-008', 104, 102, 'Pending'), -- Made today, but still unpaid
(7009, 6003, '2025-11-19', 1000.00, 'Cash', 'RCT-009', 102, 102, 'Completed'), -- Second payment on this bill
(7010, 6010, '2025-11-05', 2355.00, 'Cash', 'RCT-010', 102, 102, 'Completed'), -- Final payment for overdue bill
(7011, 6008, '2025-11-18', 6000.00, 'Bank Transfer', 'RCT-011', 102, 102, 'Completed');

-- ================================
-- OverdueNotice
-- ================================
INSERT INTO OverdueNotice (NoticeID, BillID, SentByStaffID, OverdueDate, PenaltyAmount, NoticeDate, EscalationStatus) VALUES
(8001, 6010, 101, '2025-10-21', 267.75, '2025-10-22', 'First Notice'),     
(8002, 6010, 101, '2025-11-01', 535.50, '2025-11-02', 'Second Notice'),    
(8003, 6001, 101, '2025-09-21', 150.00, '2025-09-22', 'First Notice'),
(8004, 6002, 101, '2025-09-21', 20.00, '2025-09-22', 'First Notice'),
(8005, 6003, 101, '2025-09-21', 195.00, '2025-09-22', 'First Notice'),
(8006, 6004, 101, '2025-08-21', 25.00, '2025-08-22', 'First Notice'),
(8007, 6005, 101, '2025-08-21', 280.00, '2025-08-22', 'First Notice'),
(8008, 6007, 101, '2025-07-21', 35.00, '2025-07-22', 'First Notice'),
(8009, 6008, 101, '2025-07-21', 320.00, '2025-07-22', 'First Notice'),
(8010, 6009, 101, '2025-06-21', 45.00, '2025-06-22', 'First Notice');
