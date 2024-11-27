package pzn.belajarspringdatajpa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Test
    void success() {
        assertThrows(RuntimeException.class, () -> {
            //it's proof that AOP must be call outside the class
            categoryService.create();
        });
    }

    @Test
    void failed() {
        assertThrows(RuntimeException.class, () -> {
            //it's proof that AOP can't be call inside the class
            categoryService.test();
        });
    }

}