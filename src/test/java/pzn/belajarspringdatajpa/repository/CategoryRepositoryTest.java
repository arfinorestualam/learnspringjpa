package pzn.belajarspringdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
    //example, example is feature from jpa that can be use make query from example entity that we made
    //is simple rather than using query, but if the data is a lot and complex it's not efficient
    //this is how it make :
    @Test
    void example() {
        Category category = new Category();
        category.setName("Category 0");

        Example<Category> example = Example.of(category);
        List<Category> categories = categoryRepository.findAll(example);
        assertEquals(2, categories.size());
    }

    //example matcher, matching custom data that we want
    @Test
    void exampleMatcher() {
        Category category = new Category();
        category.setName("category 0");

        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase();

        Example<Category> example = Example.of(category, exampleMatcher);
        List<Category> categories = categoryRepository.findAll(example);
        assertEquals(2, categories.size());
    }
}