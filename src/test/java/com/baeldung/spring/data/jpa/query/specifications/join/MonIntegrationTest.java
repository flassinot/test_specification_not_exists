package com.baeldung.spring.data.jpa.query.specifications.join;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static com.baeldung.spring.data.jpa.query.specifications.join.AuthorSpecifications.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MonIntegrationTest {

    @Autowired
    private AuthorsRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void beforeEach() {
        saveTestData();
    }

    @Test
    public void whenSearchingByBookTitleAndAuthorName_thenOneAuthorIsReturned() {

        Specification<Author> specification = hasLastName("Martin").and(hasBookWithTitle("Clean Code"));

        List<Author> authors = repository.findAll(specification);

        assertThat(authors).hasSize(1);
    }

    @Test
    public void montest_author_with_no_title() {

        // TEST JPQL
        List<Book> result = entityManager.createQuery(
                        "select author from Author author" +
                                " where author.id not in (" +
                                "  select book.author.id from Book book" +
                                "  where book.author.id = author.id" +
                                "  and book.title = 'Clean Code')")
                .getResultList();

        List<Object> resultsNative = entityManager.createNativeQuery(
                "select * from Book book"
        ).getResultList();


        Specification<Author> specification = hasNoTitle("Clean Code");

        List<Author> authors = repository.findAll(specification);

        assertThat(authors).hasSize(1);
    }

//    @Test
//    public void montest_author_with_title() {
//
//        Specification<Author> specification = hasTitle("Clean Code");
//
//        List<Author> authors = repository.findAll(specification);
//
//        assertThat(authors).hasSize(0);
//    }

    private void saveTestData() {
        Author uncleBob = new Author();
        uncleBob.setFirstName("Robert");
        uncleBob.setLastName("Martin");
        repository.save(uncleBob);

        Book book1 = new Book();
        book1.setTitle("Clean Code");
        book1.setAuthor(uncleBob);
        Book book2 = new Book();
        book2.setTitle("Clean Architecture");
        book2.setAuthor(uncleBob);
        bookRepository.save(book1);
        bookRepository.save(book2);

        Author me = new Author();
        me.setFirstName("me");
        me.setLastName("me");
        repository.save(me);

        Book bookMe1 = new Book();
        bookMe1.setTitle("blabla");
        bookMe1.setAuthor(me);
        Book bookMe2 = new Book();
        bookMe2.setTitle("bliblibi");
        bookMe2.setAuthor(me);
        bookRepository.save(bookMe1);
        bookRepository.save(bookMe2);

    }
}
