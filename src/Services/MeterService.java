package Services;

import Model.Meter;
import Model.MeterCRUD;
import java.util.List;

public class MeterService {

    private final MeterCRUD meterCRUD = new MeterCRUD();

    public boolean addMeter(Meter meter) {
        // Rule: Unique Serial Number
        List<Meter> all = meterCRUD.getAllRecords();
        for (Meter m : all) {
            if (m.meterSerialNumber().equals(meter.meterSerialNumber())) {
                System.err.println("Meter serial number already exists!");
                return false;
            }
        }

        return meterCRUD.addRecord(meter);
    }

    public boolean updateMeter(Meter meter) {
        return meterCRUD.updateRecord(meter);
    }

    public boolean deleteMeter(int meterID) {
        // Rule: Cannot delete if assigned to customer
        // This would need MeterAssignmentService check
        return meterCRUD.deleteRecord(meterID);
    }

    public List<Meter> getAllMeters() {
        return meterCRUD.getAllRecords();
    }

    public Meter getMeterById(int meterID) {
        return meterCRUD.getRecordById(meterID);
    }
}
