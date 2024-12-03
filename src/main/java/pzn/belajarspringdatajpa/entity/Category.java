package pzn.belajarspringdatajpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//add entity listener for jpa auditing
@EntityListeners({AuditingEntityListener.class})
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //cause it's one to many, need this annotation
    @OneToMany(mappedBy = "category")
    private List<Product> products;

    //add created date annotation so it can only write once
    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;

    //add last modified date, so it can continuously update if the column being write
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;
}
