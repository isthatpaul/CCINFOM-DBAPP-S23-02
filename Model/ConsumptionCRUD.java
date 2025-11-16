package Model;

import Database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsumptionCRUD
{

    // CREATE
    public boolean addRecord(Consumption cons) {
        String sql = "INSERT INTO CONSUMPTION (METERID, CONSUMPTIONVALUE, READINGDATE) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cons.meterID());
            ps.setDouble(2, cons.consumptionValue());
            ps.setDate(3, cons.readingDate());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Consumption addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<Consumption> getAllRecords()
    {
        List<Consumption> list = new ArrayList<>();
        String sql = "SELECT * FROM CONSUMPTION";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Consumption c = new Consumption(
                        rs.getInt("CONSUMPTION_ID"),
                        rs.getInt("METER_ID"),
                        rs.getDouble("CONSUMPTION_VALUE"),
                        rs.getDate("READING_DATE")
                );
                list.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Consumption getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE
    public Consumption getRecordById(int consumptionId) {
        String sql = "SELECT * FROM CONSUMPTION WHERE CONSUMPTIONID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, consumptionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Consumption(
                            rs.getInt("CONSUMPTION_ID"),
                            rs.getInt("METER_ID"),
                            rs.getDouble("CONSUMPTION_VALUE"),
                            rs.getDate("READING_DATE")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Consumption getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(Consumption cons) {
        String sql = "UPDATE CONSUMPTION SET METERID = ?, CONSUMPTIONVALUE = ?, READINGDATE = ?, BILLINGPERIOD = ? WHERE CONSUMPTIONID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cons.meterID());
            ps.setDouble(2, cons.consumptionValue());
            ps.setDate(3, cons.readingDate());
            ps.setInt(4, cons.consumptionID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Consumption updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int consumptionId) {
        String sql = "DELETE FROM CONSUMPTION WHERE CONSUMPTIONID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, consumptionId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Consumption deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}