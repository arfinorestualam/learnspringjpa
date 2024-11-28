package pzn.belajarspringdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pzn.belajarspringdatajpa.entity.Category;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testInsert() {
        Category category = new Category();
        category.setName("GADGET");

        categoryRepository.save(category);

        assertNotNull(category.getId());
    }

    @Test
    void testUpdate() {
        //check if the repository have data or not
        Category category = categoryRepository.findById(4L).orElse(null);
        assertNotNull(category);

        //set new name
        category.setName("GADGET MURAH");
        //change the data
        categoryRepository.save(category);

        category = categoryRepository.findById(4L).orElse(null);
        assertNotNull(category);
        assertEquals("GADGET MURAH", category.getName());
    }

    @Test
    void testDelete() {
        Category category = categoryRepository.findById(4L).orElse(null);
        assertNotNull(category);

        categoryRepository.delete(category);
        assertEquals(Optional.empty(),categoryRepository.findById(1L));
    }

    @Test
    void testQueryMethod() {
        Category category = categoryRepository.findFirstByNameEquals("Category 0").orElse(null);
        assertNotNull(category);
        assertEquals("Category 0", category.getName());

        List<Category> categories = categoryRepository.findAllByNameLike("%Category%");
        assertEquals(10, categories.size());
        assertEquals("Category 0", categories.get(0).getName());
    }
}