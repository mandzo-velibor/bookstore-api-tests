package com.bookstore.api.utils;

import com.bookstore.api.logger.ApiLogger;
import org.testng.Assert;

public class TestUtils {

    public static void handleTestFailure(AssertionError e, String testMethodName) {

        ApiLogger.log("❌ Test failed: " + testMethodName + " - " + e.getMessage());
        ApiLogger.log("-------------");
        Assert.fail(e.getMessage());
    }

    public static void finishTest(String testMethodName) {

        ApiLogger.log("✅ Test passed: " + testMethodName );
        ApiLogger.log("-------------");
    }

    public static String getCurrentTestMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}