package org.arqarq;

import org.arqarq.openapi.model.Record;
import org.arqarq.openapi.model.Report;
import org.arqarq.repository.RecordDb;
import org.arqarq.repository.RecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiSimpleApplicationTest {
    private static int TEST_INPUT_DATA_ROW_COUNT = 21;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private RecordRepository recordRepository;

    @Test
    @Order(0)
    void contextLoads() {
        assertNotNull(recordRepository);
        assertNotNull(restTemplate);
    }

    @Test
    @Order(1)
    @DisplayName("Checking if test data are loaded.")
    void testIfTableInMemDbCreatedAndFilled() {
        assertEquals(TEST_INPUT_DATA_ROW_COUNT, recordRepository.count());
    }

    @Test
    @Order(2)
    void testGetRecords() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + port + "/records");
        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        ResponseEntity<List<Record>> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
        assertNotNull(response.getBody());
        assertEquals(TEST_INPUT_DATA_ROW_COUNT, response.getBody().size());
    }

    @Test
    @Order(3)
    void testRecordAdding() throws URISyntaxException {
        Record record = new Record().firstName("Abcdefg")
            .phoneNumber("123456777")
            .amount("14,99");

        ResponseEntity<Record> response = restTemplate.postForEntity(new URI("http://localhost:" + port + "/records"), record, Record.class);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals(36, response.getBody().getId().length());
        assertEquals(++TEST_INPUT_DATA_ROW_COUNT, recordRepository.count());
    }

    @Test
    @Order(4)
    void testProcess() throws URISyntaxException {
        ResponseEntity<Record> response = restTemplate.getForEntity(new URI("http://localhost:" + port + "/process/15,1"), Record.class);
        assertNotNull(response.getBody());
        Optional<RecordDb> byId = recordRepository.findById(UUID.fromString(response.getBody().getId()));
        assertFalse(byId.isEmpty());
        assertTrue(byId.get().getRecordProcessed());
        assertEquals("34,24", response.getBody().getAmount());
    }

    @Test
    @Order(5)
    void testReportProcessedOnly() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + port + "/report?processedOnly=true");
        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        ResponseEntity<Report> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
    }

    @Test
    @Order(6)
    void testReportAll() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + port + "/report");
        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        ResponseEntity<Report> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().size());
    }
}