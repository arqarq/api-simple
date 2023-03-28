package org.arqarq.mapper;

import org.arqarq.openapi.model.Record;
import org.arqarq.repository.RecordDb;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

import static org.arqarq.util.Utils.switchToComma;
import static org.arqarq.util.Utils.switchToDot;

@Component
public class RecordMapper implements GeneralMapper<Record, RecordDb> {
    @Override
    public Record toJson(RecordDb entity) {
        Record json = new Record();

        json.setId(entity.getRecordId().toString());
        json.setFirstName(entity.getRecordFirstName());
        json.setPhoneNumber(entity.getRecordPhoneNo());
        json.setAmount(switchToComma(entity.getRecordAmount().toString()));
        return json;
    }

    @Override
    public RecordDb toEntity(Record json) {
        RecordDb entity = new RecordDb();

        String id = json.getId();
        if (id != null) {
            entity.setRecordId(UUID.fromString(id));
        }
        entity.setRecordFirstName(json.getFirstName());
        entity.setRecordPhoneNo(json.getPhoneNumber());
        entity.setRecordAmount(new BigDecimal(switchToDot(json.getAmount())));
        return entity;
    }
}