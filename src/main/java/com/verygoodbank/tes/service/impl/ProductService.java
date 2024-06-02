package com.verygoodbank.tes.service.impl;

import com.google.common.collect.ImmutableMap;
import com.verygoodbank.tes.exception.ProductInitializationException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductService {
    @Value("${product.file.name}")
    private String PRODUCT_FILE;
    private static final String MISSING_PRODUCT_NAME = "Missing Product Name";

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private ImmutableMap<String, String> productMap;

    @PostConstruct
    private void initializeProductMap() {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(PRODUCT_FILE));
            // Skip the header
            for (int i = 1; i < lines.size(); i++) {
                String[] values = lines.get(i).split(",");
                String productId = values[0];
                String productName = values[1];
                builder.put(productId, productName);
            }
            productMap = builder.build();
        } catch (IOException e) {
            throw new ProductInitializationException("Failed to read product file", e);
        } catch (Exception e) {
            throw new ProductInitializationException("Failed to initialize productMap", e);
        }
    }

    public String getProductName(String productId) {
        if (productMap.isEmpty()) {
            throw new ProductInitializationException("Product Map is empty");
        }
        if (productMap.containsKey(productId)) {
            return productMap.get(productId);
        } else {
            log.error("Product name not found for product id: {}", productId);
            return MISSING_PRODUCT_NAME;
        }
    }
}
