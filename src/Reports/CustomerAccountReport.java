package Reports;

public record CustomerAccountReport (
        int customerID,
        String customerName,
        String branch,
        String meterSerial,
        String meterStatus
) {}
