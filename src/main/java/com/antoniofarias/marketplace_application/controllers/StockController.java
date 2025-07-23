package com.antoniofarias.marketplace_application.controllers;

import com.antoniofarias.marketplace_application.services.stock.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/init")
    public ResponseEntity<Void> createStock(@RequestParam String productId,
                                            @RequestParam String warehouseId) {
        stockService.creatStockForProduct(productId, warehouseId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/entry")
    public ResponseEntity<Void> stockEntry(@RequestParam String productId,
                                           @RequestParam String warehouseId,
                                           @RequestParam int quantity) {
        stockService.stockEntry(productId, warehouseId, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/exit")
    public ResponseEntity<Void> stockExit(@RequestParam String productId,
                                          @RequestParam String warehouseId,
                                          @RequestParam int quantity) {
        stockService.stockExit(productId, warehouseId, quantity);
        return ResponseEntity.ok().build();
    }
}

