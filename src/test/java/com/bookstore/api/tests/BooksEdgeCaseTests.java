package com.bookstore.api.tests;

import com.bookstore.api.config.ApiConfiguration;
import com.bookstore.api.config.Configuration;
import com.bookstore.api.logger.ApiLogger;
import com.bookstore.api.model.Book;
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

public class BooksEdgeCaseTests {
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
        return new Object[]{new BooksEdgeCaseTests(new BookStoreService(config))};
    }

    public BooksEdgeCaseTests(IBookStoreService service) {
        this.service = service;
    }

    @Test(groups = {"regression", "books", "edge"})
    public void testGetBookWithInvalidId() {
        ApiLogger.log("🏁 Starting test: testGetBookWithInvalidId");
        ApiLogger.log("📖 Attempting to get book with invalid ID: " + Constants.INVALID_ID);
        Response response = service.getBookById(Constants.INVALID_ID);
        ApiLogger.log("Response: " + response.getBody().asString());
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 404, "Should return 404 for invalid ID");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "books", "edge"})
    public void testUpdateBookWithNonExistentId() {
        ApiLogger.log("🏁 Starting test: testUpdateBookWithNonExistentId");
        Book book = new Book(Constants.NON_EXISTENT_ID, "Non-existent Book", 100, "desc", "exc", "2025-07-30T00:00:00");
        ApiLogger.log("✏️ Attempting to update non-existent book with ID: " + Constants.NON_EXISTENT_ID);
        Response response = service.updateBook(Constants.NON_EXISTENT_ID, book);
        ApiLogger.log("Response: " + response.getBody().asString());
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 404, "Should return 404 for non-existent ID");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "books", "edge"})
    public void testDeleteBookWithNonExistentId() {
        ApiLogger.log("🏁 Starting test: testDeleteBookWithNonExistentId");
        ApiLogger.log("🗑️ Attempting to delete book with non-existent ID: " + Constants.NON_EXISTENT_ID);
        Response response = service.deleteBook(Constants.NON_EXISTENT_ID);
        ApiLogger.log("Response: " + response.getBody().asString());
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 404, "Should return 404 for non-existent ID");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "books", "edge"})
    public void testAddBookWithEmptyBody() {
        ApiLogger.log("🏁 Starting test: testAddBookWithEmptyBody");
        ApiLogger.log("➕ Attempting to add book with empty body");
        Response response = service.addBookWithEmptyBody();
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 400);
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "books", "edge"})
    public void testBooksBoundaryIds() {
        ApiLogger.log("🏁 Starting test: testBooksBoundaryIds");
        Response response = service.getAllBooks();
        List<Book> books = Arrays.asList(response.as(Book[].class)) ;
        int minId = books.stream().mapToInt(Book::getId).min().orElse(0);
        int maxId = books.stream().mapToInt(Book::getId).max().orElse(0);
        ApiLogger.log("📊 Testing books boundaries: minId=" + minId + ", maxId=" + maxId);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 200, "Should return 200 OK");
            Response minMinusOne = service.getBookById(minId - 1);
            assertEquals(minMinusOne.getStatusCode(), 404, "Should return 404 for ID below min");
            Response maxPlusOne = service.getBookById(maxId + 1);
            assertEquals(maxPlusOne.getStatusCode(), 404, "Should return 404 for ID above max");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }
}