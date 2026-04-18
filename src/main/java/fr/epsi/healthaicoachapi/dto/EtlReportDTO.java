package fr.epsi.healthaicoachapi.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;
import java.util.Map;

public class EtlReportDTO {

    @JsonAlias("generated_at")
    private String generatedAt;

    private List<Pipeline> pipelines;
    private Totals totals;

    public EtlReportDTO() {}

    public String getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(String generatedAt) { this.generatedAt = generatedAt; }
    public List<Pipeline> getPipelines() { return pipelines; }
    public void setPipelines(List<Pipeline> pipelines) { this.pipelines = pipelines; }
    public Totals getTotals() { return totals; }
    public void setTotals(Totals totals) { this.totals = totals; }

    public static class Pipeline {
        private String name;
        private String status;

        @JsonAlias("rows_read")
        private long rowsRead;

        @JsonAlias("rows_inserted")
        private long rowsInserted;

        @JsonAlias("rows_rejected")
        private long rowsRejected;

        @JsonAlias("rejection_rate")
        private double rejectionRate;

        @JsonAlias("duration_s")
        private double durationS;

        private Map<String, Long> tables;

        @JsonAlias("top_rejection_reasons")
        private Map<String, Long> topRejectionReasons;

        public Pipeline() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getRowsRead() { return rowsRead; }
        public void setRowsRead(long rowsRead) { this.rowsRead = rowsRead; }
        public long getRowsInserted() { return rowsInserted; }
        public void setRowsInserted(long rowsInserted) { this.rowsInserted = rowsInserted; }
        public long getRowsRejected() { return rowsRejected; }
        public void setRowsRejected(long rowsRejected) { this.rowsRejected = rowsRejected; }
        public double getRejectionRate() { return rejectionRate; }
        public void setRejectionRate(double rejectionRate) { this.rejectionRate = rejectionRate; }
        public double getDurationS() { return durationS; }
        public void setDurationS(double durationS) { this.durationS = durationS; }
        public Map<String, Long> getTables() { return tables; }
        public void setTables(Map<String, Long> tables) { this.tables = tables; }
        public Map<String, Long> getTopRejectionReasons() { return topRejectionReasons; }
        public void setTopRejectionReasons(Map<String, Long> topRejectionReasons) { this.topRejectionReasons = topRejectionReasons; }
    }

    public static class Totals {
        @JsonAlias("rows_read")
        private long rowsRead;

        @JsonAlias("rows_inserted")
        private long rowsInserted;

        @JsonAlias("rows_rejected")
        private long rowsRejected;

        @JsonAlias("rejection_rate")
        private double rejectionRate;

        public Totals() {}

        public long getRowsRead() { return rowsRead; }
        public void setRowsRead(long rowsRead) { this.rowsRead = rowsRead; }
        public long getRowsInserted() { return rowsInserted; }
        public void setRowsInserted(long rowsInserted) { this.rowsInserted = rowsInserted; }
        public long getRowsRejected() { return rowsRejected; }
        public void setRowsRejected(long rowsRejected) { this.rowsRejected = rowsRejected; }
        public double getRejectionRate() { return rejectionRate; }
        public void setRejectionRate(double rejectionRate) { this.rejectionRate = rejectionRate; }
    }
}
