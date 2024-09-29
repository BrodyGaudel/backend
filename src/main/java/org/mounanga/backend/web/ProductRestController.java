package org.mounanga.backend.web;

import jakarta.validation.Valid;
import org.mounanga.backend.dto.ProductRequestDTO;
import org.mounanga.backend.dto.ProductResponseDTO;
import org.mounanga.backend.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ProductResponseDTO saveProduct(@RequestBody @Valid ProductRequestDTO dto){
        return productService.saveProduct(dto);
    }

    @PutMapping("/update/{id}")
    public ProductResponseDTO updateProduct(@PathVariable Long id, @RequestBody @Valid  ProductRequestDTO dto){
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
    }

    @GetMapping("/get/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/find/{barcode}")
    public ProductResponseDTO getProductByBarcode(@PathVariable String barcode){
        return productService.getProductByBarcode(barcode);
    }

    @GetMapping("/list")
    public List<ProductResponseDTO> getAllProducts(@RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "pageSize", defaultValue = "9") int pageSize){
        return productService.getAllProducts(page, pageSize);
    }

    @GetMapping("/all")
    public List<ProductResponseDTO> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/search")
    public List<ProductResponseDTO> searchProducts(@RequestParam(name = "keyword", defaultValue = " ") String keyword,
                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "pageSize", defaultValue = "9") int pageSize){
        return productService.searchProducts(keyword, page, pageSize);
    }
}
