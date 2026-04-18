package fr.epsi.healthaicoachapi.dto;

import java.util.List;

public class BulkValidateResponseDTO {

    private int updated;
    private int failed;
    private List<String> failedIds;

    public BulkValidateResponseDTO() {}

    public BulkValidateResponseDTO(int updated, int failed, List<String> failedIds) {
        this.updated = updated;
        this.failed = failed;
        this.failedIds = failedIds;
    }

    public int getUpdated() { return updated; }
    public void setUpdated(int updated) { this.updated = updated; }
    public int getFailed() { return failed; }
    public void setFailed(int failed) { this.failed = failed; }
    public List<String> getFailedIds() { return failedIds; }
    public void setFailedIds(List<String> failedIds) { this.failedIds = failedIds; }
}
