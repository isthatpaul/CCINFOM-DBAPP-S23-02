package Services;

import Model.MeterAssignment;
import Model.MeterAssignmentCRUD;
import java.util.List;

public class MeterAssignmentService {

    private final MeterAssignmentCRUD assignmentCRUD = new MeterAssignmentCRUD();

    public boolean assignMeter(MeterAssignment assignment) {
        // Rule: Meter must exist
        // Rule: Customer must exist
        // You can add more validations here
        return assignmentCRUD.addRecord(assignment);
    }

    public boolean updateAssignment(MeterAssignment assignment) {
        return assignmentCRUD.updateRecord(assignment);
    }

    public boolean deleteAssignment(int assignmentID) {
        return assignmentCRUD.deleteRecord(assignmentID);
    }

    public List<MeterAssignment> getAllAssignments() {
        return assignmentCRUD.getAllRecords();
    }

    public MeterAssignment getAssignmentById(int assignmentID) {
        return assignmentCRUD.getRecordById(assignmentID);
    }
}
