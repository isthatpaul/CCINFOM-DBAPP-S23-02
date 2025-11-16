package Model;

import Database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilityTypeCRUD
{
    // CREATE
    public boolean addRecord(UtilityType utilityType) {
        String sql = "INSERT INTO UTILITYTYPE (UTILITYTYPENAME, DESCRIPTION, UNITOFMEASURE, CREATEDDATE, MODIFIEDDATE, ISACTIVE) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt  = conn.prepareStatement(sql))
        {
            stmt.setString(1, utilityType.utilityTypeName());
            stmt.setString(2, utilityType.description());
            stmt.setString(3, utilityType.unitOfMeasure());
            stmt.setDate(4, utilityType.createdDate());
            stmt.setDate(5, utilityType.modifiedDate());
            stmt.setBoolean(6, utilityType.isActive());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<UtilityType> getAllRecords() {
        List<UtilityType> utilityTypeList = new ArrayList<>();
        String sql = "SELECT * FROM UTILITYTYPE";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UtilityType ut = new UtilityType(
                        rs.getInt("Utility_Type_ID"),
                        rs.getString("Utility_Type_Name"),
                        rs.getString("Description"),
                        rs.getString("unit_of_measure"),
                        rs.getDate("created_date"),
                        rs.getDate("modified_date"),
                        rs.getBoolean("is_active")
                );
                utilityTypeList.add(ut);
            }
        } catch (SQLException e) {
            System.err.println("getAllRecords error: " + e.getMessage());
        }
        return utilityTypeList;
    }

    // READ ONE
    public UtilityType getRecordById(int id) {
        String sql = "SELECT * FROM UTILITYTYPE WHERE utilitytypeid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UtilityType(
                        rs.getInt("utility_type_id"),
                        rs.getString("utility_type_name"),
                        rs.getString("Description"),
                        rs.getString("unit_of_measure"),
                        rs.getDate("created_date"),
                        rs.getDate("modified_date"),
                        rs.getBoolean("is_active")
                );
            }
        } catch (Exception e) {
            System.err.println("getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(UtilityType utilityType) {
        String sql = "UPDATE UTILITYTYPE SET utilitytypeid=?, utilitytypename=?, description=?, unitofmeasure=?, createddate=?, modifieddate=? WHERE utilitytypeid=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, utilityType.utilityTypeName());
            stmt.setString(2, utilityType.description());
            stmt.setString(3, utilityType.unitOfMeasure());
            stmt.setDate(4, utilityType.createdDate());
            stmt.setDate(5, utilityType.modifiedDate());
            stmt.setInt(6, utilityType.utilityTypeID());
            stmt.setBoolean(7, utilityType.isActive());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int id) {
        String sql = "DELETE FROM UTILITYTYPE WHERE utilitytypeid=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}
