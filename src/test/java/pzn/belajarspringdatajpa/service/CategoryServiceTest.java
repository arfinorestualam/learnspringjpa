package pzn.belajarspringdatajpa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pzn.belajarspringdatajpa.entity.Category;
import pzn.belajarspringdatajpa.repository.CategoryRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

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

    @Test
    void programmatic() {
        assertThrows(RuntimeException.class, () -> categoryService.createCategories());
    }

    @Test
    void manual() {
        assertThrows(RuntimeException.class, () -> categoryService.manual());
    }

    //test audit
    @Test
    void audit() {
        Category category = new Category();
        category.setName("Sample Audit");
        categoryRepository.save(category);

        assertNotNull(category.getId());
        //to check are the created date and last modified success to write or not
        assertNotNull(category.getCreatedDate());
        assertNotNull(category.getLastModifiedDate());
    }

}