package org.mounanga.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mounanga.backend.dto.ProductRequestDTO;
import org.mounanga.backend.dto.ProductResponseDTO;
import org.mounanga.backend.entity.Product;
import org.mounanga.backend.exception.BarcodeAlreadyExistException;
import org.mounanga.backend.exception.ProductNotFoundException;
import org.mounanga.backend.repository.ProductRepository;
import org.mounanga.backend.util.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public ProductResponseDTO saveProduct(@NotNull ProductRequestDTO dto) {
        log.info("In saveProduct()");
        if(productRepository.existsByBarcode(dto.barcode())){
            throw new BarcodeAlreadyExistException(String.format("Barcode %s already exist", dto.barcode()));
        }
        Product product = Mappers.fromProductRequest(dto);
        Product savedProduct = productRepository.save(product);
        log.info("Product saved with id '{}' , at '{}'", savedProduct.getId(), savedProduct.getCreatedDate());
        return Mappers.fromProduct(savedProduct);
    }

    @Transactional
    @Override
    public ProductResponseDTO updateProduct(Long id, @NotNull ProductRequestDTO dto) {
        log.info("In updateProduct()");
        Product product = findProductById(id);
        if(!product.getBarcode().equals(dto.barcode()) && productRepository.existsByBarcode(dto.barcode())){
                throw new BarcodeAlreadyExistException(String.format("Barcode %s already exist", dto.barcode()));
        }
        product.setBarcode(dto.barcode());
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setLastModifiedDate(LocalDateTime.now());
        Product updatedProduct = productRepository.save(product);
        log.info("Product with id '{}' updated at '{}'", updatedProduct.getId(), updatedProduct.getLastModifiedDate());
        return Mappers.fromProduct(updatedProduct);
    }

    @Transactional
    @Override
    public void deleteProductById(Long id) {
        log.info("In deleteProductById()");
        productRepository.deleteById(id);
        log.info("Product with id '{}' deleted", id);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        log.info("In getProductById()");
        Product product = findProductById(id);
        log.info("Product with id '{}' found", id);
        return Mappers.fromProduct(product);
    }

    @Override
    public ProductResponseDTO getProductByBarcode(String barcode) {
        log.info("In getProductByBarcode()");
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product %s not found", barcode)));
        log.info("Product with barcode '{}' found", product.getBarcode());
        return Mappers.fromProduct(product);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts(int page, int pageSize) {
        log.info("In getAllProducts() with page '{}' and page size '{}'", page, pageSize);
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> products = productRepository.findAll(pageable);
        log.info("{} Products found in page '{}' with size '{}'", products.getTotalElements(), page, pageSize);
        return Mappers.fromListOfProducts(products.getContent());
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        log.info("In getAllProducts()");
        List<Product> products = productRepository.findAll();
        log.info("{} Products found", products.size());
        return Mappers.fromListOfProducts(products);
    }

    @Override
    public List<ProductResponseDTO> searchProducts(String keyword, int page, int pageSize) {
        log.info("In searchProducts()");
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> products = productRepository.searchByNameOrDescription("%" + keyword + "%", pageable);
        log.info("{} Products found with keyword '{}'", products.getTotalElements(), keyword);
        return Mappers.fromListOfProducts(products.getContent());
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id '%s' not found", id)));
    }
}
