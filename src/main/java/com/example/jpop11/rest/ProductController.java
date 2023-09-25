package com.example.jpop11.rest;

import com.example.jpop11.dto.ProductDTO;
import com.example.jpop11.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(produces= "application/json")
    ResponseEntity<List<ProductDTO>> findAll(){
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping(value="/{productId}",produces= "application/json")
    ResponseEntity<ProductDTO> findById(@PathVariable Long productId){
        return ResponseEntity.ok(productService.findById(productId));
    }

    @PostMapping(produces = "application/json")
   // String createProduct(@RequestBody ProductDTO productDTO) {
       ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO newProduct = productService.createProduct(productDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/productId}").buildAndExpand(newProduct.id()).toUri();
        return ResponseEntity.created(uri).body(newProduct);
        //    return "All Good";
    }

    @PutMapping(value = "{productId}", produces = "application/json")
    ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO){
        return ResponseEntity.ok(productService.updateProduct(productId, productDTO));
    }

    @DeleteMapping("{productId}")
    ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }
}
