package com.bookstore.api.tests;

import com.bookstore.api.config.ApiConfiguration;
import com.bookstore.api.config.Configuration;
import com.bookstore.api.logger.ApiLogger;
import com.bookstore.api.model.Author;
import com.bookstore.api.service.BookStoreService;
import com.bookstore.api.service.IBookStoreService;
import com.bookstore.api.utils.Constants;
import io.restassured.response.Response;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;
import static org.testng.Assert.assertEquals;


public class AuthorsEdgeCaseTests {
    private final IBookStoreService service;

    @Factory
    public static Object[] createInstances() {
        String baseUrl = Configuration.getBaseUrl();
        String booksEndpoint = Configuration.getBooksEndpoint();
        String authorsEndpoint = Configuration.getAuthorsEndpoint();
        ApiConfiguration config = new ApiConfiguration(baseUrl, booksEndpoint, authorsEndpoint);
        return new Object[]{new AuthorsEdgeCaseTests(new BookStoreService(config))};
    }

    public AuthorsEdgeCaseTests(IBookStoreService service) {
        this.service = service;
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testGetAuthorWithInvalidId() {
        ApiLogger.log("👤 Attempting to get author with invalid ID: " + Constants.INVALID_ID);
        Response response = service.getAuthorById(Constants.INVALID_ID);

        assertEquals(response.getStatusCode(), 404, "Should return 400 for invalid ID");
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testGetAuthorWithNonExistentId() {
        ApiLogger.log("👤 Attempting to get author with non-existent ID: " + Constants.NON_EXISTENT_ID);
        Response response = service.getAuthorById(Constants.NON_EXISTENT_ID);

        assertEquals(response.getStatusCode(), 404, "Should return 404 for non-existent ID");
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testAddAuthorWithEmptyBody() {
        ApiLogger.log("➕ Attempting to add author with empty body");
        Response response = service.addAuthorWithEmptyBody();

        assertEquals(response.getStatusCode(), 400, "Should return 400 for empty body");
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testAddAuthorWithInvalidIdBook() {
        Author invalidAuthor = new Author(0, Constants.INVALID_ID, "John", "Doe");
        ApiLogger.log("➕ Attempting to add author with invalid idBook: ");
        ApiLogger.logAuthor(invalidAuthor);
        Response response = service.addAuthor(invalidAuthor);

        assertEquals(response.getStatusCode(), 400, "Should return 400 for invalid idBook");
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testUpdateAuthorWithNonExistentId() {
        Author author = new Author(Constants.NON_EXISTENT_ID, Constants.VALID_BOOK_ID, "John", "Doe");
        ApiLogger.log("✏️ Attempting to update non-existent author with ID: " + Constants.NON_EXISTENT_ID);
        Response response = service.updateAuthor(Constants.NON_EXISTENT_ID, author);

        assertEquals(response.getStatusCode(), 404, "Should return 404 for non-existent ID");
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testDeleteAuthorWithNonExistentId() {
        ApiLogger.log("🗑️ Attempting to delete author with non-existent ID: " + Constants.NON_EXISTENT_ID);
        Response response = service.deleteAuthor(Constants.NON_EXISTENT_ID);

        assertEquals(response.getStatusCode(), 404, "Should return 404 for non-existent ID");
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testAuthorsBoundaryIds() {
        Response response = service.getAllAuthors();
        List<Author> authors = Arrays.asList(response.as(Author[].class));
        int minId = authors.stream().mapToInt(Author::getId).min().orElse(0);
        int maxId = authors.stream().mapToInt(Author::getId).max().orElse(0);
        ApiLogger.log("📊 Testing authors boundaries: minId=" + minId + ", maxId=" + maxId);

        assertEquals(response.getStatusCode(), 200, "Should return 200 OK");
        Response minMinusOne = service.getAuthorById(minId - 1);
        assertEquals(minMinusOne.getStatusCode(), 404, "Should return 404 for ID below min");
        Response maxPlusOne = service.getAuthorById(maxId + 1);
        assertEquals(maxPlusOne.getStatusCode(), 404, "Should return 404 for ID above max");
    }
}