package com.antoniofarias.marketplace_application.domain.category.exceptions;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(){
        super("Categoria não encontrada");
    }
}
