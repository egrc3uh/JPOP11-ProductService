package com.example.jpop11.service;

import com.example.jpop11.domain.Product;
import com.example.jpop11.dto.ProductDTO;
import com.example.jpop11.error.ProductNotFoundException;
import com.example.jpop11.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    public ProductDTO findById(Long productId) {
       return productRepository.findById(productId).map(ProductDTO::new).orElseThrow(productNotFound(productId));
    }

    public ProductDTO createProduct(ProductDTO productDTO){
        Product product = new Product(productDTO.id(), productDTO.name(), productDTO.description(), productDTO.price());
        return new ProductDTO(productRepository.save(product));
    }

    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product product = new Product(productDTO.id(), productDTO.name(), productDTO.description(), productDTO.price());
        return productRepository.findById(productId).map(newProduct -> productRepository.save(product)).map(ProductDTO::new).orElseThrow(productNotFound(productId));
    }

    public void delete(Long productId) {
        productRepository.findById(productId).ifPresentOrElse(productRepository::delete,()-> { new ProductNotFoundException("No Product with this id found " + productId); });

        productRepository.findById(productId).ifPresent(productRepository::delete);
    }

    private Supplier<ProductNotFoundException> productNotFound(Long productId) {
        return () -> new ProductNotFoundException("No Product with this id found "+ productId);
    }

}
