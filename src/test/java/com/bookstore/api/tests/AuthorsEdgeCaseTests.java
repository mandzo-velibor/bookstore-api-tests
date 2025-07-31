package com.bookstore.api.tests;

import com.bookstore.api.config.ApiConfiguration;
import com.bookstore.api.config.Configuration;
import com.bookstore.api.logger.ApiLogger;
import com.bookstore.api.model.Author;
import com.bookstore.api.service.BookStoreService;
import com.bookstore.api.service.IBookStoreService;
import com.bookstore.api.utils.Constants;
import com.bookstore.api.utils.TestUtils;
import io.restassured.response.Response;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;
import static com.bookstore.api.utils.TestUtils.getCurrentTestMethodName;
import static org.testng.Assert.assertEquals;

public class AuthorsEdgeCaseTests {
    private final IBookStoreService service;

    @Factory
    public static Object[] createInstances() {
        String baseUrl = Configuration.getBaseUrl();
        String booksEndpoint = Configuration.getBooksEndpoint();
        String authorsEndpoint = Configuration.getAuthorsEndpoint();
        ApiConfiguration config = new com.bookstore.api.config.ApiConfiguration(
                baseUrl,
                booksEndpoint,
                authorsEndpoint
        );
        return new Object[]{new AuthorsEdgeCaseTests(new BookStoreService(config))};
    }

    public AuthorsEdgeCaseTests(IBookStoreService service) {
        this.service = service;
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testGetAuthorWithInvalidId() {
        ApiLogger.log("🏁 Starting test: testGetAuthorWithInvalidId");
        Response response = service.getAuthorById(Constants.INVALID_ID);
        ApiLogger.log("👤 Attempting to get author with invalid ID: " + Constants.INVALID_ID);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 404, "Should return 400 for invalid ID");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testGetAuthorWithNonExistentId() {
        ApiLogger.log("🏁 Starting test: testGetAuthorWithNonExistentId");
        Response response = service.getAuthorById(Constants.NON_EXISTENT_ID);
        ApiLogger.log("👤 Attempting to get author with non-existent ID: " + Constants.NON_EXISTENT_ID);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 404, "Should return 404 for non-existent ID");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testAddAuthorWithEmptyBody() {
        ApiLogger.log("🏁 Starting test: testAddAuthorWithEmptyBody");
        Response response = service.addAuthorWithEmptyBody();
        ApiLogger.log("➕ Attempting to add author with empty body");
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 400, "Should return 400 for empty body");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testAddAuthorWithInvalidIdBook() {
        ApiLogger.log("🏁 Starting test: testAddAuthorWithInvalidIdBook");
        Author invalidAuthor = new Author(0, Constants.INVALID_ID, "John", "Doe");
        Response response = service.addAuthor(invalidAuthor);
        ApiLogger.log("➕ Attempting to add author with invalid idBook: ");
        ApiLogger.logAuthor(invalidAuthor);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 400, "Should return 400 for invalid idBook");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testUpdateAuthorWithNonExistentId() {
        ApiLogger.log("🏁 Starting test: testUpdateAuthorWithNonExistentId");
        Author author = new Author(Constants.NON_EXISTENT_ID, Constants.VALID_BOOK_ID, "John", "Doe");
        Response response = service.updateAuthor(Constants.NON_EXISTENT_ID, author);
        ApiLogger.log("✏️ Attempting to update non-existent author with ID: " + Constants.NON_EXISTENT_ID);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 404, "Should return 404 for non-existent ID");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testDeleteAuthorWithNonExistentId() {
        ApiLogger.log("🏁 Starting test: testDeleteAuthorWithNonExistentId");
        Response response = service.deleteAuthor(Constants.NON_EXISTENT_ID);
        ApiLogger.log("🗑️ Attempting to delete author with non-existent ID: " + Constants.NON_EXISTENT_ID);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 404, "Should return 404 for non-existent ID");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "authors", "edge"})
    public void testAuthorsBoundaryIds() {
        ApiLogger.log("🏁 Starting test: testAuthorsBoundaryIds");
        Response response = service.getAllAuthors();
        List<Author> authors = Arrays.asList(response.as(Author[].class));
        int minId = authors.stream().mapToInt(Author::getId).min().orElse(0);
        int maxId = authors.stream().mapToInt(Author::getId).max().orElse(0);
        ApiLogger.log("📊 Testing authors boundaries: minId=" + minId + ", maxId=" + maxId);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 200, "Should return 200 OK");
            Response minMinusOne = service.getAuthorById(minId - 1);
            assertEquals(minMinusOne.getStatusCode(), 404, "Should return 404 for ID below min");
            Response maxPlusOne = service.getAuthorById(maxId + 1);
            assertEquals(maxPlusOne.getStatusCode(), 404, "Should return 404 for ID above max");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }
}