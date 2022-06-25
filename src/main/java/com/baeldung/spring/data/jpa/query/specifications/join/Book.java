package com.baeldung.spring.data.jpa.query.specifications.join;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name="AUTHOR_ID", nullable=false)
    private Author author;

}
