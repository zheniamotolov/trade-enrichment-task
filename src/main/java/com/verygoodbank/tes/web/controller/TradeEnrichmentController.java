package com.verygoodbank.tes.web.controller;


import com.verygoodbank.tes.service.EnrichmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class TradeEnrichmentController {
    private final EnrichmentService enrichmentService;

    public TradeEnrichmentController(EnrichmentService enrichmentService) {
        this.enrichmentService = enrichmentService;
    }

    @RequestMapping(value = "/enrich", method = RequestMethod.POST)
    public ResponseEntity<List<String>> enrichTrade(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(enrichmentService.enrich(file));
    }
}


