package Model;

import Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeterAssignmentCRUD
{
    // CREATE
    public boolean addRecord(MeterAssignment assign) {
        String sql = "INSERT INTO METER_ASSIGNMENT (CUSTOMER_ID, METER_ID, ASSIGNMENT_DATE, INSTALLATION_DATE, ASSIGNED_BY_USER_ID, STATUS, LAST_UPDATED) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, assign.getCustomerID());
            ps.setInt(2, assign.getMeterID());
            ps.setDate(3, assign.getAssignmentDate());
            ps.setDate(4, assign.getInstallationDate());
            ps.setInt(5, assign.getAssignedByUserID());
            ps.setString(6, assign.getStatus());
            ps.setDate(7, assign.getLastUpdated());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("MeterAssignment addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<MeterAssignment> getAllRecords() {
        List<MeterAssignment> list = new ArrayList<>();
        String sql = "SELECT * FROM METER_ASSIGNMENT";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                MeterAssignment ma = new MeterAssignment(
                        rs.getInt("ASSIGNMENT_ID"),
                        rs.getInt("CUSTOMER_ID"),
                        rs.getInt("METER_ID"),
                        rs.getDate("ASSIGNMENT_DATE"),
                        rs.getDate("Installation_date"),
                        rs.getInt("ASSIGNED_BY_USER_ID"),
                        rs.getString("STATUS"),
                        rs.getDate("last_updated")
                );
                list.add(ma);
            }
        } catch (SQLException e) {
            System.err.println("MeterAssignment getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE (by primary key ASSIGNMENT_ID, assuming it exists)
    public MeterAssignment getRecordById(int assignmentID) {
        String sql = "SELECT * FROM METER_ASSIGNMENT WHERE ASSIGNMENT_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, assignmentID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new MeterAssignment(
                            rs.getInt("ASSIGNMENT_ID"),
                            rs.getInt("CUSTOMER_ID"),
                            rs.getInt("METER_ID"),
                            rs.getDate("ASSIGNMENT_DATE"),
                            rs.getDate("Installation_date"),
                            rs.getInt("ASSIGNED_BY_USER_ID"),
                            rs.getString("STATUS"),
                            rs.getDate("last_updated")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("MeterAssignment getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(MeterAssignment assign) {
        String sql = "UPDATE METER_ASSIGNMENT SET CUSTOMER_ID = ?, METER_ID = ?, ASSIGNMENT_DATE = ?, INSTALLATION_DATE=?, ASSIGNED_BY_USER_ID=?, STATUS = ?, LAST_UPDATED=? WHERE ASSIGNMENT_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assign.getCustomerID());
            ps.setInt(2, assign.getMeterID());
            ps.setDate(3, assign.getAssignmentDate());
            ps.setDate(4, assign.getInstallationDate());
            ps.setInt(5, assign.getAssignedByUserID());
            ps.setString(6, assign.getStatus());
            ps.setDate(7, assign.getLastUpdated());
            ps.setInt(8, assign.getAssignmentID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("MeterAssignment updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int assignmentID) {
        String sql = "DELETE FROM METER_ASSIGNMENT WHERE ASSIGNMENT_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, assignmentID);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("MeterAssignment deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}
