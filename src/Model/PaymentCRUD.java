package Model;

import Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentCRUD {

    // CREATE
    public boolean addRecord(Payment payment) {
        String sql = "INSERT INTO Payment (BillID, PaymentDate, AmountPaid, PaymentMethod, ReceiptNumber, ProcessedByStaffID, CollectorID, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, payment.billID());
            ps.setDate(2, payment.paymentDate());
            ps.setDouble(3, payment.amountPaid());
            ps.setString(4, payment.paymentMethod());
            ps.setString(5, payment.receiptNumber());
            ps.setInt(6, payment.processedByStaffID());
            if (payment.collectorID() == 0) {
                // If collectorID is optional and you use 0 as sentinel, consider using Integer in record.
                ps.setNull(7, Types.INTEGER);
            } else {
                ps.setInt(7, payment.collectorID());
            }
            ps.setString(8, payment.status());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Payment addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<Payment> getAllRecords() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM Payment";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Integer collector = rs.getObject("CollectorID") != null ? rs.getInt("CollectorID") : 0;
                Payment p = new Payment(
                        rs.getInt("PaymentID"),
                        rs.getInt("BillID"),
                        rs.getDate("PaymentDate"),
                        rs.getDouble("AmountPaid"),
                        rs.getString("PaymentMethod"),
                        rs.getString("ReceiptNumber"),
                        rs.getInt("ProcessedByStaffID"),
                        collector,
                        rs.getString("Status")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Payment getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE
    public Payment getRecordById(int paymentId) {
        String sql = "SELECT * FROM Payment WHERE PaymentID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Integer collector = rs.getObject("CollectorID") != null ? rs.getInt("CollectorID") : 0;
                    return new Payment(
                            rs.getInt("PaymentID"),
                            rs.getInt("BillID"),
                            rs.getDate("PaymentDate"),
                            rs.getDouble("AmountPaid"),
                            rs.getString("PaymentMethod"),
                            rs.getString("ReceiptNumber"),
                            rs.getInt("ProcessedByStaffID"),
                            collector,
                            rs.getString("Status")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Payment getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(Payment payment) {
        String sql = "UPDATE Payment SET BillID = ?, PaymentDate = ?, AmountPaid = ?, PaymentMethod = ?, ReceiptNumber = ?, ProcessedByStaffID = ?, CollectorID = ?, Status = ? WHERE PaymentID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, payment.billID());
            ps.setDate(2, payment.paymentDate());
            ps.setDouble(3, payment.amountPaid());
            ps.setString(4, payment.paymentMethod());
            ps.setString(5, payment.receiptNumber());
            ps.setInt(6, payment.processedByStaffID());
            if (payment.collectorID() == 0) {
                ps.setNull(7, Types.INTEGER);
            } else {
                ps.setInt(7, payment.collectorID());
            }
            ps.setString(8, payment.status());
            ps.setInt(9, payment.paymentID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Payment updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int paymentID) {
        String sql = "DELETE FROM Payment WHERE PaymentID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paymentID);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Payment deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}