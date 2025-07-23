package com.antoniofarias.marketplace_application.domain.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "inventory_movements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMovement {

    @Id
    private String id;
    private String productId;
    private String warehouseId;
    private MovementType type; // ENTRY, EXIT, TRANSFER
    private Integer quantity; // Quantidade movimentada
    private LocalDateTime timestamp;


    public enum MovementType{
        ENTRY,
        EXIT,
        TRANSFER
    }
}
