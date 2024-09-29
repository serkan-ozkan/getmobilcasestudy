package com.mobilgetstudy.controller;

import com.mobilgetstudy.dto.request.InventoryRequestDTO;
import com.mobilgetstudy.dto.response.InventoryResponseDTO;
import com.mobilgetstudy.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryResponseDTO> createInventory(@RequestBody InventoryRequestDTO inventoryRequestDTO) {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.createInventory(inventoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryResponseDTO);
    }

    @PostMapping("/add-stock")
    public ResponseEntity<InventoryResponseDTO> addStock(@RequestBody InventoryRequestDTO inventoryRequestDTO) {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.addStock(inventoryRequestDTO.getProductId(), inventoryRequestDTO.getStockQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(inventoryResponseDTO);
    }

    @PostMapping("/remove-stock")
    public ResponseEntity<InventoryResponseDTO> removeStock(@RequestBody InventoryRequestDTO inventoryRequestDTO) {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.removeStock(inventoryRequestDTO.getProductId(), inventoryRequestDTO.getStockQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(inventoryResponseDTO);

    }

    @GetMapping("/{productId}")
    public InventoryResponseDTO getInventoryByProductId(@PathVariable Long productId) {
        return inventoryService.getInventoryByProductId(productId);
    }
}
