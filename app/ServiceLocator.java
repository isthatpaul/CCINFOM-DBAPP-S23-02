package app;

import Services.BillService;
import Services.CustomerService;
import Services.MeterService;
import Services.MeterAssignmentService;

public class ServiceLocator {
    private static final CustomerService customerService = new CustomerService();
    private static final BillService billService = new BillService();
    private static final MeterService meterService = new MeterService();
    private static final MeterAssignmentService meterAssignmentService = new MeterAssignmentService();

    public static CustomerService customers() { return customerService; }
    public static BillService bills() { return billService; }
    public static MeterService meters() { return meterService; }
    public static MeterAssignmentService assignments() { return meterAssignmentService; }
}