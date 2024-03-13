package com.microservicios.productService.service;

import com.microservicios.productService.entity.Category;
import com.microservicios.productService.entity.Product;

import java.util.List;

public interface ProductService {

    public List<Product> listProducts();
    public Product getProduct(Long id);
    public Product createProduct(Product product);
    public Product updateProduct(Product product);
    public Product deleteProduct(Long id);
    public List<Product> findByCategory(Category category);
    public Product updateStock(Long id, Double quantity);

}
