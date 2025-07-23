package com.antoniofarias.marketplace_application.repositories;

import com.antoniofarias.marketplace_application.domain.stock.StockItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StockRepository extends MongoRepository<StockItem, String> {
    Optional<StockItem> findByProductIdAndWarehouseId(String productId, String warehouseId);
}
