package pzn.belajarspringdatajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
