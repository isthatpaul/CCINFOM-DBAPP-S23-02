package Model;

public record Staff(
        int staffID, // PK
        int employeeID, // FK to Employee (and UNIQUE)
        String username, // UNIQUE
        String passwordHash, // Store as Hash
        String role, // e.g., 'ADMIN', 'BILLING', 'TECHNICIAN'
        String assignedBranch
) {}