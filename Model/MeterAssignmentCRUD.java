package Model;

import Database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeterAssignmentCRUD {

    // CREATE
    public boolean addRecord(MeterAssignment assign) {
        String sql = "INSERT INTO METERASSIGNMENT " +
                "(CUSTOMERID, METERID, ASSIGNMENTDATE, INSTALLATIONDATE, " +
                "ASSIGNEDBYSTAFFID, STATUS, LASTUPDATED) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, assign.customerID());
            ps.setInt(2, assign.meterID());
            ps.setDate(3, assign.assignmentDate());
            ps.setDate(4, assign.installationDate());
            ps.setInt(5, assign.assignedByStaffID());
            ps.setString(6, assign.status());
            ps.setDate(7, assign.lastUpdated());
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
        String sql = "SELECT * FROM METERASSIGNMENT";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                MeterAssignment ma = new MeterAssignment(
                        rs.getInt("ASSIGNMENT_ID"),
                        rs.getInt("CUSTOMER_ID"),
                        rs.getInt("METER_ID"),
                        rs.getDate("ASSIGNMENT_DATE"),
                        rs.getDate("INSTALLATION_DATE"),
                        rs.getInt("ASSIGNED_BY_USER_ID"),
                        rs.getString("STATUS"),
                        rs.getDate("LAST_UPDATED")
                );
                list.add(ma);
            }

        } catch (SQLException e) {
            System.err.println("MeterAssignment getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE (by primary key ASSIGNMENT_ID)
    public MeterAssignment getRecordById(int assignmentID) {
        String sql = "SELECT * FROM METERASSIGNMENT WHERE ASSIGNMENTID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, assignmentID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new MeterAssignment(
                            rs.getInt("ASSIGNMENT_ID"),
                            rs.getInt("CUSTOMER_ID"),
                            rs.getInt("METER_ID"),
                            rs.getDate("ASSIGNMENT_DATE"),
                            rs.getDate("INSTALLATION_DATE"),
                            rs.getInt("ASSIGNED_BY_USER_ID"),
                            rs.getString("STATUS"),
                            rs.getDate("LAST_UPDATED")
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
        String sql = "UPDATE METERASSIGNMENT SET " +
                "CUSTOMERID = ?, METERID = ?, ASSIGNMENTDATE = ?, INSTALLATIONDATE = ?, " +
                "ASSIGNEDBYStaffID = ?, STATUS = ?, LASTUPDATED = ? " +
                "WHERE ASSIGNMENTID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, assign.customerID());
            ps.setInt(2, assign.meterID());
            ps.setDate(3, assign.assignmentDate());
            ps.setDate(4, assign.installationDate());
            ps.setInt(5, assign.assignedByStaffID());
            ps.setString(6, assign.status());
            ps.setDate(7, assign.lastUpdated());
            ps.setInt(8, assign.assignmentID());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("MeterAssignment updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int assignmentID) {
        String sql = "DELETE FROM METERASSIGNMENT WHERE ASSIGNMENTID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, assignmentID);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("MeterAssignment deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}
