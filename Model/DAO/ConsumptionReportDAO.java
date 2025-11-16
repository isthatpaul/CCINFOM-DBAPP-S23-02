package Model.DAO;

import Database.DatabaseConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ConsumptionReportDAO {

    // Get average consumption per branch for a given year
    public Map<String, Double> getAverageConsumptionPerBranch(int year) {
        Map<String, Double> avgConsumption = new HashMap<>();
        String sql = """
            SELECT s.AssignedBranch, AVG(c.ConsumptionValue) AS avgConsumption
            FROM Consumption c
            JOIN MeterAssignment ma ON c.MeterID = ma.MeterID
            JOIN Staff s ON ma.AssignedByStaffID = s.StaffID
            WHERE YEAR(c.ReadingDate) = ?
            GROUP BY s.AssignedBranch
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, year);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    avgConsumption.put(rs.getString("AssignedBranch"), rs.getDouble("avgConsumption"));
                }
            }

        } catch (SQLException e) {
            System.err.println("ConsumptionReportDAO error: " + e.getMessage());
        }

        return avgConsumption;
    }
}
