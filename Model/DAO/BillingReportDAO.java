package Model.DAO;

import Database.DatabaseConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BillingReportDAO {

    public Map<Integer, Double> getTotalBillsPerCustomer(int month, int year) {
        Map<Integer, Double> totalBills = new HashMap<>();
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
}
