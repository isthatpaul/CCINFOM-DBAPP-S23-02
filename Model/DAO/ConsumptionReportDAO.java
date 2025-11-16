package Model.DAO;

import Database.DatabaseConnection;
import Reports.ConsumptionAnalysisReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Consumption analysis DAO: average consumption per branch over a date range.
 */
public class ConsumptionReportDAO {

    // Keep the original method (month->year variant) if desired, but new variant accepts date range
    public List<ConsumptionAnalysisReport> getAverageConsumptionPerBranch(Date startInclusive, Date endInclusive) {
        List<ConsumptionAnalysisReport> avgConsumption = new ArrayList<>();
        String sql = """
            SELECT s.AssignedBranch, AVG(c.ConsumptionValue) AS avgConsumption
            FROM Consumption c
            JOIN MeterAssignment ma ON c.MeterID = ma.MeterID
            JOIN Staff s ON ma.AssignedByStaffID = s.StaffID
            WHERE c.ReadingDate BETWEEN ? AND ?
            GROUP BY s.AssignedBranch
            ORDER BY s.AssignedBranch
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, startInclusive);
            ps.setDate(2, endInclusive);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ConsumptionAnalysisReport r = new ConsumptionAnalysisReport(
                            rs.getString("AssignedBranch"),
                            rs.getDouble("avgConsumption")
                    );
                    avgConsumption.add(r);
                }
            }

        } catch (SQLException e) {
            System.err.println("ConsumptionReportDAO error: " + e.getMessage());
        }

        return avgConsumption;
    }
}