package org.mounanga.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequestDTO(
        @NotBlank(message = "field 'barcode' is mandatory : it can not be blank")
        String barcode,

        @NotBlank(message = "field 'name' is mandatory : it can not be blank")
        String name,

        @NotBlank(message = "field 'description' is mandatory : it can not be blank")
        String description,

        @NotNull(message = "field 'price' is mandatory : it can not be null")
        @Positive(message = "field 'price' is mandatory : it can not be positive")
        Double price,

        @NotNull(message = "field 'quantity' is mandatory : it can not be null")
        @Positive(message = "field 'quantity' is mandatory : it can not be positive")
        Integer quantity) {
}
