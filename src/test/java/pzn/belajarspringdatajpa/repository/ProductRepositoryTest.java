package pzn.belajarspringdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.support.TransactionOperations;
import pzn.belajarspringdatajpa.entity.Category;
import pzn.belajarspringdatajpa.entity.Product;
import pzn.belajarspringdatajpa.model.ProductPrice;
import pzn.belajarspringdatajpa.model.SimpleProduct;
import pzn.belajarspringdatajpa.model.SimpleProducts;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionOperations transactionOperations;

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

    @Test
    void findProductWithPageable() {
        //for searching page 0
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id")));
        //see don't need sort, cause pageable can handle it.
        //List<Product> products = productRepository.findAllByCategory_Name("Category 0", pageable);
        Page<Product> products = productRepository.findAllByCategory_Name("Category 0",pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Iphone 8", products.getContent().get(0).getName());

        //for searching page 1
        pageable = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")));
        products = productRepository.findAllByCategory_Name("Category 0", pageable);

        assertEquals(1, products.getContent().size());
        assertEquals(1, products.getNumber());
        assertEquals(2, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Iphone 16", products.getContent().get(0).getName());
    }

    @Test
    void testCount() {
        //for function count in this, will be select all without specific param
        Long count = productRepository.count();
        assertEquals(2L, count);

        //on other hand, this count will be counting according what we want:
        count = productRepository.countByCategory_Name("Category 0");
        assertEquals(2L, count);

    }

    @Test
    void testExists() {
        boolean exists = productRepository.existsByName("Iphone 16");
        assertTrue(exists);

        exists = productRepository.existsByName("Iphone 10");
        assertFalse(exists);
    }

    @Test
    void testDeleteOld() {
        //because delete is not read only, we use transactional operation
        //fyi, this operation will run in one transaction
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Category category = categoryRepository.findById(10L).orElse(null);
            assertNotNull(category);

            Product product = new Product();
            product.setName("Iphone 10");
            product.setPrice(25_0000_0000L);
            product.setCategory(category);
            productRepository.save(product);

            int delete = productRepository.deleteByName("Iphone 10");
            assertEquals(1, delete);

            //test not exist
            delete = productRepository.deleteByName("Iphone 10");
            assertEquals(0, delete);
        });
        //if we don't use transaction, the test will fail, cause it need transaction operation
    }

    @Test
    void testDeleteNew() {
        //because we add transactional in repository, we don't need transactional operation again
        //but in this test, we run 3 transactional
        //so there is no roll back, if transaction 1 success, it'll be input even transaction 2and3 failed
        Category category = categoryRepository.findById(10L).orElse(null);
        assertNotNull(category);

        Product product = new Product();
        product.setName("Iphone 10");
        product.setPrice(25_0000_0000L);
        product.setCategory(category);
        productRepository.save(product); //this transaction 1

        int delete = productRepository.deleteByName("Iphone 10"); // this transaction 2
        assertEquals(1, delete);

        //test not exist
        delete = productRepository.deleteByName("Iphone 10"); //transaction 3
        assertEquals(0, delete);
    }

    @Test
    void searchProduct() {
        List<Product> products = productRepository.searchProductUsingName("Iphone 16");

        assertEquals(1, products.size());
        assertEquals("Iphone 16", products.get(0).getName());
    }

    @Test
    void searchProductPageable() {
        Pageable pageable = PageRequest.of(0,1);
        List<Product> products = productRepository.searchProductUsingNamePageable("Iphone 16", pageable);

        assertEquals(1, products.size());
        assertEquals("Iphone 16", products.get(0).getName());
    }

    @Test
    void searchProductLike() {
        List<Product> products = productRepository.searchProduct("%Iphone%");
        assertEquals(2, products.size());

        products = productRepository.searchProduct("%Category%");
        assertEquals(2, products.size());
    }

    @Test
    void searchProductLikePageable() {
        Pageable pageable = PageRequest.of(0,1, Sort.by(Sort.Order.desc("id")));
        List<Product> products = productRepository.searchProductPageable("%Iphone%", pageable);
        assertEquals(1, products.size());

        products = productRepository.searchProductPageable("%Category%", pageable);
        assertEquals(1, products.size());
    }

    @Test
    void searchProductLikePageResult() {
        Pageable pageable = PageRequest.of(0,1, Sort.by(Sort.Order.desc("id")));
        Page<Product> products = productRepository.searchProductPageResult("%Iphone%", pageable);
        assertEquals(1, products.getContent().size());

        assertEquals(2, products.getTotalPages());
        assertEquals(2, products.getTotalElements());
        assertEquals(0, products.getNumber());

        products = productRepository.searchProductPageResult("%Category%", pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(2, products.getTotalPages());
        assertEquals(2, products.getTotalElements());
        assertEquals(0, products.getNumber());
    }

    //because we didn't use @Transactional, now using Transaction Operations
    //for testing modifying
    @Test
    void modifying() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            int total = productRepository.deleteProductUsingName("Wrong");
            assertEquals(0, total);

            total = productRepository.updateProductPriceToZero(1L);
            assertEquals(1, total);

            Product product = productRepository.findById(1L).orElse(null);
            assertNotNull(product);
            assertEquals(0L, product.getPrice());
        });
    }

    //testing modifying using @Transactional
    @Test
    void modifyingTransactional() {

        int total = productRepository.deleteProductName("Wrong");
        assertEquals(0, total);

        total = productRepository.updateProductPriceZero(1L);
        assertEquals(1, total);

        Product product = productRepository.findById(1L).orElse(null);
        assertNotNull(product);
        assertEquals(0L, product.getPrice());
    }

    //testing stream
    @Test
    void stream() {
        //why we use transaction operations ? cause stream is close when transaction isn't active
        //that's why use transaction operations that the cycle still alive (except is close by other)
        //and you can use the data from stream
        //that will happen if do it with annotation @Transactional too
        transactionOperations.executeWithoutResult(transactionStatus -> {
           Category category = categoryRepository.findById(10L).orElse(null);
           assertNotNull(category);

            Stream<Product> stream = productRepository.streamAllByCategory(category);
            //code below will not run, and the test is error, cause the transaction close
            //if you write not with transaction operation or @Transactional
            stream.forEach(product -> System.out.println(product.getId() + " : " + product.getName()));
        });
    }

    //testing slice
    @Test
    void slice() {
        Pageable pageable = PageRequest.of(0,1);

        Category category = categoryRepository.findById(10L).orElse(null);
        assertNotNull(category);

        Slice<Product> slice = productRepository.findAllByCategory(category, pageable);
        //cause slice know is next data available or not, we can use while
        //to find all next data
        while (slice.hasNext()) {
            //slice have next page, to get data from next page
            //if you want previous page, just use previousPageable()
            slice = productRepository.findAllByCategory(category, slice.nextPageable());
        }
    }

    //testing lock, because we use pessimistic write type of lock
    //lock2 will be wait lock1 process.
    //and remember it run in transaction, so need @Transactional or transaction operations
    @Test
    void lock1() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
           try {
               Product product = productRepository.findFirstByIdEquals(1L).orElse(null);
               assertNotNull(product);

               product.setPrice(30_0000_000L);
               Thread.sleep(30_000L);
               productRepository.save(product);
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
        });
    }

    @Test
    void lock2() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
           Product product = productRepository.findFirstByIdEquals(1L).orElse(null);
           assertNotNull(product);
           product.setPrice(10_0000_000L);
           productRepository.save(product);

        });
    }

    //test spec
    @Test
    void specification() {
        // i want to search product name is iphone 16, and 8, how i do it with spec?
        //first, use spec
        Specification<Product> specification = (root, criteria, builder) -> {
            // "root" represents the table (or entity). Here, "root" is the Product table.
            // Using root, we can access columns in the table like "name".

            // "criteria" is used to control the overall query structure,
            // for example, defining SELECT, WHERE, GROUP BY, etc.

            // "builder" is a helper to construct logical conditions in the query
            // (e.g., WHERE, AND, OR clauses).

            assert criteria != null;
            return criteria
                    .where( // Defines the WHERE clause in the query
                            builder.or( // Creates an "OR" condition between two expressions
                                    builder.equal(root.get("name"), "Iphone 16"), // Condition 1: name = "Iphone 16"
                                    builder.equal(root.get("name"), "Iphone 8")   // Condition 2: name = "Iphone 8"
                            )
                    )
                    .getRestriction(); // Returns the built query restrictions
        };

        //find all query from spec
        List<Product> products = productRepository.findAll(specification);
        assertEquals(2, products.size());

    }

    //test projection
    @Test
    void projection() {
        List<SimpleProduct> simpleProducts = productRepository.findAllByNameLike("%Iphone%");
        assertEquals(2, simpleProducts.size());
        //look at the result test, in product there is a lot of column, but when we use
        //projection, only column that determined in interface is select
    }

    //test java record
    @Test
    void record() {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        List<SimpleProducts> simpleProducts = productRepository.findAllByNameLike("%Iphone%",sort);
        assertEquals(2, simpleProducts.size());
    }

    //test dynamic projection
    @Test
    void dynamic() {
        //this is how we implement the dynamic :
        List<SimpleProducts> simpleProducts = productRepository.findAllByNameLike("%Iphone%", SimpleProducts.class);
        assertEquals(2, simpleProducts.size());

        List<ProductPrice> productPrices = productRepository.findAllByNameLike("%Iphone%", ProductPrice.class);
        assertEquals(2, productPrices.size());
    }

}