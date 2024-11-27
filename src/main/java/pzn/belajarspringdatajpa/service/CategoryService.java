package pzn.belajarspringdatajpa.service;

//using transactional annotation for declarative transactional

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pzn.belajarspringdatajpa.entity.Category;
import pzn.belajarspringdatajpa.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional()
    public void create() {
        for (int i = 0; i < 5; i++) {
            Category category = new Category();
            category.setName("Category " + i);
            categoryRepository.save(category);
        }
        throw new RuntimeException("Ups rollback please");
    }

    //it'll failed, cause AOP can't run in same class
    //the create method will be run, but transactional annotation didn't apply
    public void test() {
        create();
    }
}
