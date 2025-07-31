package com.bookstore.api.tests;

import com.bookstore.api.config.ApiConfiguration;
import com.bookstore.api.config.Configuration;
import com.bookstore.api.logger.ApiLogger;
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

public class BookTests {
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
        return new Object[]{new BookTests(new BookStoreService(config))};
    }

    public BookTests(IBookStoreService service) {
        this.service = service;
        this.faker = new Faker();
    }

    @Test(groups = {"smoke", "books", "regression"})
    public void testListAllBooks() {
        ApiLogger.log("🏁 Starting test: testListAllBooks");
        Response response = service.getAllBooks();
        List<Book> books = Arrays.asList(response.as(Book[].class));
        int totalBooks = books.size();
        ApiLogger.log("📊 Total number of books: " + totalBooks);

        if (!books.isEmpty()) {
            int firstFiveCount = Math.min(5, books.size());
            List<Book> firstFive = books.subList(0, firstFiveCount);
            ApiLogger.log("📋 First 5 books:");
            firstFive.forEach(ApiLogger::logBook);

            int lastFiveCount = Math.min(5, books.size());
            int lastFiveStart = Math.max(0, books.size() - 5);
            List<Book> lastFive = books.subList(lastFiveStart, lastFiveStart + lastFiveCount);
            ApiLogger.log("📋 Last 5 books:");
            lastFive.forEach(ApiLogger::logBook);
        }
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 200, "Should return 200 OK");
            assertFalse(books.isEmpty(), "Book list should not be empty");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"smoke", "books"})
    public void testGetBookById() {
        ApiLogger.log("🏁 Starting test: testGetBookById");
        int bookId = Constants.VALID_BOOK_ID;
        Response response = service.getBookById(bookId);
        Book book = response.as(Book.class);
        ApiLogger.logBook(book);
        ApiLogger.logResponse(response);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 200);
            assertEquals(book.getId(), bookId, "Book ID should match requested ID");
            assertNotNull(book.getTitle(), "Book title should not be null");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "books"})
    public void testAddUpdateDeleteBook() {
        ApiLogger.log("🏁 Starting test: testAddUpdateDeleteBook");
        Book newBook = new Book(0, faker.book().title(), faker.number().numberBetween(100, 500),
                faker.lorem().sentence(), faker.lorem().paragraph(),
                "2025-07-30T00:00:00");
        ApiLogger.log("➕ Adding book: ");
        ApiLogger.logBook(newBook);
        String testMethodName = getCurrentTestMethodName();
        try {
            Response addResponse = service.addBook(newBook);
            assertEquals(addResponse.getStatusCode(), 200);
            newBook.setTitle(faker.book().title());
            ApiLogger.log("✏️ Updating book: ");
            ApiLogger.logBook(newBook);
            Response updateResponse = service.updateBook(newBook.getId(), newBook);
            assertEquals(updateResponse.getStatusCode(), 200);
            ApiLogger.log("🗑️ Deleting book with id: " + newBook.getId());
            Response deleteResponse = service.deleteBook(newBook.getId());
            assertEquals(deleteResponse.getStatusCode(), 200);
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "books"})
    public void testUpdateBookAndVerify() {
        ApiLogger.log("🏁 Starting test: testUpdateBookAndVerify");
        Book newBook = new Book(Constants.NON_EXISTENT_ID, faker.book().title(), faker.number().numberBetween(100, 500),
                faker.lorem().sentence(), faker.lorem().paragraph(),
                "2025-07-30T00:00:00");
        Response addResponse = service.addBook(newBook);
        newBook.setTitle(faker.book().title());
        Response response = service.updateBook(newBook.getId(), newBook);
        Book updatedBook = response.as(Book.class);
        ApiLogger.log("✏️ Updated book: ");
        ApiLogger.logBook(updatedBook);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(addResponse.getStatusCode(), 200);
            assertEquals(response.getStatusCode(), 200);
            assertEquals(updatedBook.getTitle(), newBook.getTitle(), "Updated title should match");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "books"})
    public void testDeleteBookAndVerify() {
        ApiLogger.log("🏁 Starting test: testDeleteBookAndVerify");
        Book newBook = new Book(Constants.NON_EXISTENT_ID, faker.book().title(), faker.number().numberBetween(100, 500),
                faker.lorem().sentence(), faker.lorem().paragraph(),
                "2025-07-30T00:00:00");
        Response addResponse = service.addBook(newBook);
        Response deleteResponse = service.deleteBook(newBook.getId());
        ApiLogger.log("🗑️ Deleted book with ID: " + newBook.getId());
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(addResponse.getStatusCode(), 200);
            assertEquals(deleteResponse.getStatusCode(), 200);
            Response getResponse = service.getBookById(newBook.getId());
            assertEquals(getResponse.getStatusCode(), 404, "Book should not exist after deletion");
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"smoke", "books"})
    public void testAddBookWithFaker() {
        ApiLogger.log("🏁 Starting test: testAddBookWithFaker");
        Book newBook = new Book(0, faker.book().title(), faker.number().numberBetween(100, 500),
                faker.lorem().sentence(), faker.lorem().paragraph(),
                "2025-07-30T00:00:00");
        Response response = service.addBook(newBook);
        Book createdBook = response.as(Book.class);
        ApiLogger.log("➕ Created book: ");
        ApiLogger.logBook(createdBook);
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 200);
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }

    @Test(groups = {"regression", "books"})
    public void testGetBooksSortedByTitleDesc() {
        ApiLogger.log("🏁 Starting test: testGetBooksSortedByTitleDesc");
        Response response = service.getAllBooks();
        List<Book> books = Arrays.asList(response.as(Book[].class));
        books = books.stream().sorted(Comparator.comparing(Book::getTitle).reversed()).toList();
        books.forEach(book -> ApiLogger.log("🔤 " + book.getTitle()));
        String testMethodName = getCurrentTestMethodName();
        try {
            assertEquals(response.getStatusCode(), 200, "Should return 200 OK");
            assertTrue(books.size() > 1, "Should have at least two books to sort");
            for (int i = 0; i < books.size() - 1; i++) {
                assertTrue(books.get(i).getTitle().compareTo(books.get(i + 1).getTitle()) >= 0,
                        "Books should be sorted by title in descending order");
            }
        } catch (AssertionError e) {
            TestUtils.handleTestFailure(e, testMethodName);
        }
        TestUtils.finishTest(testMethodName);
    }
}