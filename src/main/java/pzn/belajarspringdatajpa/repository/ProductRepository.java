package pzn.belajarspringdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pzn.belajarspringdatajpa.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //because Jpa can't handle . like query
    //we need _ for substitute
    List<Product> findAllByCategory_Name(String name);
}
