CREATE DATABASE belajar_spring_data_jpa;

USE belajar_spring_data_jpa;

CREATE TABLE categories
(
    id BIGINT   NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL ,
    PRIMARY KEY (id)
)ENGINE  InnoDB;

SELECT * FROM products;

CREATE TABLE products (
    id      BIGINT      NOT NULL    AUTO_INCREMENT,
    name    VARCHAR(100)    NOT NULL,
    price   BIGINT          NOT NULL,
    category_id BIGINT      NOT NULL,
    primary key (id),
    foreign key fk_products_categories (category_id) REFERENCES categories (id)
) ENGINE InnoDB;

ALTER TABLE categories ADD COLUMN created_date TIMESTAMP;
ALTER TABLE categories ADD COLUMN last_modified_date TIMESTAMP;