package Model;

import Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillCRUD
{
    // CREATE
    public boolean addRecord(Bill bill) {
        String sql = "INSERT INTO Bill (CustomerID, ConsumptionID, RateID, AmountDue, DueDate, Status, GeneratedByStaffID, TechnicianID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bill.customerID());
            ps.setInt(2, bill.consumptionID());
            ps.setInt(3, bill.rateID());
            ps.setDouble(4, bill.amountDue());
            ps.setDate(5, bill.dueDate());
            ps.setString(6, bill.status());
            ps.setInt(7, bill.generatedByStaffID());
            if (bill.technicianID() == null) {
                ps.setNull(8, Types.INTEGER);
            } else {
                ps.setInt(8, bill.technicianID());
            }
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Bill addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<Bill> getAllRecords() {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT * FROM Bill";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Integer technician = rs.getObject("TechnicianID") != null ? rs.getInt("TechnicianID") : null;
                Bill b = new Bill(
                        rs.getInt("BillID"),
                        rs.getInt("CustomerID"),
                        rs.getInt("ConsumptionID"),
                        rs.getInt("RateID"),
                        rs.getDouble("AmountDue"),
                        rs.getDate("DueDate"),
                        rs.getString("Status"),
                        rs.getInt("GeneratedByStaffID"),
                        technician
                );
                list.add(b);
            }
        } catch (SQLException e) {
            System.err.println("Bill getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE
    public Bill getRecordById(int billId) {
        String sql = "SELECT * FROM Bill WHERE BillID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, billId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Integer technician = rs.getObject("TechnicianID") != null ? rs.getInt("TechnicianID") : null;
                    return new Bill(
                            rs.getInt("BillID"),
                            rs.getInt("CustomerID"),
                            rs.getInt("ConsumptionID"),
                            rs.getInt("RateID"),
                            rs.getDouble("AmountDue"),
                            rs.getDate("DueDate"),
                            rs.getString("Status"),
                            rs.getInt("GeneratedByStaffID"),
                            technician
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Bill getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(Bill bill) {
        String sql = "UPDATE Bill SET CustomerID = ?, ConsumptionID = ?, RateID = ?, AmountDue = ?, DueDate = ?, Status = ?, GeneratedByStaffID = ?, TechnicianID = ? WHERE BillID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bill.customerID());
            ps.setInt(2, bill.consumptionID());
            ps.setInt(3, bill.rateID());
            ps.setDouble(4, bill.amountDue());
            ps.setDate(5, bill.dueDate());
            ps.setString(6, bill.status());
            ps.setInt(7, bill.generatedByStaffID());
            if (bill.technicianID() == null) {
                ps.setNull(8, Types.INTEGER);
            } else {
                ps.setInt(8, bill.technicianID());
            }
            ps.setInt(9, bill.billID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Bill updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int billId) {
        String sql = "DELETE FROM Bill WHERE BillID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, billId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Bill deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}