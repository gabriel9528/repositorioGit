package com.microservicios.productService.service;

import com.microservicios.productService.entity.Category;
import com.microservicios.productService.entity.Product;
import com.microservicios.productService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    @Override
    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        product.setStatus("CREATED");
        product.setCreateAt(new Date());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product product1 = getProduct(product.getId());
        if(product1==null){
            return null;
        }
        product1.setName(product.getName());
        product1.setDescription(product.getName());
        product1.setCategory(product.getCategory());
        product1.setPrice(product.getPrice());
        product1.setStock(product.getStock());
        return productRepository.save(product1);
    }

    @Override
    public Product deleteProduct(Long id) {
        Product product = getProduct(id);
        if(product==null){
            return null;
        }
        product.setStatus("DELETED");
        return productRepository.save(product);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product updateStock(Long id, Double quantity) {
        Product product = getProduct(id);
        if(product==null){
            return null;
        }
        Double stock = product.getStock()+quantity;
        product.setStock(stock);
        return productRepository.save(product);
    }
}
