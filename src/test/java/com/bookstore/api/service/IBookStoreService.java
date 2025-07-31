package com.bookstore.api.service;

import com.bookstore.api.model.Author;
import com.bookstore.api.model.Book;
import io.restassured.response.Response;

public interface IBookStoreService {
    Response getAllBooks();
    Response getBookById(int bookId);
    Response addBook(Book book);
    Response addBookWithEmptyBody();
    Response updateBook(int bookId, Book book);
    Response deleteBook(int bookId);
    Response getAllAuthors();
    Response getAuthorById(int authorId);
    Response addAuthor(Author author);
    Response addAuthorWithEmptyBody();
    Response updateAuthor(int authorId, Author author);
    Response deleteAuthor(int authorId);
    Response getAuthorsByBookId(int bookId);
}