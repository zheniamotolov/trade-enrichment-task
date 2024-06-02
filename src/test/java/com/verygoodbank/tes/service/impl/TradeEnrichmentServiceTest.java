package com.verygoodbank.tes.service.impl;

import com.verygoodbank.tes.exception.TradeEnrichmentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TradeEnrichmentServiceTest {

    @Mock
    private ProductService productService;
    @InjectMocks
    private TradeEnrichmentService tradeEnrichmentService;

    @Test
    void shouldEnrichTradeData() throws IOException {
        String csvContent = "date,product_id,currency,price\n20220101,productId,USD,100.0";
        InputStream is = new ByteArrayInputStream(csvContent.getBytes());
        MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", is);
        when(productService.getProductName("productId")).thenReturn("productName");

        List<String> result = tradeEnrichmentService.enrich(file);

        assertEquals(2, result.size());
        assertEquals("date,product_name,currency,price", result.get(0));
        assertEquals("20220101,productName,USD,100.0", result.get(1));
    }

    @Test
    void shouldSkipRowWithIncorrectDate() throws IOException {
        String csvContent = "date,product_id,currency,price\ninvalidDate,productId,USD,100.0";
        InputStream is = new ByteArrayInputStream(csvContent.getBytes());
        MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", is);

        List<String> result = tradeEnrichmentService.enrich(file);

        assertEquals(1, result.size());
        assertEquals("date,product_name,currency,price", result.get(0));
    }

    @Test
    void shouldThrowTradeEnrichmentException() {
        MultipartFile file = mock(MultipartFile.class);
        try {
            when(file.getInputStream()).thenThrow(new IOException());
            assertThrows(TradeEnrichmentException.class, () -> tradeEnrichmentService.enrich(file));
        } catch (IOException e) {
            fail("IOException should not be thrown in this test");
        }
    }
}