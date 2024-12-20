package pzn.belajarspringdatajpa.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pzn.belajarspringdatajpa.entity.Category;
import pzn.belajarspringdatajpa.entity.Product;
import pzn.belajarspringdatajpa.model.SimpleProduct;
import pzn.belajarspringdatajpa.model.SimpleProducts;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

//spec is feature from jpa, where you can query complex and dynamically without query
//first, add extend jpa spec executor and add entity in it.
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    //because Jpa can't handle . like query
    //we need _ for substitute
    List<Product> findAllByCategory_Name(String name);

    //for sorting, just add Sort add the end of parameter
    List<Product> findAllByCategory_Name(String name, Sort sort);

    //using Pageable for paging, you don't need sort when use pageable
    //cause pageable handling sort too.
    //List<Product> findAllByCategory_Name(String name, Pageable pageable);

    //if you want to know page result (total page, total item from query)
    //just change the list, to Page, but the parameter must Pageable
    Page<Product> findAllByCategory_Name(String name, Pageable pageable);

    //count query method, just like using normal query, since we need data
    //from category name, we add _ to join, just use count in front the name of method
    Long countByCategory_Name(String name);

    //to check existing data
    //just use in front the name of method existBy
    boolean existsByName(String name);

    //to delete data
    //just use in front the name of method deleteBy
    //use int because we want to know how many data we delete
    //adding @Transactional, because simple jpa repository for custom query method
    //only use read only, we can't use save/put, delete which need transactional,
    //cause of that we need annotation above.
    @Transactional
    int deleteByName(String name);

    //now in repository we can call the method, without the entity
    //the param need so the program now that param name will be fill with param name from method
    List<Product> searchProductUsingName(@Param("name") String name);

    //add pageable
    List<Product> searchProductUsingNamePageable(@Param("name") String name, Pageable pageable);

    //Query annotation, if the query method to long and need a lot parameters.
    //using @Query will be helpful, in bellow, a lot name is required, it simplify using query.
    @Query(value = "select p from Product p where p.name like :name or p.category.name like :name")
    List<Product> searchProduct(@Param("name") String name);
    //in above we can use 1 parameter but use in 2 like query, for name or category name

    //Query annotation support both Pageable and sort
    @Query(value = "select p from Product p where p.name like :name or p.category.name like :name")
    List<Product> searchProductPageable(@Param("name") String name, Pageable pageable);

    //Query annotation with page result, need to add countQuery
    @Query(
            value = "select p from Product p where p.name like :name or p.category.name like :name",
            countQuery = "select count(p) from Product p where p.name like :name or p.category.name like :name"
    )
    Page<Product> searchProductPageResult(@Param("name") String name, Pageable pageable);

    //@Modifying an annotation that use to inform spring, that this isn't just query
    //but modify query (update or delete)
    @Modifying
    @Query("delete from Product p where p.name = :name")
    int deleteProductUsingName(@Param("name") String name);

    @Modifying
    @Query("update Product p set p.price = 0 where p.id = :id")
    int updateProductPriceToZero(@Param("id") Long id);

    //it can use @Transactional too
    @Transactional
    @Modifying
    @Query("delete from Product p where p.name = :name")
    int deleteProductName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("update Product p set p.price = 0 where p.id = :id")
    int updateProductPriceZero(@Param("id") Long id);

    //cause using find all query in list may crash cause of out of memory
    //we can do find all with stream, which lazy and take it slowly, not load all data
    //just use Stream if you want use stream
    Stream<Product> streamAllByCategory(Category category);

    //spring provide slice, it beyond page, cause it can know what previous and next page is available or not
    //how to use it :
    Slice<Product>  findAllByCategory(Category category, Pageable pageable);
    //cause slice is like page but advance, need param pageable too

    //lock is a method to control how data locked when access database.
    //imagine your db is access by many users, you need something that prevent
    //data mix up and the user get wrong data from what they expected.
    //you just need annotation @Lock and write mode that you need
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findFirstByIdEquals(Long id);

    //how auditing? auditing is process to know createAt, updateAt, who is update the data
    //to audit the db. Please jump to test to see how implement it, the feature will be auto write
    //without we set the data.

    //for spec, you can jump to test too.

    //projection, it is a feature for choose which column from table that needed
    //example, we only need id and name from product so we can do like this :
    List<SimpleProduct> findAllByNameLike(String name);
    //in above, we use interface to determine which column we need from table
    //and yes, it must be interface.

    // If you're wondering why we use an interface, modern Java also provides the 'record' feature.
    // The functionality is similar, but 'record' acts as a class rather than an interface.
    //but it only run in java 17 or newer, if it old, just use projection interface.
    List<SimpleProducts> findAllByNameLike(String name, Sort sort);

    //Dynamic projection, wondering if i have more than 1 record or interface, but need same query method
    //like above we have findAllByNameLike, how i do it? cause it throw error.
    //that's why there is Dynamic Projection, a feature that we can have generic method
    //that will be use later if needed, depend which record/interface that we need
    <T> List<T> findAllByNameLike(String name, Class<T> tClass);

}
