package com.mobilgetstudy.service;

import com.mobilgetstudy.dto.request.CreateProductRequestDTO;
import com.mobilgetstudy.dto.request.InventoryRequestDTO;
import com.mobilgetstudy.dto.response.ProductResponseDTO;
import com.mobilgetstudy.model.Product;
import com.mobilgetstudy.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final InventoryService inventoryService;

    @Autowired
    public ProductService(ProductRepository productRepository, InventoryService inventoryService) {
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public ProductResponseDTO createProduct(CreateProductRequestDTO createProductRequestDTO) {
        Product product = new Product();
        product.setName(createProductRequestDTO.getName());
        product.setPrice(createProductRequestDTO.getPrice());
        product.setDiscount(createProductRequestDTO.getDiscount());
        product.setTax(createProductRequestDTO.getTax());

        Product createdProduct = productRepository.save(product);

        if (createProductRequestDTO.getInitialStock() != null) {
            InventoryRequestDTO inventoryRequestDTO = new InventoryRequestDTO();
            inventoryRequestDTO.setProductId(createdProduct.getId());
            inventoryRequestDTO.setStockQuantity(createProductRequestDTO.getInitialStock());
            inventoryService.createInventory(inventoryRequestDTO);
        }

        return convertToDTO(createdProduct);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    public ProductResponseDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductResponseDTO convertToDTO(Product product) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(product.getId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setDiscount(product.getDiscount());
        productResponseDTO.setTax(product.getTax());
        return productResponseDTO;
    }
}
