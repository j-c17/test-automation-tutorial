package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class TestReport {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> currentTest;
    private static ThreadLocal<ExtentTest> currentTestClass;
    private static Set<String> testCases;

    public static void init() {
        extent = new ExtentReports();
        currentTest = new ThreadLocal<>();
        currentTestClass = new ThreadLocal<>();
        testCases = new HashSet<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String fileName = "raport-" + LocalDateTime.now().format(formatter) + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(Paths.get( fileName).toFile())

                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[]{ViewName.TEST, ViewName.DASHBOARD, ViewName.EXCEPTION}).apply();
        extent.attachReporter(spark);
    }

    public synchronized void createTest(String testCase, String testName) {
        if (testCases.add(testCase)) {
            ExtentTest test = extent.createTest(testCase);
            currentTestClass.set(test);
        }

        ExtentTest node = currentTestClass.get().createNode(testName);
        currentTest.set(node);
        extent.flush();
    }

    public void pass(String testName) {
        currentTest.get().pass("PASSED: " + testName);
        currentTest.remove();
        extent.flush();
    }

    public void xfail(String testName, String reason) {
        currentTest.get().assignCategory("XFAIL");
        skip(testName, reason);
    }

    public void skip(String testName, String reason) {
        currentTest.get().skip("SKIPPED: " + testName);
        currentTest.get().skip(reason);
        currentTest.remove();
        extent.flush();
    }

    public void fail(String testName, String reason) {
        currentTest.get().fail("FAILED: " + testName);
        currentTest.get().fail(reason);
        currentTest.remove();
        extent.flush();
    }

    public void logImage(String base64Image) {
        String message = "<img src=\"data:image/png;base64, " + base64Image + "\" width=\"100%\" />";
        currentTest.get().log(Status.INFO, message);
    }

    public void log(String message) {
        currentTest.get().log(Status.INFO, message);
        extent.flush();
    }

    public static void closeThreadLocalCollections() {
        currentTest.remove();
        currentTestClass.remove();
    }
}