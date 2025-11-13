package Model;

import Database.DatabaseConnection;
import Model.PaymentReceipt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentReceiptCRUD {

    // CREATE
    public boolean addRecord(PaymentReceipt receipt) {
        String sql = "INSERT INTO PAYMENT_RECEIPT (BILL_ID, PAYMENT_DATE, AMOUNT_PAID, PAYMENT_METHOD, RECEIPT_NUMBER, PROCESSED_BY_USER_ID, COLLECTOR_ID, STATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, receipt.getBillID());
            ps.setDate(2, receipt.getPaymentDate());
            ps.setDouble(3, receipt.getAmountPaid());
            ps.setString(4, receipt.getPaymentMethod());
            ps.setString(5, receipt.getReceiptNumber());
            ps.setInt(6, receipt.getProcessedByUserID());
            ps.setInt(7, receipt.getCollectorID());
            ps.setString(8, receipt.getStatus());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("PaymentReceipt addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<PaymentReceipt> getAllRecords() {
        List<PaymentReceipt> list = new ArrayList<>();
        String sql = "SELECT * FROM PAYMENT_RECEIPT";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                PaymentReceipt pr = new PaymentReceipt(
                        rs.getInt("RECEIPT_ID"),
                        rs.getInt("BILL_ID"),
                        rs.getDate("PAYMENT_DATE"),
                        rs.getDouble("AMOUNT_PAID"),
                        rs.getString("PAYMENT_METHOD"),
                        rs.getString("receipt_number"),
                        rs.getInt("processed_by_user_id"),
                        rs.getInt("collector_id"),
                        rs.getString("status")
                );
                list.add(pr);
            }
        } catch (SQLException e) {
            System.err.println("PaymentReceipt getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE
    public PaymentReceipt getRecordById(int receiptId) {
        String sql = "SELECT * FROM PAYMENT_RECEIPT WHERE PAYMENT_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, receiptId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PaymentReceipt(
                            rs.getInt("RECEIPT_ID"),
                            rs.getInt("BILL_ID"),
                            rs.getDate("PAYMENT_DATE"),
                            rs.getDouble("AMOUNT_PAID"),
                            rs.getString("PAYMENT_METHOD"),
                            rs.getString("receipt_number"),
                            rs.getInt("processed_by_user_id"),
                            rs.getInt("collector_id"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("PaymentReceipt getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE and DELETE are typically excluded for financial records (Immutability is best practice)
    // I will include them here for full CRUD completeness, but they should be used with caution.

    // UPDATE
    public boolean updateRecord(PaymentReceipt receipt) {
        String sql = "UPDATE PAYMENT_RECEIPT SET BILL_ID = ?, PAYMENT_DATE = ?, AMOUNT_PAID = ?, PAYMENT_METHOD = ?, RECEIPT_NUMBER = ?, PROCESSED_BY_USER_ID=?, COLLECTOR_ID=?, STATUS=? WHERE PAYMENT_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, receipt.getBillID());
            ps.setDate(2, receipt.getPaymentDate());
            ps.setDouble(3, receipt.getAmountPaid());
            ps.setString(4, receipt.getPaymentMethod());
            ps.setString(5, receipt.getReceiptNumber());
            ps.setInt(6, receipt.getProcessedByUserID());
            ps.setInt(7, receipt.getCollectorID());
            ps.setString(8, receipt.getStatus());
            ps.setInt(9, receipt.getPaymentID());
            ps.executeUpdate();;
            return true;
        } catch (SQLException e) {
            System.err.println("PaymentReceipt updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int paymentID) {
        String sql = "DELETE FROM PAYMENT_RECEIPT WHERE PAYMENT_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, paymentID);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("PaymentReceipt deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}