package org.mounanga.backend.dto;

import java.util.Set;

public record ExceptionResponseDTO(Integer code,
                                   String message,
                                   String description,
                                   Set<String> validation) {
}
