package pzn.belajarspringdatajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pzn.belajarspringdatajpa.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

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

    //Query annotation with page result
    @Query(
            value = "select p from Product p where p.name like :name or p.category.name like :name",
            countQuery = "select count(p) from Product p where p.name like :name or p.category.name like :name"
    )
    Page<Product> searchProductPageResult(@Param("name") String name, Pageable pageable);

}
