package com.mobilgetstudy.service;

import com.mobilgetstudy.dto.request.InventoryRequestDTO;
import com.mobilgetstudy.dto.response.InventoryResponseDTO;
import com.mobilgetstudy.model.Inventory;
import com.mobilgetstudy.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateInventory() {
        // Arrange
        InventoryRequestDTO inventoryRequestDTO = new InventoryRequestDTO();
        inventoryRequestDTO.setProductId(1L);
        inventoryRequestDTO.setStockQuantity(10);

        // Act
        inventoryService.createInventory(inventoryRequestDTO);

        // Assert
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void shouldAddStock() {
        // Arrange
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        inventory.setStockQuantity(10);

        when(inventoryRepository.findByProductId(1L)).thenReturn(inventory);

        // Act
        inventoryService.addStock(1L, 5);

        // Assert
        verify(inventoryRepository, times(1)).findByProductId(1L);
        verify(inventoryRepository, times(1)).save(inventory);
        assertEquals(15, inventory.getStockQuantity());
    }

    @Test
    void shouldRemoveStock() {
        // Arrange
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        inventory.setStockQuantity(10);

        when(inventoryRepository.findByProductId(1L)).thenReturn(inventory);

        // Act
        inventoryService.removeStock(1L, 5);

        // Assert
        verify(inventoryRepository, times(1)).findByProductId(1L);
        verify(inventoryRepository, times(1)).save(inventory);
        assertEquals(5, inventory.getStockQuantity());
    }

    @Test
    void shouldGetInventoryByProductId() {
        // Arrange
        Inventory inventory = new Inventory();
        inventory.setId(1L);
        inventory.setProductId(1L);
        inventory.setStockQuantity(10);

        when(inventoryRepository.findByProductId(1L)).thenReturn(inventory);

        // Act
        InventoryResponseDTO inventoryResponseDTO = inventoryService.getInventoryByProductId(1L);

        // Assert
        verify(inventoryRepository, times(1)).findByProductId(1L);
        assertEquals(1L, inventoryResponseDTO.getId());
        assertEquals(1L, inventoryResponseDTO.getProductId());
        assertEquals(10, inventoryResponseDTO.getQuantity());
    }



}
