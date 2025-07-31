package com.bookstore.api.utils;

import com.bookstore.api.logger.ApiLogger;
import org.apache.commons.io.output.TeeOutputStream;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ApiTestConfig implements ISuiteListener {

    private PrintStream originalOut;

    @BeforeSuite
    public void onStart(ISuite suite) {
        ApiLogger.log("🛠️ Starting API Test Suite setup...");
        ApiLogger.log(" ");
        try {
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }
            String consoleLogPath = "reports/api-console.log";
            FileOutputStream fileOut = new FileOutputStream(consoleLogPath);
            TeeOutputStream teeStream = new TeeOutputStream(System.out, fileOut);
            PrintStream teePrintStream = new PrintStream(teeStream, true, StandardCharsets.UTF_8);
            originalOut = System.out;
            System.setOut(teePrintStream);
        } catch (IOException e) {
            ApiLogger.log("Error setting up log: " + e.getMessage());
        }
    }

    @AfterSuite
    public void onFinish(ISuite suite) {
        ApiLogger.log("✅ Test suite finished.");
        if (originalOut != null) {
            System.setOut(originalOut);
        }
        try {
            Path allureResultsDir = Paths.get("target/allure-results");
            if (!Files.exists(allureResultsDir)) {
                Files.createDirectories(allureResultsDir);
                ApiLogger.log("📁 Created allure-results directory");
            }
        } catch (IOException e) {
            ApiLogger.log("Error creating allure-results directory: " + e.getMessage());
        }
    }
}