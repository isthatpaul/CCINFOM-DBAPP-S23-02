package Model;

import Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RateCRUD
{
    // CREATE
    public boolean addRecord(Rate rate)
    {
        String sql = "INSERT INTO Rate (UtilityTypeID, RatePerUnit, EffectiveDate) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (rate.utilityTypeID() == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, rate.utilityTypeID());
            }
            ps.setDouble(2, rate.ratePerUnit());
            ps.setDate(3, rate.effectiveDate());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Rate addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<Rate> getAllRecords()
    {
        List<Rate> list = new ArrayList<>();
        String sql = "SELECT * FROM Rate";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Rate r = new Rate(
                        rs.getInt("RateID"),
                        rs.getObject("UtilityTypeID") != null ? rs.getInt("UtilityTypeID") : null,
                        rs.getDouble("RatePerUnit"),
                        rs.getDate("EffectiveDate")
                );
                list.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Rate getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE
    public Rate getRecordById(int rateId)
    {
        String sql = "SELECT * FROM Rate WHERE RateID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rateId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Rate(
                            rs.getInt("RateID"),
                            rs.getObject("UtilityTypeID") != null ? rs.getInt("UtilityTypeID") : null,
                            rs.getDouble("RatePerUnit"),
                            rs.getDate("EffectiveDate")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Rate getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // Add this method to your RateCRUD.java
    public Rate findActiveRateForUtility(int utilityTypeId, Date readingDate) {
        // Find the most recent rate that was effective on or before the reading date
        String sql = "SELECT * FROM Rate WHERE UtilityTypeID = ? AND EffectiveDate <= ? ORDER BY EffectiveDate DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilityTypeId);
            ps.setDate(2, readingDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Rate(
                            rs.getInt("RateID"),
                            rs.getInt("UtilityTypeID"),
                            rs.getDouble("RatePerUnit"),
                            rs.getDate("EffectiveDate")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(Rate rate) {
        String sql = "UPDATE Rate SET UtilityTypeID = ?, RatePerUnit = ?, EffectiveDate = ? WHERE RateID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (rate.utilityTypeID() == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, rate.utilityTypeID());
            }
            ps.setDouble(2, rate.ratePerUnit());
            ps.setDate(3, rate.effectiveDate());
            ps.setInt(4, rate.rateID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Rate updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int rateId) {
        String sql = "DELETE FROM Rate WHERE RateID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rateId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Rate deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}