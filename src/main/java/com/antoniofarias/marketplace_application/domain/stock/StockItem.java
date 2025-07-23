package com.antoniofarias.marketplace_application.domain.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stocks_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockItem {

    @Id
    private String id;
    private String productId;
    private String warehouseId;
    private Integer quantity;

}
