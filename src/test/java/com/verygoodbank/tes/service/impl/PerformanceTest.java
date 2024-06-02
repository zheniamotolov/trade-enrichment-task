package com.verygoodbank.tes.service.impl;

import com.verygoodbank.tes.util.DataGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class PerformanceTest {

    private final static int TRADE_RECORDS_AMOUNT = 10000000;
    private final static int PRODUCT_RECORDS_AMOUNT = 100000;
    private final static String PRODUCT_FILE = "src/test/resources/product.csv";
    private final static String TRADE_FILE = "src/test/resources/trade.csv";

    @Autowired
    private TradeEnrichmentService tradeEnrichmentService;


    @BeforeAll
    static void setup() {
        DataGenerator.generateTradeCSV(TRADE_FILE, TRADE_RECORDS_AMOUNT, PRODUCT_RECORDS_AMOUNT);
        DataGenerator.generateProductCSV(PRODUCT_FILE, PRODUCT_RECORDS_AMOUNT);
    }

    @Test
    void shouldValidatePerformance() throws IOException {
        File file = new File(TRADE_FILE);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/csv", input);

        // Measure the time before the method call
        long startTime = System.nanoTime();
        List<String> result = tradeEnrichmentService.enrich(multipartFile);
        // Measure the time after the method call
        long endTime = System.nanoTime();

        // Calculate the execution time
        long executionTime = endTime - startTime;
        System.out.println("Execution time in milliseconds: " + executionTime / 1_000_000);

        assertFalse(result.isEmpty());
    }
}