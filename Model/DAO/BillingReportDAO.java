package Model.DAO;

import Database.DatabaseConnection;
import Reports.BIllingCollectionReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Billing and collection report DAO.
 * Returns one row per bill containing bill, customer, consumption and payment summary fields.
 */
public class BillingReportDAO {

    // Keep the simple aggregate per customer if you need it
    public java.util.Map<Integer, Double> getTotalBillsPerCustomer(int month, int year) {
        java.util.Map<Integer, Double> totalBills = new java.util.HashMap<>();
        String sql = """
            SELECT b.CustomerID, SUM(b.AmountDue) AS totalAmount
            FROM Bill b
            JOIN Consumption c ON b.ConsumptionID = c.ConsumptionID
            WHERE MONTH(c.ReadingDate) = ? AND YEAR(c.ReadingDate) = ?
            GROUP BY b.CustomerID
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, month);
            ps.setInt(2, year);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totalBills.put(rs.getInt("CustomerID"), rs.getDouble("totalAmount"));
                }
            }

        } catch (SQLException e) {
            System.err.println("BillingReportDAO error: " + e.getMessage());
        }

        return totalBills;
    }

    // New: detailed billing + collection rows per bill for a date range
    public List<BIllingCollectionReport> getBillingCollectionReport(Date startInclusive, Date endInclusive) {
        List<BIllingCollectionReport> rows = new ArrayList<>();
        String sql = """
            SELECT b.BillID,
                   CONCAT(cu.FirstName, ' ', cu.LastName) AS customerName,
                   co.ConsumptionValue AS consumptionValue,
                   b.AmountDue,
                   COALESCE(SUM(p.AmountPaid), 0) AS amountPaid,
                   co.ReadingDate AS billingDate,
                   MAX(p.PaymentDate) AS paymentDate,
                   s.Username AS staffName
            FROM Bill b
            JOIN Consumption co ON b.ConsumptionID = co.ConsumptionID
            JOIN Customer cu ON b.CustomerID = cu.CustomerID
            LEFT JOIN Payment p ON p.BillID = b.BillID
            LEFT JOIN Staff s ON p.ProcessedByStaffID = s.StaffID
            WHERE co.ReadingDate BETWEEN ? AND ?
            GROUP BY b.BillID, cu.FirstName, cu.LastName, co.ConsumptionValue, b.AmountDue, co.ReadingDate, s.Username
            ORDER BY co.ReadingDate, b.BillID
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, startInclusive);
            ps.setDate(2, endInclusive);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BIllingCollectionReport r = new BIllingCollectionReport(
                            rs.getInt("BillID"),
                            rs.getString("customerName"),
                            rs.getDouble("consumptionValue"),
                            rs.getDouble("AmountDue"),
                            rs.getObject("amountPaid") != null ? rs.getDouble("amountPaid") : null,
                            rs.getDate("billingDate"),
                            rs.getDate("paymentDate"),
                            rs.getString("staffName")
                    );
                    rows.add(r);
                }
            }

        } catch (SQLException e) {
            System.err.println("BillingReportDAO getBillingCollectionReport error: " + e.getMessage());
        }

        return rows;
    }
}