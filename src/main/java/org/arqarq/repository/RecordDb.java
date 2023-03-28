package org.arqarq.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "RECORD", schema = "DATA")
public class RecordDb {
    @Id
    @GeneratedValue
    // @GeneratedValue(generator = "UUID")
    // @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    // @ColumnDefault("random_uuid()")
    // @Type(type = "uuid-char")
    private UUID recordId;
    @Column(nullable = false)
    private String recordFirstName;
    @Column(nullable = false, length = 9)
    private String recordPhoneNo;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal recordAmount;
    @Column(insertable = false)
    private Boolean recordProcessed = false;
    @Column(insertable = false, updatable = false)
    private Timestamp recordTimestamp;

    public UUID getRecordId() {
        return recordId;
    }

    public void setRecordId(UUID recordId) {
        this.recordId = recordId;
    }

    public String getRecordFirstName() {
        return recordFirstName;
    }

    public void setRecordFirstName(String recordFirstName) {
        this.recordFirstName = recordFirstName;
    }

    public String getRecordPhoneNo() {
        return recordPhoneNo;
    }

    public void setRecordPhoneNo(String recordPhoneNo) {
        this.recordPhoneNo = recordPhoneNo;
    }

    public BigDecimal getRecordAmount() {
        return recordAmount;
    }

    public void setRecordAmount(BigDecimal recordAmount) {
        this.recordAmount = recordAmount;
    }

    public Boolean getRecordProcessed() {
        return recordProcessed;
    }

    public void setRecordProcessed(Boolean recordProcessed) {
        this.recordProcessed = recordProcessed;
    }
}