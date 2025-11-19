package Services;

import Model.MeterAssignment;
import Model.MeterAssignmentCRUD;
import Model.MeterCRUD;

public class MeterAssignmentService {

    private final MeterAssignmentCRUD assignmentCRUD = new MeterAssignmentCRUD();
    private final MeterCRUD meterCRUD = new MeterCRUD();

    /**
     * Creates a new meter assignment and transactionally updates the meter's status to 'ASSIGNED'.
     * This enforces the business rule that an assigned meter cannot be available.
     * @param assignment The new assignment to create.
     * @return true if both the assignment is created and the meter status is updated, false otherwise.
     */
    public boolean assignMeter(MeterAssignment assignment) {
        // Step 1: Add the new assignment record.
        boolean assignmentAdded = assignmentCRUD.addRecord(assignment);
        
        // If the assignment fails, stop immediately.
        if (!assignmentAdded) {
            return false;
        }

        // Step 2: If the assignment was successful, update the meter's status to "ASSIGNED".
        return meterCRUD.updateMeterStatus(assignment.meterID(), "ASSIGNED");
    }

    /**
     * Updates an existing meter assignment. This typically involves changing dates or status.
     * @param assignment The assignment record with updated information.
     * @return true if the update was successful.
     */
    public boolean updateAssignment(MeterAssignment assignment) {
        return assignmentCRUD.updateRecord(assignment);
    }

    /**
     * Deletes a meter assignment and transactionally sets the meter's status back to 'AVAILABLE'.
     * This enforces the business rule that a newly unassigned meter should be available for others.
     * @param assignmentID The ID of the assignment to delete.
     * @return true if both the assignment is deleted and the meter status is updated, false otherwise.
     */
    public boolean deleteAssignment(int assignmentID) {
        // First, we need to know which meter is associated with this assignment.
        MeterAssignment assignment = assignmentCRUD.getRecordById(assignmentID);
        if (assignment == null) {
            return false; // Cannot proceed if the assignment doesn't exist.
        }

        // Step 1: Delete the assignment record.
        boolean assignmentDeleted = assignmentCRUD.deleteRecord(assignmentID);
        
        // If the deletion fails, stop immediately.
        if (!assignmentDeleted) {
            return false;
        }

        // Step 2: If the deletion was successful, update the meter's status back to "AVAILABLE".
        return meterCRUD.updateMeterStatus(assignment.meterID(), "AVAILABLE");
    }
}