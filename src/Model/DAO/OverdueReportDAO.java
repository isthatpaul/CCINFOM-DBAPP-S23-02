package Model.DAO;

import Database.DatabaseConnection;
import Reports.OverdueReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Overdue report DAO: returns per-customer overdue totals for a date range.
 */
public class OverdueReportDAO {

    // Keep simple ID list if needed (fixed to DISTINCT)
    public List<Integer> getOverdueBills(int month, int year) {
        List<Integer> overdueBillIDs = new ArrayList<>();
        String sql = """
            SELECT DISTINCT b.BillID
            FROM Bill b
            WHERE b.Status != 'PAID'
              AND MONTH(b.DueDate) = ?
              AND YEAR(b.DueDate) = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, month);
            ps.setInt(2, year);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    overdueBillIDs.add(rs.getInt("BillID"));
                }
            }

        } catch (SQLException e) {
            System.err.println("OverdueReportDAO error: " + e.getMessage());
        }

        return overdueBillIDs;
    }

    // New: aggregate overdue amount per customer for a date range
    public List<OverdueReport> getOverdueAccounts(Date startInclusive, Date endInclusive) {
        List<OverdueReport> list = new ArrayList<>();
        String sql = """
            SELECT cu.CustomerID,
                   CONCAT(cu.FirstName, ' ', cu.LastName) AS customerName,
                   COALESCE(SUM(b.AmountDue), 0) AS totalOverdue
            FROM Bill b
            JOIN Customer cu ON b.CustomerID = cu.CustomerID
            WHERE b.Status != 'PAID'
              AND b.DueDate BETWEEN ? AND ?
            GROUP BY cu.CustomerID, cu.FirstName, cu.LastName
            ORDER BY totalOverdue DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, startInclusive);
            ps.setDate(2, endInclusive);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OverdueReport r = new OverdueReport(
                            rs.getInt("CustomerID"),
                            rs.getString("customerName"),
                            rs.getDouble("totalOverdue")
                    );
                    list.add(r);
                }
            }

        } catch (SQLException e) {
            System.err.println("OverdueReportDAO getOverdueAccounts error: " + e.getMessage());
        }

        return list;
    }
}