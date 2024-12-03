package pzn.belajarspringdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//add this dependency so it enable the jpa auditing
@EnableJpaAuditing
public class BelajarSpringDataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BelajarSpringDataJpaApplication.class, args);
    }

}
