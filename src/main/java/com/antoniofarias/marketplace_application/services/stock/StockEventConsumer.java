package com.antoniofarias.marketplace_application.services.stock;

import com.antoniofarias.marketplace_application.services.aws.MessageDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class StockEventConsumer {

    private final StockService stockService;
    private final ObjectMapper objectMapper;

    public StockEventConsumer(StockService stockService, ObjectMapper objectMapper) {
        this.stockService = stockService;
        this.objectMapper = objectMapper;
    }

    public void consumeProductCreateEvent(String jsonMessage){
        try{
            MessageDTO message = objectMapper.readValue(jsonMessage, MessageDTO.class);

            if("ProductCreated".equals(message.eventType())){

                JsonNode data = objectMapper.convertValue(message.data(), JsonNode.class);

                String productId = data.get("id").asText();

                // Definindo depósito padrão (poderia vir do evento ou estar configurado)
                String defaultWarehouseId = "default-warehouse";

                stockService.creatStockForProduct(productId, defaultWarehouseId);
            }
        } catch (Exception e){
            throw new RuntimeException("Erro ao processar evento de produto criado", e);
        }
    }

}
