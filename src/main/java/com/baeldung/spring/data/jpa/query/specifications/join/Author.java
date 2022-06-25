package com.baeldung.spring.data.jpa.query.specifications.join;

import lombok.Data;

import javax.persistence.*;

import java.util.List;

@Entity
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="author")
    private List<Book> books;

}
