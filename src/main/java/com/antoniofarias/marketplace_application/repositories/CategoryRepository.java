package com.antoniofarias.marketplace_application.repositories;


import com.antoniofarias.marketplace_application.domain.category.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

}
