package org.mounanga.backend.service;

import org.mounanga.backend.dto.ProductRequestDTO;
import org.mounanga.backend.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {

    ProductResponseDTO saveProduct(ProductRequestDTO dto);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto);
    void deleteProductById(Long id);
    ProductResponseDTO getProductById(Long id);
    ProductResponseDTO getProductByBarcode(String barcode);
    List<ProductResponseDTO> getAllProducts(int page, int pageSize);
    List<ProductResponseDTO> getAllProducts();
    List<ProductResponseDTO> searchProducts(String keyword, int page, int pageSize);
}
