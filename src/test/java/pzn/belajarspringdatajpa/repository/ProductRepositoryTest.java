package pzn.belajarspringdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import pzn.belajarspringdatajpa.entity.Category;
import pzn.belajarspringdatajpa.entity.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createProduct() {
        Category category = categoryRepository.findById(10L).orElse(null);
        assertNotNull(category);

        {
            Product product = new Product();
            product.setName("Iphone 16");
            product.setPrice(25_0000_0000L);
            product.setCategory(category);
            productRepository.save(product);
        }

        {
            Product product = new Product();
            product.setName("Iphone 8");
            product.setPrice(5_0000_0000L);
            product.setCategory(category);
            productRepository.save(product);
        }
    }

    @Test
    void findProducts() {
        List<Product> products = productRepository.findAllByCategory_Name("Category 0");

        assertEquals(2, products.size());
        assertEquals("Iphone 16", products.get(0).getName());
        assertEquals("Iphone 8", products.get(1).getName());
    }

    @Test
    void findProductSort() {
        //fyi: sort can be multiple argument, just add , at the end of sort
        //this means, sort by id descending :
        Sort sort = Sort.by(Sort.Order.desc("id"));
        //adding sort here, it'll reverse the test of findProducts()
        //cause the id will be sort from the last
        List<Product> products = productRepository.findAllByCategory_Name("Category 0", sort);
        assertEquals(2, products.size());
        assertEquals("Iphone 16", products.get(1).getName());
        assertEquals("Iphone 8", products.get(0).getName());
    }

}