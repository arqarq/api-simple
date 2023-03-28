package org.arqarq.service;

import org.arqarq.mapper.GeneralMapper;
import org.arqarq.openapi.api.ReportApiDelegate;
import org.arqarq.openapi.model.Report;
import org.arqarq.openapi.model.ReportRow;
import org.arqarq.repository.RecordRepository;
import org.arqarq.repository.ReportRowDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService implements ReportApiDelegate {
    private final RecordRepository recordRepository;
    private final GeneralMapper<ReportRow, ReportRowDto> reportMapper;

    ReportService(RecordRepository recordRepository, GeneralMapper<ReportRow, ReportRowDto> reportMapper) {
        this.recordRepository = recordRepository;
        this.reportMapper = reportMapper;
    }

    @Override
    public ResponseEntity<Report> report(String processedOnly) {
        List<ReportRowDto> reportRows = recordRepository.getReport(true);
        if (!Boolean.parseBoolean(processedOnly)) {
            reportRows.addAll(recordRepository.getReport(false));
        }

        Report report = new Report();

        report.addAll(reportMapper.toJsonList(reportRows));
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}