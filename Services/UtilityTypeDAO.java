package Services;

import Database.DatabaseConnection;
import Model.UtilityType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilityTypeDAO
{
    public UtilityTypeDAO() {}

    public void addUtilityType(UtilityType utilityType) throws SQLException
    {
        String query = "INSERT INTO ref_utility_type (utility_type_name, description, unit_of_measure, created_date, modified_date, is_active)" +
                       "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setString(1, utilityType.getUtilityTypeName());
            stmt.setString(2, utilityType.getDescription());
            stmt.setString(3, utilityType.getUnitOfMeasure());
            stmt.setDate(4, utilityType.getCreatedDate());
            stmt.setDate(5, utilityType.getModifiedDate());
            stmt.setBoolean(6, utilityType.isActive());
            stmt.executeUpdate();
        }
    }

    public UtilityType getUtilityType(String utilityTypeName) throws SQLException
    {
        String sql = "SELECT * FROM ref_utility_type WHERE utility_type_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, Integer.parseInt(utilityTypeName));
            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return new UtilityType(
                            rs.getInt("UtilityTypeID"),
                            rs.getString("UtilityTypeName"),
                            rs.getString("Description"),
                            rs.getString("UnitOfMeasure"),
                            rs.getDate("CreatedDate"),
                            rs.getDate("ModifiedDate"),
                            rs.getBoolean("isActive")
                    );
                }
            }
        }
        return null;
    }

    public List<UtilityType> getAllUtilityTypes() throws SQLException
    {
        List<UtilityType> utilityTypes = new ArrayList<>();
        String query = "SELECT * FROM ref_utility_type";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query))
        {
            while(rs.next())
            {
                utilityTypes.add(new UtilityType(
                        rs.getInt("UTILITY_TYPE_ID"),
                        rs.getString("UTILITY_TYPE_NAME"),
                        rs.getString("DESCRIPTION"),
                        rs.getString("UNIT_OF_MEASURE"),
                        rs.getDate("CREATED_DATE"),
                        rs.getDate("MODIFIED_DATE"),
                        rs.getBoolean("IS_ACTIVE")
                ));
            }
        }
        return utilityTypes;
    }
}
