package com.antoniofarias.marketplace_application.services;

import com.antoniofarias.marketplace_application.domain.category.Category;
import com.antoniofarias.marketplace_application.domain.category.CategoryDTO;
import com.antoniofarias.marketplace_application.domain.category.exceptions.CategoryNotFoundException;
import com.antoniofarias.marketplace_application.domain.product.Product;
import com.antoniofarias.marketplace_application.repositories.CategoryRepository;
import com.antoniofarias.marketplace_application.repositories.ProductRepository;
import com.antoniofarias.marketplace_application.services.aws.AwsSnsService;
import com.antoniofarias.marketplace_application.services.aws.MessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepository repository;
    private ProductRepository productRepository;
    private final AwsSnsService snsService;
    private final ObjectMapper objectMapper;

    public CategoryService(CategoryRepository repository, AwsSnsService snsService, ProductRepository productRepository, ObjectMapper objectMapper){
        this.repository = repository;
        this.snsService = snsService;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    public List<Category> getAll(){
        return this.repository.findAll();
    }

    public Optional<Category> getById(String id){
        return this.repository.findById(id);
    }

    public Category insert(CategoryDTO categoryData){
        Category newCategory = new Category(categoryData);
        this.repository.save(newCategory);

        sendEvent("CategoryCreated", newCategory.getOwnerId(), newCategory);
        return newCategory;
    }

    public Category update(String id, CategoryDTO categoryData){
        Category category = this.repository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);
        if(!categoryData.title().isEmpty()) category.setTitle(categoryData.title());
        if(!categoryData.description().isEmpty()) category.setDescription(categoryData.description());

        this.repository.save(category);

        sendEvent("CategoryUpdated", category.getOwnerId(), category);

        return category;
    }

    public void delete(String id){
        Category category = this.repository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        List<Product> productsToDelete = productRepository.findByCategoryId(id);
        if(!productsToDelete.isEmpty()) {
            productRepository.deleteAll(productsToDelete);

            for(Product product : productsToDelete){
                sendEvent("ProductDeleted", category.getOwnerId(), Map.of("id", product.getId()));
            }
        }

        this.repository.delete(category);

        sendEvent("CategoryDeleted", category.getOwnerId(), Map.of("id", category.getId()));
    }

    private void sendEvent(String eventType, String ownerId, Object data){
        try{
            MessageDTO message = new MessageDTO(eventType, ownerId, data);
            String jsonMessage = objectMapper.writeValueAsString(message);
            snsService.publish(jsonMessage);
        } catch (JsonProcessingException e){
            throw new RuntimeException("Erro ao serializar evento para SNS", e);
        }
    }
}
