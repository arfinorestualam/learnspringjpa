package pzn.belajarspringdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pzn.belajarspringdatajpa.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
