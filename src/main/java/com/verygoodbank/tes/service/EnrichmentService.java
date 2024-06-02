package com.verygoodbank.tes.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EnrichmentService {
    List<String> enrich(MultipartFile file);
}
