package com.microservicios.productService;

import com.microservicios.productService.entity.Category;
import com.microservicios.productService.entity.Product;
import com.microservicios.productService.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

@DataJpaTest
public class ProductRepositoryMockTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void WhenFindByCategory_thenReturnListProduct() {
        Product product = Product.builder()
                .name("Cellphone")
                .category(Category.builder().id(1L).build())
                .description("iphone14")
                .stock(Double.parseDouble("10"))
                .price(Double.parseDouble("1500.90"))
                .status("CREATED")
                .createAt(new Date())
                .build();
        productRepository.save(product);
        List<Product> listProducts = productRepository.findByCategory(product.getCategory());

        Assertions.assertThat(listProducts.size()).isEqualTo(3);
    }
}
