SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `public_utility_billing_system`;
USE `public_utility_billing_system`;

-- ================================
-- UtilityType
-- ================================
CREATE TABLE UtilityType (
    UtilityTypeID INT PRIMARY KEY,
    UtilityTypeName VARCHAR(100) NOT NULL,
    Description VARCHAR(255),
    UnitOfMeasure VARCHAR(50) NOT NULL,
    CreatedDate DATE NOT NULL,
    ModifiedDate DATE,
    IsActive BOOLEAN NOT NULL DEFAULT TRUE
);

-- ================================
-- Rate
-- ================================
CREATE TABLE Rate (
    RateID INT PRIMARY KEY,
    UtilityTypeID INT NOT NULL,
    RatePerUnit DECIMAL(10,2) NOT NULL,
    EffectiveDate DATE NOT NULL,
    FOREIGN KEY (UtilityTypeID) REFERENCES UtilityType(UtilityTypeID)
);

-- ================================
-- Department
-- ================================
CREATE TABLE Department (
    DepartmentID INT PRIMARY KEY,
    DepartmentName VARCHAR(100) NOT NULL,
    Description VARCHAR(255)
);

-- ================================
-- Employee
-- ================================
CREATE TABLE Employee (
    EmployeeID INT PRIMARY KEY,
    DepartmentID INT NOT NULL,
    FirstName VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    Street VARCHAR(255),
    City VARCHAR(100),
    Province VARCHAR(100),
    ZipCode VARCHAR(20),
    ContactNumber VARCHAR(30),
    Position VARCHAR(100),
    HireDate DATE NOT NULL,
    LastLoginDate DATE,
    FOREIGN KEY (DepartmentID) REFERENCES Department(DepartmentID)
);

-- ================================
-- Staff (User Accounts)
-- ================================
CREATE TABLE Staff (
    StaffID INT PRIMARY KEY,
    EmployeeID INT NOT NULL UNIQUE,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Role VARCHAR(50) NOT NULL,
    AssignedBranch VARCHAR(100),
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)
);

-- ================================
-- Customer
-- ================================
CREATE TABLE Customer (
    CustomerID INT PRIMARY KEY,
    AccountNumber VARCHAR(50) NOT NULL UNIQUE,
    FirstName VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    Street VARCHAR(255),
    City VARCHAR(100),
    Province VARCHAR(100),
    ZipCode VARCHAR(20),
    ContactNumber VARCHAR(30),
    CreatedDate DATE NOT NULL,
    BillingStatus VARCHAR(20) NOT NULL
);

-- ================================
-- Meter
-- ================================
CREATE TABLE Meter (
	MeterID INT PRIMARY KEY,
	UtilityTypeID INT,
	MeterSerialNumber VARCHAR(100) NOT NULL UNIQUE,
	MeterStatus VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
	FOREIGN KEY (UtilityTypeID) REFERENCES UtilityType(UtilityTypeID)
);

-- ================================
-- MeterAssignment
-- ================================
CREATE TABLE MeterAssignment (
    AssignmentID INT PRIMARY KEY,
    CustomerID INT NOT NULL,
    MeterID INT NOT NULL,
    AssignmentDate DATE NOT NULL,
    InstallationDate DATE,
    AssignedByStaffID INT NOT NULL,
    Status VARCHAR(20) NOT NULL,
    LastUpdated DATE,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (AssignedByStaffID) REFERENCES Staff(StaffID),
    -- COPILOT: Added missing foreign key to the Meter table
    FOREIGN KEY (MeterID) REFERENCES Meter(MeterID)
);

-- ================================
-- Consumption
-- ================================
CREATE TABLE Consumption (
    ConsumptionID INT PRIMARY KEY,
    ReadingDate DATE NOT NULL,
    ConsumptionValue DECIMAL(10,2) NOT NULL,
    MeterID INT NOT NULL,
    FOREIGN KEY (MeterID) REFERENCES Meter(MeterID)
);

-- ================================
-- Bill
-- ================================
CREATE TABLE Bill (
    BillID INT PRIMARY KEY,
    CustomerID INT NOT NULL,
    ConsumptionID INT NOT NULL,
    RateID INT NOT NULL,
    AmountDue DECIMAL(10,2) NOT NULL,
    DueDate DATE NOT NULL,
    Status VARCHAR(20) NOT NULL,
    GeneratedByStaffID INT NOT NULL,
    TechnicianID INT,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (ConsumptionID) REFERENCES Consumption(ConsumptionID),
    FOREIGN KEY (RateID) REFERENCES Rate(RateID),
    FOREIGN KEY (GeneratedByStaffID) REFERENCES Staff(StaffID),
    FOREIGN KEY (TechnicianID) REFERENCES Staff(StaffID)
);

-- ================================
-- Payment
-- ================================
CREATE TABLE Payment (
    PaymentID INT PRIMARY KEY,
    BillID INT NOT NULL,
    PaymentDate DATE NOT NULL,
    AmountPaid DECIMAL(10,2) NOT NULL,
    PaymentMethod VARCHAR(50) NOT NULL,
    ReceiptNumber VARCHAR(100) UNIQUE,
    ProcessedByStaffID INT NOT NULL,
    CollectorID INT,
    Status VARCHAR(20) NOT NULL,
    FOREIGN KEY (BillID) REFERENCES Bill(BillID),
    FOREIGN KEY (ProcessedByStaffID) REFERENCES Staff(StaffID),
    FOREIGN KEY (CollectorID) REFERENCES Staff(StaffID)
);

-- ================================
-- OverdueNotice
-- ================================
CREATE TABLE OverdueNotice (
    NoticeID INT PRIMARY KEY,
    BillID INT NOT NULL,
    OverdueDate DATE NOT NULL,
    PenaltyAmount DECIMAL(10,2) NOT NULL,
    NoticeDate DATE NOT NULL,
    EscalationStatus VARCHAR(20) NOT NULL,
    SentByStaffID INT NOT NULL,
    FOREIGN KEY (BillID) REFERENCES Bill(BillID),
    FOREIGN KEY (SentByStaffID) REFERENCES Staff(StaffID)
);

SET FOREIGN_KEY_CHECKS = 1;