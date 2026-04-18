package fr.epsi.healthaicoachapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.epsi.healthaicoachapi.dto.DataRecordDTO;
import fr.epsi.healthaicoachapi.dto.PaginatedResponseDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExportService {

    private final ValidationService validationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ExportService(ValidationService validationService) {
        this.validationService = validationService;
    }

    public byte[] export(String format, List<String> entityTypes) throws IOException {
        PaginatedResponseDTO<DataRecordDTO> all = validationService.getRecords(1, Integer.MAX_VALUE, null, null, null, (String) null, (String) null);
        List<DataRecordDTO> records = all.getData();

        if (entityTypes != null && !entityTypes.isEmpty()) {
            records = records.stream()
                    .filter(r -> entityTypes.contains(r.getType()))
                    .toList();
        }

        if ("csv".equalsIgnoreCase(format)) {
            return toCsv(records).getBytes();
        }
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(records);
    }

    private String toCsv(List<DataRecordDTO> records) {
        StringBuilder sb = new StringBuilder("id,type,status,createdAt,updatedAt\n");
        for (DataRecordDTO r : records) {
            sb.append(escape(r.getId())).append(',')
              .append(escape(r.getType())).append(',')
              .append(escape(r.getStatus())).append(',')
              .append(escape(r.getCreatedAt())).append(',')
              .append(escape(r.getUpdatedAt())).append('\n');
        }
        return sb.toString();
    }

    private String escape(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
