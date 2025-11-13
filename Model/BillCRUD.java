package Model;

import Database.DatabaseConnection;
import Model.Bill;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillCRUD
{
    // CREATE
    public boolean addRecord(Bill bill) {
        String sql = "INSERT INTO BILL (CONSUMPTION_ID, CUSTOMER_ID, RATE_ID, DUE_DATE, AMOUNT_DUE, STATUS, GENERATED_BY_USER_ID, TECHNICIAN_ID) VALUES (?, ?, ?, ?, ?, ?, ? , ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bill.getConsumptionID());
            ps.setInt(2, bill.getCustomerID());
            ps.setInt(3, bill.getRateID());
            ps.setDate(4, bill.getDueDate());
            ps.setDouble(5, bill.getAmountDue());
            ps.setString(6, bill.getStatus());
            ps.setInt(7,bill.getGeneratedByUserID());
            ps.setInt(8,bill.getTechnicianID());
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
        String sql = "SELECT * FROM BILL";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Bill b = new Bill(
                        rs.getInt("BILL_ID"),
                        rs.getInt("CONSUMPTION_ID"),
                        rs.getInt("CUSTOMER_ID"),
                        rs.getInt("Rate_ID"),
                        rs.getDouble("AMOUNT_DUE"),
                        rs.getDate("Due_date"),
                        rs.getString("status"),
                        rs.getInt("generated_by_user_id"),
                        rs.getInt("TECHNICIAN_ID")
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
        String sql = "SELECT * FROM BILL WHERE BILL_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, billId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Bill(
                            rs.getInt("BILL_ID"),
                            rs.getInt("CONSUMPTION_ID"),
                            rs.getInt("CUSTOMER_ID"),
                            rs.getInt("Rate_ID"),
                            rs.getDouble("AMOUNT_DUE"),
                            rs.getDate("Due_date"),
                            rs.getString("status"),
                            rs.getInt("generated_by_user_id"),
                            rs.getInt("TECHNICIAN_ID")
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
        String sql = "UPDATE BILL SET CONSUMPTION_ID = ?, CUSTOMER_ID = ?, RATE_ID = ?, DUE_DATE = ?, AMOUNT_DUE = ?, STATUS = ?, GENERATED_BY_USER_ID = ?, TECHNICIAN_ID=? WHERE BILL_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bill.getConsumptionID());
            ps.setInt(2, bill.getCustomerID());
            ps.setInt(3, bill.getRateID());
            ps.setDate(4, bill.getDueDate());
            ps.setDouble(5, bill.getAmountDue());
            ps.setString(6, bill.getStatus());
            ps.setInt(7,bill.getGeneratedByUserID());
            ps.setInt(8,bill.getTechnicianID());
            ps.setInt(9, bill.getBillID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Bill updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int billId) {
        String sql = "DELETE FROM BILL WHERE BILL_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, billId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Bill deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}