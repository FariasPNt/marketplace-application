package com.antoniofarias.marketplace_application.services;

import com.antoniofarias.marketplace_application.domain.category.exceptions.CategoryNotFoundException;
import com.antoniofarias.marketplace_application.domain.product.Product;
import com.antoniofarias.marketplace_application.domain.product.ProductDTO;
import com.antoniofarias.marketplace_application.domain.product.exceptions.ProductNotFoundException;
import com.antoniofarias.marketplace_application.repositories.ProductRepository;
import com.antoniofarias.marketplace_application.services.aws.AwsSnsService;
import com.antoniofarias.marketplace_application.services.aws.MessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final AwsSnsService snsService;
    private final ObjectMapper objectMapper;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, AwsSnsService snsService, ObjectMapper objectMapper){
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.snsService = snsService;
        this.objectMapper = objectMapper;
    }

    public Product insert(ProductDTO productData){
        this.categoryService.getById(productData.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        Product newProduct = new Product(productData);
        this.productRepository.save(newProduct);

        sendEvent("ProductCreated", newProduct.getOwnerId(), newProduct);

        return newProduct;
    }

    public List<Product> getAll(){
        return this.productRepository.findAll();
    }

    public Product update(String id, ProductDTO productData){
        Product product = this.productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        if(productData.categoryId() != null){
            this.categoryService.getById(productData.categoryId())
                    .orElseThrow(CategoryNotFoundException::new);
            product.setCategory(productData.categoryId());
        }

        if(!productData.title().isEmpty()) product.setTitle(productData.title());
        if(!productData.description().isEmpty()) product.setDescription(productData.description());
        if(!(productData.price() == null)) product.setPrice(productData.price());

        this.productRepository.save(product);

        sendEvent("ProductUpdated", product.getOwnerId(), product);

        return product;
    }

    public void delete(String id){
        Product product = this.productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        this.productRepository.delete(product);

        sendEvent("ProductDeleted", product.getOwnerId(), Map.of("id", product.getId()));
    }

    private void sendEvent(String eventType, String ownerId, Object data){
        try{
            MessageDTO message = new MessageDTO(eventType, ownerId, data);
            String jsonMessage = objectMapper.writeValueAsString(message);
            snsService.publish(jsonMessage);
        } catch (JsonProcessingException e){
            throw new RuntimeException("Erro ao serializar evento do produto para SNS", e);
        }
    }
}
