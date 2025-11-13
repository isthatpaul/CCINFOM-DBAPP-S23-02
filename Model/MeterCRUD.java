package Model;

import Database.DatabaseConnection;
import Model.Meter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeterCRUD
{
    // CREATE
    public boolean addRecord(Meter meter) {
        String sql = "INSERT INTO METER (UTILITY_TYPE_ID, METER_SERIAL_NUMBER, METER_STATUS) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, meter.getUtilityTypeId());
            ps.setString(2, meter.getMeterSerialNumber());
            ps.setString(3, meter.getMeterStatus());
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
        String sql = "SELECT * FROM METER";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Meter m = new Meter(
                        rs.getInt("METER_ID"),
                        rs.getInt("UTILITY_TYPE_ID"),
                        rs.getString("METER_SERIAL_NUMBER"),
                        rs.getString("METER_STATUS")
                );
                list.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Meter getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE
    public Meter getRecordById(Long meterId) {
        String sql = "SELECT * FROM METER WHERE METER_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, meterId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Meter(
                            rs.getInt("METER_ID"),
                            rs.getInt("UTILITY_TYPE_ID"),
                            rs.getString("METER_SERIAL_NUMBER"),
                            rs.getString("METER_STATUS")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Meter getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(Meter meter) {
        String sql = "UPDATE METER SET UTILITY_TYPE_ID = ?, METER_SERIAL_NUMBER = ?, METER_STATUS = ? WHERE METER_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, meter.getUtilityTypeId());
            ps.setString(2, meter.getMeterSerialNumber());
            ps.setString(3, meter.getMeterStatus());
            ps.setLong(4, meter.getMeterId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Meter updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(Long meterId) {
        String sql = "DELETE FROM METER WHERE METER_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, meterId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Meter deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}