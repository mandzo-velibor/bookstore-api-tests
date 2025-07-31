package com.bookstore.api.utils;

import com.bookstore.api.logger.ApiLogger;
import org.apache.commons.io.output.TeeOutputStream;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ApiTestConfig implements ISuiteListener {

    private PrintStream originalOut;
    private FileOutputStream fileOut;
    private TeeOutputStream teeStream;
    private PrintStream teePrintStream;

    @Override
    public void onStart(ISuite suite) {
        ApiLogger.log("🛠️ Starting API Test Suite setup...");
        ApiLogger.log(" ");
        try {
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }
            String consoleLogPath = "reports/api-console.log";
            fileOut = new FileOutputStream(consoleLogPath, true); // true za append
            teeStream = new TeeOutputStream(System.out, fileOut);
            teePrintStream = new PrintStream(teeStream, true, StandardCharsets.UTF_8);
            originalOut = System.out;
            System.setOut(teePrintStream);
        } catch (IOException e) {
            ApiLogger.log("Error setting up log: " + e.getMessage());
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        ApiLogger.log("✅ Test suite finished.");
        if (teePrintStream != null) {
            teePrintStream.flush();
        }
        if (fileOut != null) {
            try {
                fileOut.flush();
                fileOut.close();
            } catch (IOException e) {
                ApiLogger.log("Error closing log file: " + e.getMessage());
            }
        }
        if (teeStream != null) {
            try {
                teeStream.close();
            } catch (IOException e) {
                ApiLogger.log("Error closing tee stream: " + e.getMessage());
            }
        }
        if (originalOut != null) {
            System.setOut(originalOut);
        }
    }
}