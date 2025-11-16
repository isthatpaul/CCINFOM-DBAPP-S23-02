package Model.DAO;

import Database.DatabaseConnection;
import Reports.RevenueReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Revenue report DAO. Two variants:
 *  - getRevenuePerUtility(year) returns Map<Integer, Double> (kept)
 *  - getRevenueReport(start, end) returns typed report rows with utility name
 */
public class RevenueReportDAO {

    // Keep the original map-based helper (unchanged)
    public Map<Integer, Double> getRevenuePerUtility(int year) {
        Map<Integer, Double> revenueMap = new HashMap<>();
        String sql = """
            SELECT r.UtilityTypeID, SUM(b.AmountDue) AS totalRevenue
            FROM Bill b
            JOIN Rate r ON b.RateID = r.RateID
            JOIN Consumption c ON b.ConsumptionID = c.ConsumptionID
            WHERE YEAR(c.ReadingDate) = ?
            GROUP BY r.UtilityTypeID
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, year);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    revenueMap.put(rs.getInt("UtilityTypeID"), rs.getDouble("totalRevenue"));
                }
            }

        } catch (SQLException e) {
            System.err.println("RevenueReportDAO error: " + e.getMessage());
        }

        return revenueMap;
    }

    // New: return typed revenue report per utility type using payments (collections) over a date range
    public List<RevenueReport> getRevenueReport(Date startInclusive, Date endInclusive) {
        List<RevenueReport> list = new ArrayList<>();
        String sql = """
            SELECT ut.UtilityTypeID,
                   ut.UtilityTypeName,
                   COALESCE(SUM(p.AmountPaid), 0) AS totalRevenue
            FROM Payment p
            JOIN Bill b ON p.BillID = b.BillID
            JOIN Rate r ON b.RateID = r.RateID
            JOIN UtilityType ut ON r.UtilityTypeID = ut.UtilityTypeID
            WHERE p.PaymentDate BETWEEN ? AND ?
            GROUP BY ut.UtilityTypeID, ut.UtilityTypeName
            ORDER BY totalRevenue DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, startInclusive);
            ps.setDate(2, endInclusive);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RevenueReport row = new RevenueReport(
                            rs.getInt("UtilityTypeID"),
                            rs.getString("UtilityTypeName"),
                            rs.getDouble("totalRevenue")
                    );
                    list.add(row);
                }
            }

        } catch (SQLException e) {
            System.err.println("RevenueReportDAO getRevenueReport error: " + e.getMessage());
        }

        return list;
    }
}