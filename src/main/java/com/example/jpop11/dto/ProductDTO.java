package com.example.jpop11.dto;

import com.example.jpop11.domain.Product;

public record ProductDTO(
    Long id, String name, String description,Double price) {

    public ProductDTO(Product product){
        this (product.id(),product.name(),product.description(),product.price);
    }

}
