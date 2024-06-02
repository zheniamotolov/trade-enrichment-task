package com.verygoodbank.tes.service.impl;

import com.google.common.collect.ImmutableMap;
import com.verygoodbank.tes.exception.ProductInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        Map<String, String> productMap = new HashMap<>();
        productMap.put("productId", "productName");
        ImmutableMap<String, String> immutableProductMap = ImmutableMap.copyOf(productMap);
        ReflectionTestUtils.setField(productService, "productMap", immutableProductMap);
    }

    @Test
    void shouldReturnProductName() {
        String result = productService.getProductName("productId");
        assertEquals("productName", result);
    }

    @Test
    void shouldReturnMissingProductName() {
        String result = productService.getProductName("invalidProductId");
        assertEquals("Missing Product Name", result);
    }

    @Test
    void shouldThrowProductInitializationException() {
        Map<String, String> productMap = new HashMap<>();
        ImmutableMap<String, String> immutableProductMap = ImmutableMap.copyOf(productMap);
        ReflectionTestUtils.setField(productService, "productMap", immutableProductMap);
        assertThrows(ProductInitializationException.class, () -> productService.getProductName("productId"));
    }
}