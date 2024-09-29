package org.mounanga.backend.util;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.mounanga.backend.dto.ProductRequestDTO;
import org.mounanga.backend.dto.ProductResponseDTO;
import org.mounanga.backend.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

public class Mappers {

    private Mappers() {
        super();
    }

    public static @NotNull Product fromProductRequest(@NotNull final ProductRequestDTO dto) {
        final Product product = new Product();
        product.setBarcode(dto.barcode());
        product.setDescription(dto.description());
        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setQuantity(dto.quantity());
        product.setLastModifiedDate(null);
        product.setCreatedDate(LocalDateTime.now());
        return product;
    }

    @NotNull
    @Contract("_ -> new")
    public static ProductResponseDTO fromProduct(final @NotNull Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getBarcode(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCreatedDate(),
                product.getLastModifiedDate()
        );
    }

    public static List<ProductResponseDTO> fromListOfProducts(final @NotNull List<Product> products) {
        return products.stream().map(Mappers::fromProduct).toList();
    }
}
