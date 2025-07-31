package com.bookstore.api.utils;

import com.bookstore.api.logger.ApiLogger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestExecutionListener extends TestListenerAdapter {

    @Override
    public void onTestStart(ITestResult result) {
        String testMethodName = result.getMethod().getMethodName();
        ApiLogger.log("🏁 Starting test: " + testMethodName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        TestUtils.finishTest(result);
        ApiLogger.log("-------------");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        TestUtils.handleTestFailure(result); // Uklanjamo Assert.fail, samo logiramo
        ApiLogger.log("-------------");
    }
}