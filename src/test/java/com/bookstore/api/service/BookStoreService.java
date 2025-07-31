package com.bookstore.api.service;

import com.bookstore.api.base.ApiBase;
import com.bookstore.api.config.ApiConfiguration;
import com.bookstore.api.model.Author;
import com.bookstore.api.model.Book;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class BookStoreService extends ApiBase implements IBookStoreService {

    private final ApiConfiguration config;

    public BookStoreService(ApiConfiguration config) {
        this.config = config;
        RestAssured.filters(new AllureRestAssured());
    }

    @Override
    public Response getAllBooks() {
        return setup(config.getBaseUrl())
                .filter(new AllureRestAssured())
                .when()
                .get(config.getBooksEndpoint());
    }

    @Override
    public Response getBookById(int id) {
        return setup(config.getBaseUrl())
                .filter(new AllureRestAssured())
                .when()
                .get(config.getBooksEndpoint() + "/" + id);
    }

    @Override
    public Response addBook(Book book) {
        return setup(config.getBaseUrl())
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .body(book)
                .when()
                .post(config.getBooksEndpoint());
    }

    @Override
    public Response addBookWithEmptyBody() {
        return setup(config.getBaseUrl())
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .body("")
                .when()
                .post(config.getBooksEndpoint());
    }

    @Override
    public Response updateBook(int id, Book book) {
        return setup(config.getBaseUrl())
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .body(book)
                .when()
                .put(config.getBooksEndpoint() + "/" + id);
    }

    @Override
    public Response deleteBook(int id) {
        return setup(config.getBaseUrl())
                .filter(new AllureRestAssured())
                .when()
                .delete(config.getBooksEndpoint() + "/" + id);
    }

    @Override
    public Response getAllAuthors() {
        return setup(config.getBaseUrl())
                .filter(new AllureRestAssured())
                .when()
                .get(config.getAuthorsEndpoint());
    }

    @Override
    public Response getAuthorById(int id) {
        return setup(config.getBaseUrl())
                .filter(new AllureRestAssured())
                .when()
                .get(config.getAuthorsEndpoint() + "/" + id);
    }

    @Override
    public Response addAuthor(Author author) {
        return setup(config.getBaseUrl())
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .body(author)
                .when()
                .post(config.getAuthorsEndpoint());
    }

    @Override
    public Response addAuthorWithEmptyBody() {
        return setup(config.getBaseUrl())
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .body("")
                .when()
                .post(config.getAuthorsEndpoint());
    }

    @Override
    public Response updateAuthor(int id, Author author) {
        return setup(config.getBaseUrl())
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .body(author)
                .when()
                .put(config.getAuthorsEndpoint() + "/" + id);
    }

    @Override
    public Response deleteAuthor(int id) {
        return setup(config.getBaseUrl())
                .filter(new AllureRestAssured())
                .when()
                .delete(config.getAuthorsEndpoint() + "/" + id);
    }

    @Override
    public Response getAuthorsByBookId(int idBook) {
        return setup(config.getBaseUrl())
                .filter(new AllureRestAssured())
                .when()
                .get(config.getAuthorsEndpoint() + "/authors/books/" + idBook);
    }
}