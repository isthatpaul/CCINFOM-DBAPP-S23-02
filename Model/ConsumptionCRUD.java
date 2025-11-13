package Model;

import Database.DatabaseConnection;
import Model.Consumption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsumptionCRUD
{

    // CREATE
    public boolean addRecord(Consumption cons) {
        String sql = "INSERT INTO CONSUMPTION (METER_ID, CONSUMPTION_VALUE, READING_DATE, BILLING_PERIOD) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, cons.getMeterId());
            ps.setDouble(2, cons.getConsumptionValue());
            ps.setDate(3, cons.getReadingDate());
            ps.setDate(4, cons.getBillingPeriod());
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
                        rs.getDate("READING_DATE"),
                        rs.getDate("BILLING_PERIOD")
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
        String sql = "SELECT * FROM CONSUMPTION WHERE CONSUMPTION_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, consumptionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Consumption(
                            rs.getInt("CONSUMPTION_ID"),
                            rs.getInt("METER_ID"),
                            rs.getDouble("CONSUMPTION_VALUE"),
                            rs.getDate("READING_DATE"),
                            rs.getDate("BILLING_PERIOD")
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
        String sql = "UPDATE CONSUMPTION SET METER_ID = ?, CONSUMPTION_VALUE = ?, READING_DATE = ?, BILLING_PERIOD = ? WHERE CONSUMPTION_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cons.getMeterId());
            ps.setDouble(2, cons.getConsumptionValue());
            ps.setDate(3, cons.getReadingDate());
            ps.setDate(4, cons.getBillingPeriod());
            ps.setInt(5, cons.getConsumptionId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Consumption updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int consumptionId) {
        String sql = "DELETE FROM CONSUMPTION WHERE CONSUMPTION_ID = ?";
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