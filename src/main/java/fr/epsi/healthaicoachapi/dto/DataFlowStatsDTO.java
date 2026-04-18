package fr.epsi.healthaicoachapi.dto;

public class DataFlowStatsDTO {

    private String name;
    private String type;
    private String status;
    private long recordsToday;
    private String lastSync;
    private double errorRate;

    public DataFlowStatsDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getRecordsToday() { return recordsToday; }
    public void setRecordsToday(long recordsToday) { this.recordsToday = recordsToday; }
    public String getLastSync() { return lastSync; }
    public void setLastSync(String lastSync) { this.lastSync = lastSync; }
    public double getErrorRate() { return errorRate; }
    public void setErrorRate(double errorRate) { this.errorRate = errorRate; }
}
