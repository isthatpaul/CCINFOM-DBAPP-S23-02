package Model.DAO;

import Database.DatabaseConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class RevenueReportDAO {

    // Get total revenue per utility type for a given year
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
}
