package org.arqarq.mapper;

import org.arqarq.openapi.model.ReportRow;
import org.arqarq.openapi.model.ReportRowData;
import org.arqarq.repository.ReportRowDto;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static org.arqarq.util.Utils.switchToComma;

@Component
public class ReportMapper implements GeneralMapper<ReportRow, ReportRowDto> {
    private List<String> dataToArray(ReportRowDto entity) {
        List<String> data = new LinkedList<>();

        data.add(entity.recordFirstName());
        data.add(switchToComma(entity.totalAmount().toString()));
        data.add(entity.lastTimestamp().toLocalDateTime().toLocalDate().toString());
        return data;
    }

    @Override
    public List<ReportRow> toJsonList(List<ReportRowDto> entities) {
        ReportRow reportRowForMerge = new ReportRow();
        List<ReportRow> report = new LinkedList<>();

        entities.forEach(it -> {
            ReportRowData reportRowData = new ReportRowData();

            List<String> data = dataToArray(it);
            reportRowData.add(data);
            reportRowForMerge.merge(it.recordPhoneNo(), reportRowData, (v, v2) -> {
                v.add(data);
                return v;
            });
        });
        reportRowForMerge.forEach((key, value) -> {
            ReportRow reportRow = new ReportRow();

            reportRow.put(key, value);
            report.add(reportRow);
        });
        return report;
    }
}