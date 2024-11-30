package pzn.belajarspringdatajpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//need this annotation to tell the class it's entity
@Entity
//must have this annotation to refer name of table in the db
@Table(name = "products")
//named query
@NamedQueries({
        //insert the entity if we want to use it
        @NamedQuery(name = "Product.searchProductUsingName",
                query = "SELECT p FROM Product p WHERE p.name = :name" //ORDER BY p.name DESC
        ),
        @NamedQuery(name = "Product.searchProductUsingNamePageable",
                query = "SELECT p FROM Product p WHERE p.name = :name" //ORDER BY p.name DESC
        )
        //named query didn't support sorting, but it support pageable without sort
        //if you want sort, you can add it manually in named query, like the example above
        //that's why using named query can't be flexible.
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long price;

    //need this annotation because it's refer to category
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
}
