package org.mounanga.backend.dto;

import java.time.LocalDateTime;

public record ProductResponseDTO(
        Long id,
        String barcode,
        String name,
        String description,
        Double price,
        Integer quantity,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate) {
}
