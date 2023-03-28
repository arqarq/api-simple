package org.arqarq.service;

import org.apache.commons.lang3.tuple.Triple;
import org.arqarq.mapper.GeneralMapper;
import org.arqarq.openapi.api.ProcessApiDelegate;
import org.arqarq.openapi.api.RecordsApiDelegate;
import org.arqarq.openapi.model.Record;
import org.arqarq.openapi.model.Records;
import org.arqarq.repository.RecordDb;
import org.arqarq.repository.RecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.NativeWebRequest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.arqarq.util.Utils.switchToDot;

@Service
public class RecordsService implements RecordsApiDelegate, ProcessApiDelegate {
    private final RecordRepository recordRepository;
    private final GeneralMapper<Record, RecordDb> recordMapper;
    private final List<Triple<Integer, BigDecimal, BigDecimal>> ranges;

    RecordsService(RecordRepository recordRepository, GeneralMapper<Record, RecordDb> recordMapper, List<Triple<Integer, BigDecimal, BigDecimal>> getConfig) {
        this.recordRepository = recordRepository;
        this.recordMapper = recordMapper;
        ranges = getConfig;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return RecordsApiDelegate.super.getRequest();
    }

    @Override
    public ResponseEntity<Records> listRecords() {
        Records records = new Records();

        records.addAll(recordRepository.findAll().stream()
            .map(recordMapper::toJson)
            .toList());
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Record> createRecord(Record record) {
        return new ResponseEntity<>(recordMapper.toJson(recordRepository.save(recordMapper.toEntity(record))), HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<Record> process(String amount) {
        Record record = null;

        BigDecimal thrAmount = new BigDecimal(switchToDot(amount));
        Triple<Integer, BigDecimal, BigDecimal> range = ranges.stream().filter(it -> !(thrAmount.compareTo(it.getMiddle()) < 0 ||
                thrAmount.compareTo(it.getRight()) > 0))
            .max(Comparator.comparing(Triple::getLeft))
            .orElse(Triple.of(null, BigDecimal.ZERO, new BigDecimal("99999999.99")));
        BigDecimal low = range.getMiddle();
        BigDecimal high = range.getRight();
        for (RecordDb recordDb : recordRepository.findAllByRecordProcessed(false).stream().toList()) {
            if (checkBusinessLogic(recordDb, low, high)) {
                recordDb.setRecordProcessed(true);
                record = recordMapper.toJson(recordDb);
                break;
            }
        }
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    private boolean checkBusinessLogic(RecordDb recordDb, BigDecimal low, BigDecimal high) {
        BigDecimal recordAmount = recordDb.getRecordAmount();
        boolean rangeNotPass = recordAmount.compareTo(high) > 0 || recordAmount.compareTo(low) < 0;
        return !rangeNotPass && recordRepository.doesntExistProcessedPhoneNumberAfterTimestamp(recordDb.getRecordPhoneNo(),
            Timestamp.from(LocalDateTime.now().plus(-3, DAYS)
                .atZone(ZoneId.of("Europe/Warsaw"))
                .toInstant()));
    }
}