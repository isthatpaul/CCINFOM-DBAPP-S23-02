package Reports;

public record OverdueReport(
        int customerID,
        String customerName,
        double totalOverdue
) {}
