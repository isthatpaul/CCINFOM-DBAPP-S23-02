package Model;

import Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsumptionCRUD
{

    // CREATE
    public boolean addRecord(Consumption cons) {
        String sql = "INSERT INTO Consumption (ReadingDate, ConsumptionValue, MeterID) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, cons.readingDate());
            ps.setDouble(2, cons.consumptionValue());
            ps.setInt(3, cons.meterID());
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
        String sql = "SELECT * FROM Consumption";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Consumption c = new Consumption(
                        rs.getInt("ConsumptionID"),
                        rs.getInt("MeterID"),
                        rs.getDouble("ConsumptionValue"),
                        rs.getDate("ReadingDate")
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
        String sql = "SELECT * FROM Consumption WHERE ConsumptionID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, consumptionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Consumption(
                            rs.getInt("ConsumptionID"),
                            rs.getInt("MeterID"),
                            rs.getDouble("ConsumptionValue"),
                            rs.getDate("ReadingDate")
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
        String sql = "UPDATE Consumption SET ReadingDate = ?, ConsumptionValue = ?, MeterID = ? WHERE ConsumptionID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, cons.readingDate());
            ps.setDouble(2, cons.consumptionValue());
            ps.setInt(3, cons.meterID());
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
        String sql = "DELETE FROM Consumption WHERE ConsumptionID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, consumptionId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Consumption deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}