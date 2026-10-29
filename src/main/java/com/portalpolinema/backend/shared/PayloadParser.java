package com.portalpolinema.backend.shared;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class PayloadParser {
    private final ObjectMapper objectMapper;

    public PayloadParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode parse(String payload, int id) {
        try {
            return objectMapper.readTree(payload);
        } catch (Exception e) {
            throw new RuntimeException("Invalid payload JSON for approval_request id: " + id, e);
        }
    }
}
