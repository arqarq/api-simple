package org.arqarq.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record ReportRowDto(String recordPhoneNo, String recordFirstName, BigDecimal totalAmount, Timestamp lastTimestamp) {
}