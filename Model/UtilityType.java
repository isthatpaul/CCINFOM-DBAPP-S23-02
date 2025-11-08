package Model;

public class UtilityType
{
    private int utilityTypeID;
    private String utilityTypeName;
    private String description;
    private String unitOfMeasure;
    private String createdDate;
    private String modifiedDate;
    private boolean isActive;

    public UtilityType(int utilityTypeID, String utilityTypeName, String description, ) {}

    // getters and setters
    public int getUtilityTypeID() { return utilityTypeID; }

    public void setUtilityTypeID(int utilityTypeID) { this.utilityTypeID = utilityTypeID; }

    public String getUtilityTypeName() { return utilityTypeName; }

    public void setUtilityTypeName(String utilityTypeName) { this.utilityTypeName = utilityTypeName; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getUnitOfMeasure() { return unitOfMeasure; }

    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public String getCreatedDate() { return createdDate; }

    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String getModifiedDate() { return modifiedDate; }

    public void setModifiedDate(String modifiedDate) { this.modifiedDate = modifiedDate; }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) { isActive = active; }
}
