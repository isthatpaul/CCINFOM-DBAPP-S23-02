package Model;

public record Department(
        int departmentID, // PK
        String departmentName,
        String description
) {}