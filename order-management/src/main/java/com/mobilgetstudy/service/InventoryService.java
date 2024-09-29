package com.mobilgetstudy.service;

import com.mobilgetstudy.dto.request.InventoryRequestDTO;
import com.mobilgetstudy.dto.response.InventoryResponseDTO;
import com.mobilgetstudy.model.Inventory;
import com.mobilgetstudy.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public InventoryResponseDTO createInventory(InventoryRequestDTO inventoryRequestDTO) {
        Inventory inventory = new Inventory();
        inventory.setProductId(inventoryRequestDTO.getProductId());
        inventory.setStockQuantity(inventoryRequestDTO.getStockQuantity());
        inventoryRepository.save(inventory);
        return convertToDTO(inventory);
    }

    @Transactional
    public InventoryResponseDTO addStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId);
        inventory.setStockQuantity(inventory.getStockQuantity() + quantity);
        inventoryRepository.save(inventory);
        return convertToDTO(inventory);
    }
    @Transactional
    public InventoryResponseDTO removeStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId);
        inventory.setStockQuantity(inventory.getStockQuantity() - quantity);
        inventoryRepository.save(inventory);
        return convertToDTO(inventory);
    }

    public InventoryResponseDTO getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId);
        return convertToDTO(inventory);
    }

    private InventoryResponseDTO convertToDTO(Inventory inventory) {
        InventoryResponseDTO inventoryResponseDTO = new InventoryResponseDTO();
        inventoryResponseDTO.setId(inventory.getId());
        inventoryResponseDTO.setProductId(inventory.getProductId());
        inventoryResponseDTO.setQuantity(inventory.getStockQuantity());
        return inventoryResponseDTO;
    }


}
