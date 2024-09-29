package com.mobilgetstudy.service;

import com.mobilgetstudy.dto.request.CreateProductRequestDTO;
import com.mobilgetstudy.model.Product;
import com.mobilgetstudy.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryService inventoryService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProduct() {
        // Arrange
        CreateProductRequestDTO productRequestDTO = new CreateProductRequestDTO();
        productRequestDTO.setName("Product 1");
        productRequestDTO.setPrice(BigDecimal.valueOf(100));

        when(productRepository.save(any(Product.class))).thenReturn(new Product());

        // Act
        productService.createProduct(productRequestDTO);

        // Assert
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldCreateProductWithInitialStock() {
        // Arrange
        CreateProductRequestDTO productRequestDTO = new CreateProductRequestDTO();
        productRequestDTO.setName("Product 1");
        productRequestDTO.setPrice(BigDecimal.valueOf(100));
        productRequestDTO.setInitialStock(10);

        when(productRepository.save(any(Product.class))).thenReturn(new Product());
        when(inventoryService.createInventory(any())).thenReturn(null);

        // Act
        productService.createProduct(productRequestDTO);

        // Assert
        verify(productRepository, times(1)).save(any(Product.class));
        verify(inventoryService, times(1)).createInventory(any());
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> products = List.of(new Product(), new Product());

        // Arrange
        when(productRepository.findAll()).thenReturn(products);

        // Act
        productService.getAllProducts();

        // Assert
        verify(productRepository, times(1)).findAll();
        assertEquals(2, productRepository.findAll().size());
    }

    @Test
    void shouldGetProductById() {

        Product product = new Product();
        product.setId(1L);

        // Arrange
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product));

        // Act
        productService.getProductById(1L);

        // Assert
        verify(productRepository, times(1)).findById(1L);
        assertEquals(1L, productRepository.findById(1L).get().getId());
    }

    @Test
    void shouldDeleteProduct() {
        // Arrange
        doNothing().when(productRepository).deleteById(1L);

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository, times(1)).deleteById(1L);
    }
}
