package pzn.belajarspringdatajpa.service;

//using transactional annotation for declarative transactional

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionOperations;
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

    //if the transaction need to be run async, @Transactional can't be use
    //so we need code transaction manually, like this :

    //need autowired this
    @Autowired
    private TransactionOperations transactionOperations;

    //code transaction error manually
    public void error() {
        throw new RuntimeException("Ups");
    }

    //code transaction manually using transaction operations
    public void createCategories() {
        //we can use execute if need call a result, but we don't need result
        //so need execute without result
        transactionOperations.executeWithoutResult(transactionStatus -> {
            for (int i = 0; i < 5; i++) {
                Category category = new Category();
                category.setName("Category " + i);
                categoryRepository.save(category);
            }
            error();
        });
    }

    //this will be very manual transaction, using transaction manager
    @Autowired
    private PlatformTransactionManager transactionManager;

    public void manual() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setTimeout(10);
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            for (int i = 0; i < 5; i++) {
                Category category = new Category();
                category.setName("Category " + i);
                categoryRepository.save(category);
            }
            error();
            transactionManager.commit(status);
        } catch (Throwable e) {
            transactionManager.rollback(status);
            throw new RuntimeException(e);
        }
    }
}
