package Services;

import Model.Bill;
import Model.BillCRUD;
import Model.Consumption;
import Model.Rate;
import Model.OverdueNoticeCRUD;

import java.sql.Date;
import java.util.Calendar;

public class BillService {

    private final BillCRUD billCRUD = new BillCRUD();
    private final OverdueNoticeCRUD overdueNoticeCRUD = new OverdueNoticeCRUD();

    public boolean generateBill(Consumption consumption, Rate rate, int customerID, int staffID, Integer technicianID, int dueDays) {

        double amountDue = consumption.consumptionValue() * rate.ratePerUnit();

        Calendar cal = Calendar.getInstance();
        cal.setTime(consumption.readingDate());
        cal.add(Calendar.DAY_OF_MONTH, dueDays);
        Date dueDate = new Date(cal.getTimeInMillis());

        // FIX: convert null technician to 0 (since Bill.technicianID is int)
        int techId = (technicianID == null) ? 0 : technicianID;

        Bill bill = new Bill(
                0,
                customerID,
                consumption.consumptionID(),
                rate.rateID(),
                amountDue,
                dueDate,
                "UNPAID",
                staffID,
                techId
        );

        return billCRUD.addRecord(bill);
    }

    public boolean generateBill(Consumption consumption, Rate rate, int customerID, int staffID) {
        return generateBill(consumption, rate, customerID, staffID, null, 15);
    }

    // applyPenalty/updateBillAfterPayment (unchanged) ...
}