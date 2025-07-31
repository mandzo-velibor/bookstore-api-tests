package com.bookstore.api.utils;

import com.bookstore.api.logger.ApiLogger;
import org.testng.ITestResult;

public class TestUtils {

    public static void handleTestFailure(ITestResult result) {
        String testMethodName = result.getMethod().getMethodName();
        ApiLogger.log("❌ Test failed: " + testMethodName + " - " + result.getThrowable().getMessage());
    }

    public static void finishTest(ITestResult result) {
        String testMethodName = result.getMethod().getMethodName();
        ApiLogger.log("✅ Test passed: " + testMethodName);
    }

}