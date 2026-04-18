package fr.epsi.healthaicoachapi.dto;

import java.util.List;

public class BulkValidateRequestDTO {

    private List<String> ids;
    private String status;

    public BulkValidateRequestDTO() {}

    public List<String> getIds() { return ids; }
    public void setIds(List<String> ids) { this.ids = ids; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
