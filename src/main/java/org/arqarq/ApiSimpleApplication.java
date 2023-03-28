package org.arqarq;

import org.apache.commons.lang3.tuple.Triple;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static org.arqarq.util.Utils.ConfigMalformedException;
import static org.arqarq.util.Utils.calculateThresholds;
import static org.springframework.boot.Banner.Mode.OFF;

@SpringBootApplication
public class ApiSimpleApplication {
    private static final String CONFIG_FILE_NAME = "CONFIG_FILE.TXT";
    private static final Logger LOG = Logger.getLogger("ApiSimpleApplication");

    @Bean
    List<Triple<Integer, BigDecimal, BigDecimal>> getConfig() throws IOException {
        String conf;
        List<Triple<Integer, BigDecimal, BigDecimal>> config = new LinkedList<>();

        try {
            conf = Files.readString(Paths.get(".", CONFIG_FILE_NAME), StandardCharsets.UTF_8);
        } catch (NoSuchFileException e) {
            try {
                conf = Files.readString(Paths.get("..", CONFIG_FILE_NAME));
            } catch (NoSuchFileException ex) {
                LOG.info("Config file '" + CONFIG_FILE_NAME + "' not found, empty config loaded.");
                return config;
            }
        }
        try {
            config = calculateThresholds(conf.trim());
        } catch (ConfigMalformedException e) {
            LOG.info("Config file '" + CONFIG_FILE_NAME + "' contains malformed data, empty config loaded.");
        }
        LOG.info("");
        LOG.info("Config data [(priority,low,high), ...]: " + config);
        LOG.info("");
        return config;
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ApiSimpleApplication.class);

        springApplication.setBannerMode(OFF);
        springApplication.run(args);
    }
}