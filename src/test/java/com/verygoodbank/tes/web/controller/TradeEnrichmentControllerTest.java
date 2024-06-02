package com.verygoodbank.tes.web.controller;

import com.verygoodbank.tes.service.EnrichmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeEnrichmentController.class)
public class TradeEnrichmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrichmentService enrichmentService;

    @Test
    public void shouldEnrichTradeData() throws Exception {
        String originalFileName = "test.csv";
        String contentType = "text/csv";
        byte[] content = "date,product_id,currency,price\n20220101,1,USD,100.00".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", originalFileName, contentType, content);

        List<String> enrichedData = List.of("date,product_name,currency,price", "20220101,Product 1,USD,100.00");
        given(enrichmentService.enrich(any(MultipartFile.class))).willReturn(enrichedData);

        mockMvc.perform(multipart("/api/v1/enrich").file(file))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"date,product_name,currency,price\",\"20220101,Product 1,USD,100.00\"]"));
    }
}