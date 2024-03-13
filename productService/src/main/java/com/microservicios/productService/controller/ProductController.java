package com.microservicios.productService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.productService.entity.Category;
import com.microservicios.productService.entity.Product;
import com.microservicios.productService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> listProduct(@RequestParam(value = "categoryId", required = false) Long categoryId){
        List<Product> listProducts = new ArrayList<>();
        if(categoryId == null){
            listProducts = productService.listProducts();
            if(listProducts.isEmpty()){
                return ResponseEntity.noContent().build();
            }
        }else{
            listProducts = productService.findByCategory(Category.builder().id(categoryId).build());
            if(listProducts.isEmpty()){
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(listProducts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id){
        Product product = productService.getProduct(id);
        if(product==null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult result){
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Product productCreated = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product){
        product.setId(id);
        Product product1 = productService.updateProduct(product);
        if(product1==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product1);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id){
        Product productDeleted = productService.deleteProduct(id);
        if(productDeleted==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDeleted);
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Product> updateStockProduct(@PathVariable("id") Long id, @RequestParam(name="quantity", required = true) Double quantity){
        Product product = productService.updateStock(id, quantity);
        if(product==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    private String formatMessage(BindingResult result){
        List<Map<String, String>> listErrors = result.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> errors = new HashMap<>();
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errors;
                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(listErrors)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";
        try{
            jsonString = objectMapper.writeValueAsString(errorMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return jsonString;
    }
}
