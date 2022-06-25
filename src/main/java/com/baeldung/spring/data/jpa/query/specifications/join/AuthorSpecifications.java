package com.baeldung.spring.data.jpa.query.specifications.join;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class AuthorSpecifications {

    public static Specification<Author> hasFirstNameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.<String>get("firstName"), "%" + name + "%");
    }

    public static Specification<Author> hasLastName(String name) {
        return (root, query, cb) -> cb.equal(root.<String>get("lastName"), name);
    }

    public static Specification<Author> hasBookWithTitle(String bookTitle) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, Author> authorsBook = root.join("books");
            return criteriaBuilder.equal(authorsBook.get("title"), bookTitle);
        };
    }

    public static Specification<Author> hasNoTitle(String bookTitle) {
        return (root, query, criteriaBuilder) -> {

            Subquery<Book> bookSubquery = query.subquery(Book.class);

            Root<Book> bookRoot = bookSubquery.from(Book.class);
            bookSubquery
                    .select(bookRoot.get("title"))
                    .distinct(true)
                    .where(
                            criteriaBuilder.equal(bookRoot.get("author"), root),
                            criteriaBuilder.equal(bookRoot.get("title"), bookTitle));



            // RETOURNE DES DOUBLONS !!!!
            return criteriaBuilder.exists(bookSubquery.distinct(true)).not();

        };
    }

    // NE MARCHE PAS
//    public static Specification<Author> hasNoTitle(String bookTitle) {
//        return (root, query, criteriaBuilder) -> {
//            Join<Book, Author> authorsBook = root.join("books");
//
//            Subquery<Book> bookSubquery = query.subquery(Book.class);
//
//            Root<Book> bookRoot = bookSubquery.from(Book.class);
//            bookSubquery
//                    .select(bookRoot)
//                    .where(criteriaBuilder.equal(bookRoot.get("title"), bookTitle));
//
//            return criteriaBuilder.not(criteriaBuilder.exists(bookSubquery));
//        };
//    }

//    public static Specification<Author> hasNoBookWithTitle(String bookTitle) {
//        return (root, query, criteriaBuilder) -> {
//            Join<Book, Author> authorsBook = root.join("books");
//
//
//            Predicate predicate = criteriaBuilder.equal(authorsBook.get("title"), bookTitle);
//
//            return criteriaBuilder.isEmpty(predicate.getExpressions().get(0));
//        };
//    }

//    public static Specification<Author> hasTitle(String bookTitle) {
//        return (root, query, criteriaBuilder) -> {
//            Join<Book, Author> authorsBook = root.join("books");
//
//            Subquery<Book> bookSubquery = query.subquery(Book.class);
//
//            Root<Book> bookRoot = bookSubquery.from(Book.class);
//            bookSubquery
//                    .select(bookRoot)
//                    .where(criteriaBuilder.equal(bookRoot.get("title"), bookTitle));
//
//            return criteriaBuilder.exists(bookSubquery);
//        };
//    }

}