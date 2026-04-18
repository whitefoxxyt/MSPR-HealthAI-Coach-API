package fr.epsi.healthaicoachapi.dto;

public class DataQualityMetricsDTO {

    private long totalRecords;
    private long missingValues;
    private long duplicates;
    private long anomalies;
    private double completenessRate;
    private String dataFlowStatus;
    private String lastUpdate;

    public DataQualityMetricsDTO() {}

    public long getTotalRecords() { return totalRecords; }
    public void setTotalRecords(long totalRecords) { this.totalRecords = totalRecords; }
    public long getMissingValues() { return missingValues; }
    public void setMissingValues(long missingValues) { this.missingValues = missingValues; }
    public long getDuplicates() { return duplicates; }
    public void setDuplicates(long duplicates) { this.duplicates = duplicates; }
    public long getAnomalies() { return anomalies; }
    public void setAnomalies(long anomalies) { this.anomalies = anomalies; }
    public double getCompletenessRate() { return completenessRate; }
    public void setCompletenessRate(double completenessRate) { this.completenessRate = completenessRate; }
    public String getDataFlowStatus() { return dataFlowStatus; }
    public void setDataFlowStatus(String dataFlowStatus) { this.dataFlowStatus = dataFlowStatus; }
    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }
}
