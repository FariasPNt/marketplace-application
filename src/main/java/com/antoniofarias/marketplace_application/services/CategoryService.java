package com.antoniofarias.marketplace_application.services;

import com.antoniofarias.marketplace_application.domain.category.Category;
import com.antoniofarias.marketplace_application.domain.category.CategoryDTO;
import com.antoniofarias.marketplace_application.domain.category.exceptions.CategoryNotFoundException;
import com.antoniofarias.marketplace_application.repositories.CategoryRepository;
import com.antoniofarias.marketplace_application.services.aws.AwsSnsService;
import com.antoniofarias.marketplace_application.services.aws.MessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepository repository;

    private final AwsSnsService snsService;

    public CategoryService(CategoryRepository repository, AwsSnsService snsService){
        this.repository = repository;
        this.snsService = snsService;
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
        this.snsService.publish(new MessageDTO(newCategory.toString()));
        return newCategory;
    }

    public Category update(String id, CategoryDTO categoryData){
        Category category = this.repository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);
        if(!categoryData.title().isEmpty()) category.setTitle(categoryData.title());
        if(!categoryData.description().isEmpty()) category.setDescription(categoryData.description());

        this.repository.save(category);

        this.snsService.publish(new MessageDTO(category.toString()));

        return category;
    }

    public void delete(String id){
        Category category = this.repository.findById(id).orElseThrow(CategoryNotFoundException::new);
        this.repository.delete(category);
    }
}
