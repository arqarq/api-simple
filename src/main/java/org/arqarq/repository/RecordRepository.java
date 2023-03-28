package org.arqarq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<RecordDb, UUID> {
    @Query(value = "SELECT CASE WHEN count(r.recordProcessed) = 0 THEN true ELSE false END FROM RecordDb r " +
        "WHERE r.recordProcessed AND r.recordPhoneNo = :phoneNo AND r.recordTimestamp > :daysBefore")
    boolean doesntExistProcessedPhoneNumberAfterTimestamp(String phoneNo, Timestamp daysBefore);

    List<RecordDb> findAllByRecordProcessed(Boolean recordProcessed);

    @Query("SELECT new org.arqarq.repository.ReportRowDto(r.recordPhoneNo, r.recordFirstName, sum(r.recordAmount), max(r.recordTimestamp)) FROM RecordDb r "
        + "WHERE r.recordProcessed = :recordProcessed GROUP BY r.recordPhoneNo, r.recordFirstName")
    List<ReportRowDto> getReport(Boolean recordProcessed);
}