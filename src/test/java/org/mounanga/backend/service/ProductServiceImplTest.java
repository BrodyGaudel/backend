package org.mounanga.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.backend.dto.ProductRequestDTO;
import org.mounanga.backend.dto.ProductResponseDTO;
import org.mounanga.backend.entity.Product;
import org.mounanga.backend.exception.BarcodeAlreadyExistException;
import org.mounanga.backend.exception.ProductNotFoundException;
import org.mounanga.backend.repository.ProductRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceImplTest {


    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void testSaveProductWhenBarcodeDoesNotExistThenSaveProduct() {
        ProductRequestDTO dto = new ProductRequestDTO("123456", "ProductName", "Description", 100.0, 10);
        when(productRepository.existsByBarcode(dto.barcode())).thenReturn(false);

        Product savedProduct = new Product(1L, "123456", "ProductName", "Description", 100.0, 10, LocalDateTime.now(), null);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponseDTO responseDTO = productService.saveProduct(dto);

        assertNotNull(responseDTO);
        assertEquals("123456", responseDTO.barcode());
        assertEquals("ProductName", responseDTO.name());
        assertEquals(100.0, responseDTO.price());
        assertEquals(10, responseDTO.quantity());
        verify(productRepository, times(1)).existsByBarcode(dto.barcode());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testSaveProductWhenBarcodeAlreadyExistsThenThrowException() {
        ProductRequestDTO dto = new ProductRequestDTO("123456", "ProductName", "Description", 100.0, 10);
        when(productRepository.existsByBarcode(dto.barcode())).thenReturn(true);

        BarcodeAlreadyExistException exception = assertThrows(BarcodeAlreadyExistException.class, () -> productService.saveProduct(dto));
        assertEquals("Barcode 123456 already exist", exception.getMessage());
        verify(productRepository, times(1)).existsByBarcode(dto.barcode());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProductWhenBarcodeDoesNotExistThenUpdateProduct() {

        Long productId = 1L;
        ProductRequestDTO dto = new ProductRequestDTO("654321", "UpdatedProductName", "UpdatedDescription", 200.0, 5);
        Product existingProduct = new Product(productId, "123456", "OldProductName", "OldDescription", 100.0, 10, LocalDateTime.now(), null);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.existsByBarcode(dto.barcode())).thenReturn(false);

        Product updatedProduct = new Product(productId, dto.barcode(), dto.name(), dto.description(), dto.price(), dto.quantity(), existingProduct.getCreatedDate(), LocalDateTime.now());
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponseDTO responseDTO = productService.updateProduct(productId, dto);

        assertNotNull(responseDTO);
        assertEquals("654321", responseDTO.barcode());
        assertEquals("UpdatedProductName", responseDTO.name());
        assertEquals(200.0, responseDTO.price());
        assertEquals(5, responseDTO.quantity());
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void testUpdateProductWhenBarcodeAlreadyExistsThenThrowException() {
        Long productId = 1L;
        ProductRequestDTO dto = new ProductRequestDTO("654321", "UpdatedProductName", "UpdatedDescription", 200.0, 5);
        Product existingProduct = new Product(productId, "123456", "OldProductName", "OldDescription", 100.0, 10, LocalDateTime.now(), null);
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.existsByBarcode(dto.barcode())).thenReturn(true);

        BarcodeAlreadyExistException exception = assertThrows(BarcodeAlreadyExistException.class, () -> productService.updateProduct(productId, dto));
        assertEquals("Barcode 654321 already exist", exception.getMessage());
        verify(productRepository, never()).save(existingProduct);
    }

    @Test
    void testDeleteProductById() {
        Long productId = 1L;

        productService.deleteProductById(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testGetProductByIdWhenProductExistsThenReturnProduct() {
        Long productId = 1L;
        Product product = new Product(productId, "123456", "ProductName", "Description", 100.0, 10, LocalDateTime.now(), null);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductResponseDTO responseDTO = productService.getProductById(productId);

        assertNotNull(responseDTO);
        assertEquals("123456", responseDTO.barcode());
        assertEquals("ProductName", responseDTO.name());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetProductByIdWhenProductDoesNotExistThenThrowException() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));

        assertEquals("Product with id '1' not found", exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
    }


    @Test
    void testGetProductByBarcodeWhenProductExistsThenReturnProduct() {
        String barcode = "123456";
        Product product = new Product(1L, barcode, "ProductName", "Description", 100.0, 10, LocalDateTime.now(), null);
        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.of(product));

        ProductResponseDTO responseDTO = productService.getProductByBarcode(barcode);

        assertNotNull(responseDTO);
        assertEquals(barcode, responseDTO.barcode());
        assertEquals("ProductName", responseDTO.name());
        verify(productRepository, times(1)).findByBarcode(barcode);
    }

    @Test
    void testGetProductByBarcodeWhenProductDoesNotExistThenThrowException() {
        String barcode = "123456";
        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.getProductByBarcode(barcode));
        assertEquals("Product 123456 not found", exception.getMessage());
        verify(productRepository, times(1)).findByBarcode(barcode);
    }

    @Test
    void testGetAllProductsWithPagination() {
        int page = 0;
        int pageSize = 2;
        Pageable pageable = PageRequest.of(page, pageSize);
        Product product1 = new Product(1L, "123456", "ProductName1", "Description1", 100.0, 10, LocalDateTime.now(), null);
        Product product2 = new Product(2L, "654321", "ProductName2", "Description2", 200.0, 5, LocalDateTime.now(), null);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product1, product2), pageable, 2);
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        List<ProductResponseDTO> responseDTOs = productService.getAllProducts(page, pageSize);

        assertNotNull(responseDTOs);
        assertEquals(2, responseDTOs.size());
        assertEquals("123456", responseDTOs.get(0).barcode());
        assertEquals("654321", responseDTOs.get(1).barcode());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetAllProductsWithoutPagination() {
        Product product1 = new Product(1L, "123456", "ProductName1", "Description1", 100.0, 10, LocalDateTime.now(), null);
        Product product2 = new Product(2L, "654321", "ProductName2", "Description2", 200.0, 5, LocalDateTime.now(), null);
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<ProductResponseDTO> responseDTOs = productService.getAllProducts();

        assertNotNull(responseDTOs);
        assertEquals(2, responseDTOs.size());
        assertEquals("123456", responseDTOs.get(0).barcode());
        assertEquals("654321", responseDTOs.get(1).barcode());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testSearchProductsWithKeyword() {
        String keyword = "Product";
        int page = 0;
        int pageSize = 2;
        Pageable pageable = PageRequest.of(page, pageSize);
        Product product1 = new Product(1L, "123456", "ProductName1", "Description1", 100.0, 10, LocalDateTime.now(), null);
        Product product2 = new Product(2L, "654321", "ProductName2", "Description2", 200.0, 5, LocalDateTime.now(), null);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product1, product2), pageable, 2);
        when(productRepository.searchByNameOrDescription("%" + keyword + "%", pageable)).thenReturn(productPage);

        List<ProductResponseDTO> responseDTOs = productService.searchProducts(keyword, page, pageSize);

        assertNotNull(responseDTOs);
        assertEquals(2, responseDTOs.size());
        assertEquals("123456", responseDTOs.get(0).barcode());
        assertEquals("654321", responseDTOs.get(1).barcode());
        verify(productRepository, times(1)).searchByNameOrDescription("%" + keyword + "%", pageable);
    }

}