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
        String sql = "INSERT INTO RATE (UTILITYTYPEID, RATEPERUNIT, EFFECTIVEDATE) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, rate.utilityTypeID());
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
        String sql = "SELECT * FROM RATE";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Rate r = new Rate(
                        rs.getInt("RATE_ID"),
                        rs.getInt("UTILITY_TYPE_ID"),
                        rs.getDouble("RATE_PER_UNIT"),
                        rs.getDate("EFFECTIVE_DATE")
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
        String sql = "SELECT * FROM RATE WHERE RATEID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rateId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Rate(
                            rs.getInt("RATE_ID"),
                            rs.getInt("UTILITY_TYPE_ID"),
                            rs.getDouble("RATE_PER_UNIT"),
                            rs.getDate("EFFECTIVE_DATE")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Rate getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(Rate rate) {
        String sql = "UPDATE RATE SET UTILITYTYPEID = ?, RATEPERUNIT = ?, EFFECTIVEDATE = ? WHERE RATEID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, rate.utilityTypeID());
            ps.setDouble(2, rate.ratePerUnit());
            ps.setDate(3, rate.effectiveDate());
                ps.setLong(4, rate.rateID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Rate updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int rateId) {
        String sql = "DELETE FROM RATE WHERE RATEID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, rateId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Rate deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}