package fr.epsi.healthaicoachapi.dto;

import java.util.Map;

public class DataRecordDTO {

    private String id;
    private String type;
    private Map<String, Object> data;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String validatedBy;
    private String validatedAt;

    public DataRecordDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public String getValidatedBy() { return validatedBy; }
    public void setValidatedBy(String validatedBy) { this.validatedBy = validatedBy; }
    public String getValidatedAt() { return validatedAt; }
    public void setValidatedAt(String validatedAt) { this.validatedAt = validatedAt; }
}
