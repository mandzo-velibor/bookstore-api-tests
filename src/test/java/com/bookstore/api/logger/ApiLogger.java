package com.bookstore.api.logger;

import com.bookstore.api.model.Book;
import com.bookstore.api.model.Author;
import io.restassured.response.Response;

public class ApiLogger {

    public static void log(String message) {
        System.out.println(message);
    }

    public static void logBook(Book book) {
        String formatted = String.format(
                """
                        📚 Book ID: %d
                           Title: %s
                           Pages: %d
                           Description: %s
                           Excerpt: %s
                           Publish Date: %s""",
                book.getId(),
                book.getTitle(),
                book.getPageCount(),
                truncate(book.getDescription(), 50),
                truncate(book.getExcerpt(), 50),
                book.getPublishDate()

        );
        log(formatted);
    }

    public static void logAuthor(Author author) {
        String formatted = String.format(
                """
                        👤 Author ID: %d
                           Book ID: %d
                           First Name: %s
                           Last Name: %s""",
                author.getId(),
                author.getIdBook(),
                author.getFirstName(),
                author.getLastName()
        );
        log(formatted);
    }

    public static void logResponse(Response response) {
        log(String.format("📡 HTTP Response - Status: %d\n   Body: %s",
                response.getStatusCode(), truncate(response.getBody().asString(), 100)));
    }

    private static String truncate(String text, int maxLength) {
        if (text == null) return "null";
        return text.length() <= maxLength ? text : text.substring(0, maxLength - 3) + "...";
    }
}