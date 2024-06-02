package com.verygoodbank.tes.service.impl;

import com.verygoodbank.tes.exception.TradeEnrichmentException;
import com.verygoodbank.tes.service.EnrichmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

@Service
public class TradeEnrichmentService implements EnrichmentService {
    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final String RESULT_HEADER = "date,product_name,currency,price";
    private static final Logger log = LoggerFactory.getLogger(TradeEnrichmentService.class);

    private final ProductService productService;

    public TradeEnrichmentService(ProductService productService) {
        this.productService = productService;
    }

//    @Override
//    public List<String> validate(MultipartFile file) {
//        List<String> validRows = new ArrayList<>();
//        validRows.add(RESULT_HEADER);
//        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            String line;
//            // Skip the first line header
//            fileReader.readLine();
//
//            while ((line = fileReader.readLine()) != null) {
//                String[] record = line.split(",");
//                if (isValidDate(record[0])) {
//                    validRows.add(csvRecordToString(record));
//                }
//            }
//        } catch (IOException e) {
//            throw new TradeValidationException("Failed to read trade data", e);
//        } catch (Exception e) {
//            throw new TradeValidationException("Failed to validate trade data", e);
//        }
//        return validRows;
//    }

    // Assuming the order of trade rows is not preserved, otherwise a single thread approach could be used
    @Override
    public List<String> enrich(MultipartFile file) {
        ConcurrentLinkedQueue<String> validRows = new ConcurrentLinkedQueue<>();
        validRows.add(RESULT_HEADER);
        try (Stream<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream())).lines()) {
            // Skip the first line header
            lines.skip(1)
                    // Assuming there are no restrictions on using threads, otherwise a thread pool could be used
                    .parallel()
                    .forEach(line -> {
                        String[] record = line.split(",");
                        if (isValidDate(record[0])) {
                            validRows.add(csvRecordToString(record));
                        }
                    });
        } catch (IOException e) {
            throw new TradeEnrichmentException("Failed to read trade data", e);
        } catch (Exception e) {
            throw new TradeEnrichmentException("Failed to validate trade data", e);
        }
        return new ArrayList<>(validRows);
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
            return true;
        } catch (DateTimeParseException e) {
            log.error("Invalid date: {}", date);
            return false;
        }
    }

    private String csvRecordToString(String[] record) {
        String productName = productService.getProductName(record[1]);
        return String.join(",", record[0], productName, record[2], record[3]);
    }
}
