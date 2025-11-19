package Model;

import Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeterCRUD
{
    // CREATE
    public boolean addRecord(Meter meter) {
        String sql = "INSERT INTO Meter (UtilityTypeID, MeterSerialNumber, MeterStatus) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (meter.utilityTypeID() == 0) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, meter.utilityTypeID());
            }
            ps.setString(2, meter.meterSerialNumber());
            ps.setString(3, meter.meterStatus());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Meter addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<Meter> getAllRecords() {
        List<Meter> list = new ArrayList<>();
        String sql = "SELECT * FROM Meter";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Meter m = new Meter(
                        rs.getInt("MeterID"),
                        rs.getInt("UtilityTypeID"),
                        rs.getString("MeterSerialNumber"),
                        rs.getString("MeterStatus")
                );
                list.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Meter getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE
    public Meter getRecordById(int meterId) {
        String sql = "SELECT * FROM Meter WHERE MeterID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, meterId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Meter(
                            rs.getInt("MeterID"),
                            rs.getInt("UtilityTypeID"),
                            rs.getString("MeterSerialNumber"),
                            rs.getString("MeterStatus")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Meter getRecordById error: " + e.getMessage());
        }
        return null;
    }

    public List<Meter> getMetersByCustomerId(int customerId) {
        List<Meter> meters = new ArrayList<>();
        String sql = "SELECT m.* FROM Meter m JOIN MeterAssignment ma ON m.MeterID = ma.MeterID WHERE ma.CustomerID = ? AND ma.Status = 'Active'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    meters.add(new Meter(
                        rs.getInt("MeterID"),
                        rs.getInt("UtilityTypeID"),
                        rs.getString("MeterSerialNumber"),
                        rs.getString("MeterStatus")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meters;
    }

    // UPDATE
    public boolean updateRecord(Meter meter) {
        String sql = "UPDATE Meter SET UtilityTypeID = ?, MeterSerialNumber = ?, MeterStatus = ? WHERE MeterID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (meter.utilityTypeID() == 0) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, meter.utilityTypeID());
            }
            ps.setString(2, meter.meterSerialNumber());
            ps.setString(3, meter.meterStatus());
            ps.setInt(4, meter.meterID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Meter updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int meterId) {
        String sql = "DELETE FROM Meter WHERE MeterID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, meterId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Meter deleteRecord error: " + e.getMessage());
            return false;
        }
    }

    /**
     * FIX: This method was missing. It is used by the MeterAssignmentService to
     * automatically update a meter's status when it's assigned or unassigned.
     */
    public boolean updateMeterStatus(int meterId, String status) {
        String sql = "UPDATE Meter SET MeterStatus = ? WHERE MeterID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, meterId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * FIX: This method was missing. It is used by the MeterAssignmentFormDialog
     * to show only meters that are available to be assigned.
     */
    public List<Meter> getUnassignedMeters() {
        List<Meter> meters = new ArrayList<>();
        // In most schemas, 'Available' is the status for unassigned meters.
        String sql = "SELECT * FROM Meter WHERE MeterStatus = 'AVAILABLE'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                meters.add(new Meter(
                    rs.getInt("MeterID"),
                    rs.getInt("UtilityTypeID"),
                    rs.getString("MeterSerialNumber"),
                    rs.getString("MeterStatus")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meters;
    }
}