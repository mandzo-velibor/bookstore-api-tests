package com.bookstore.api.tests;

import com.bookstore.api.base.ApiBase;
import com.bookstore.api.config.ApiConfiguration;
import com.bookstore.api.config.Configuration;
import com.bookstore.api.logger.ApiLogger;
import com.bookstore.api.model.Author;
import com.bookstore.api.model.Book;
import com.bookstore.api.service.BookStoreService;
import com.bookstore.api.service.IBookStoreService;
import com.bookstore.api.utils.Constants;
import com.bookstore.api.utils.TestUtils;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import static com.bookstore.api.utils.TestUtils.getCurrentTestMethodName;
import static org.testng.Assert.*;

public class AuthorTests extends ApiBase {
    private final IBookStoreService service;
    private final Faker faker;

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
        return new Object[]{new AuthorTests(new BookStoreService(config))};
    }

    public AuthorTests(IBookStoreService service) {
        this.service = service;
        this.faker = new Faker();
    }

    @Test(groups = {"smoke", "authors", "regression"})
    public void testListAllAuthors() {
        ApiLogger.log("🏁 Starting test: testListAllAuthors");
        Response response = service.getAllAuthors();
        List<Author> authors = Arrays.asList(response.as(Author[].class));
        int totalAuthors = authors.size();
        ApiLogger.log("📊 Total number of authors: " + totalAuthors);
        if (!authors.isEmpty()) {
            int firstFiveCount = Math.min(5, authors.size());
            List<Author> firstFive = authors.subList(0, firstFiveCount);
            ApiLogger.log("📋 First 5 authors:");
            firstFive.forEach(ApiLogger::logAuthor);

            int lastFiveCount = Math.min(5, authors.size());
            int lastFiveStart = Math.max(0, authors.size() - 5);
            List<Author> lastFive = authors.subList(lastFiveStart, lastFiveStart + lastFiveCount);
            ApiLogger.log("📋 Last 5 authors:");
            lastFive.forEach(ApiLogger::logAuthor);
        }
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 200, "Should return 200 OK");
            assertFalse(authors.isEmpty(), "Author list should not be empty");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"smoke", "authors"})
    public void testGetAuthorById() {
        ApiLogger.log("🏁 Starting test: testGetAuthorById");
        int authorId = Constants.VALID_AUTHOR_ID;
        Response response = service.getAuthorById(authorId);
        Author author = response.as(Author.class);
        ApiLogger.log("👤 Retrieved author: ");
        ApiLogger.logAuthor(author);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 200);
            assertEquals(author.getId(), authorId, "Author ID should match requested ID");
            assertNotNull(author.getLastName(), "Author last name should not be null");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "authors"})
    public void testAuthorsSortedByLastName() {
        ApiLogger.log("🏁 Starting test: testAuthorsSortedByLastName");
        Response response = service.getAllAuthors();
        List<Author> authors = Arrays.asList(response.as(Author[].class));
        authors = authors.stream().sorted(Comparator.comparing(Author::getLastName)).toList();
        int totalAuthors = authors.size();
        ApiLogger.log("📊 Total number of authors: " + totalAuthors);
        if (!authors.isEmpty()) {
            int firstFiveCount = Math.min(5, authors.size());
            List<Author> firstFive = authors.subList(0, firstFiveCount);
            ApiLogger.log("📋 First 5 authors:");
            firstFive.forEach(ApiLogger::logAuthor);

            int lastFiveCount = Math.min(5, authors.size());
            int lastFiveStart = Math.max(0, authors.size() - 5);
            List<Author> lastFive = authors.subList(lastFiveStart, lastFiveStart + lastFiveCount);
            ApiLogger.log("📋 Last 5 authors:");
            lastFive.forEach(ApiLogger::logAuthor);
        }
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 200, "Should return 200 OK");
            assertTrue(authors.size() > 1, "Should have at least two authors to sort");
            for (int i = 0; i < authors.size() - 1; i++) {
                assertTrue(authors.get(i).getLastName().compareTo(authors.get(i + 1).getLastName()) <= 0,
                        "Authors should be sorted by last name");
            }
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "authors"})
    public void testAddAuthor() {
        ApiLogger.log("🏁 Starting test: testAddAuthor");
        Author newAuthor = new Author(0, Constants.VALID_BOOK_ID, faker.name().firstName(), faker.name().lastName());
        Response response = service.addAuthor(newAuthor);
        Author createdAuthor = response.as(Author.class);
        ApiLogger.log("➕ Created author: ");
        ApiLogger.logAuthor(createdAuthor);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 200);
            assertEquals(createdAuthor.getLastName(), newAuthor.getLastName(), "Author last name should match");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "authors"})
    public void testUpdateAuthor() {
        ApiLogger.log("🏁 Starting test: testUpdateAuthor");
        Author newAuthor = new Author(Constants.NON_EXISTENT_ID, Constants.VALID_BOOK_ID, faker.name().firstName(), faker.name().lastName());
        Response addResponse = service.addAuthor(newAuthor);
        newAuthor.setLastName(faker.name().lastName());
        Response response = service.updateAuthor(newAuthor.getId(), newAuthor);
        Author updatedAuthor = response.as(Author.class);
        ApiLogger.log("✏️ Updated author: ");
        ApiLogger.logAuthor(updatedAuthor);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(addResponse.getStatusCode(), 200);
            assertEquals(response.getStatusCode(), 200);
            assertEquals(updatedAuthor.getLastName(), newAuthor.getLastName(), "Updated last name should match");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "authors"})
    public void testDeleteAuthorAndVerify() {
        ApiLogger.log("🏁 Starting test: testDeleteAuthorAndVerify");
        Author newAuthor = new Author(Constants.NON_EXISTENT_ID, Constants.VALID_BOOK_ID, faker.name().firstName(), faker.name().lastName());
        Response addResponse = service.addAuthor(newAuthor);
        Response deleteResponse = service.deleteAuthor(newAuthor.getId());
        ApiLogger.log("🗑️ Deleted author with ID: " + newAuthor.getId());
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(addResponse.getStatusCode(), 200);
            assertEquals(deleteResponse.getStatusCode(), 200);
            Response getResponse = service.getAuthorById(newAuthor.getId());
            assertEquals(getResponse.getStatusCode(), 404, "Author should not exist after deletion");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "authors"})
    public void testAuthorsByBookId() {
        int bookId = Constants.VALID_BOOK_ID;
        ApiLogger.log("🏁 Starting test: testAuthorsByBookId " + bookId);
        Response bookResponse = service.getBookById(bookId);
        Book book = bookResponse.as(Book.class);
        ApiLogger.logBook(book);

        Response response = service.getAuthorsByBookId(bookId);
        List<Author> authors = Arrays.asList(response.as(Author[].class));
        authors.forEach(ApiLogger::logAuthor);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(bookResponse.getStatusCode(), 200);
            assertEquals(response.getStatusCode(), 200);
            assertFalse(authors.isEmpty(), "Should return at least one author for book ID " + bookId);
            assertTrue(authors.stream().allMatch(author -> author.getIdBook() == bookId),
                    "All authors should have idBook " + bookId);
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }
}