package pzn.belajarspringdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pzn.belajarspringdatajpa.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    //make query method in repository
    //JPA QL support query with their structure, like this :

    Optional<Category> findFirstByNameEquals(String name);
    //the method from above, will be translated to query (that's why the name is query method),
    // so the method name will be strict with the documentation,
    // make sure the name is match with the documentation
    //fyi : the Equals in last name method above is necessary,
    //cause it'll be translate equal to

    List<Category> findAllByNameLike(String name);
}
