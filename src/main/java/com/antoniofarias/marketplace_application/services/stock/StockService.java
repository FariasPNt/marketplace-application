package com.antoniofarias.marketplace_application.services.stock;

import com.antoniofarias.marketplace_application.domain.stock.StockItem;
import com.antoniofarias.marketplace_application.repositories.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void creatStockForProduct(String productId, String warehouseId){
        StockItem stockItem = new StockItem();
        stockItem.setProductId(productId);
        stockItem.setWarehouseId(warehouseId);
        stockItem.setQuantity(0);

        stockRepository.save(stockItem);
    }

    public void stockEntry(String productId, String warehouseId, int quantity){
        StockItem stock = stockRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        stock.setQuantity(stock.getQuantity() + quantity);
        stockRepository.save(stock);
    }

    public void stockExit(String productId, String warehouseId, int quantity){
        StockItem stock = stockRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        if(stock.getQuantity() < quantity){
            throw new RuntimeException("Estoque insuficiente");
        }

        stock.setQuantity(stock.getQuantity() - quantity);
        stockRepository.save(stock);
    }
}
