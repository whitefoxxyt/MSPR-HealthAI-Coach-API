package fr.epsi.healthaicoachapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import fr.epsi.healthaicoachapi.dto.EtlReportDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class EtlReportService {

    private final String reportPath;
    private final ObjectMapper objectMapper;

    public EtlReportService(
            @Value("${etl.report.path:/app/etl-data/processed/quality_report.json}") String reportPath) {
        this.reportPath = reportPath;
        this.objectMapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public EtlReportDTO getReport() throws IOException {
        File file = new File(reportPath);
        if (!file.exists()) {
            throw new IOException("ETL report not found at: " + reportPath);
        }
        return objectMapper.readValue(file, EtlReportDTO.class);
    }
}
