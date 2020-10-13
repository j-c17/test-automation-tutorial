package listener;


import client.web.SeleniumDriverManager;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.TestInitialization;
import utils.TestReport;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;


public class ListenerPlugin implements ConcurrentEventListener {

    private static final Logger LOG = LogManager.getLogger(ListenerPlugin.class);
    private static final TestReport testReport = new TestReport();

    public void onTestRunStarted(TestRunStarted testRunStarted) {
        TestInitialization.init();
        LOG.info(String.format("Test run started at: %s", LocalDateTime.now().toString()));
    }

    public void onTestCaseStarted(TestCaseStarted testCaseStarted) {
        String testClass = Paths.get(testCaseStarted.getTestCase().getUri()).getFileName().toString();
        String testName = testCaseStarted.getTestCase().getName();
        LOG.info(String.format("Test case started: %s", testName));
        testReport.createTest(testClass, testName);
    }

    public void onTestCaseFinished(TestCaseFinished testCaseFinished) {
        LOG.info(String.format("Test case %s finished", testCaseFinished.getTestCase().getName()));
    }

    public void onTestRunFinished(TestRunFinished testRunFinished) {
        SeleniumDriverManager.closeAllDrivers();
        TestReport.closeThreadLocalCollections();
    }

    public void onTestStepFinished(TestStepFinished testStepFinished) {
        List<String> tags = testStepFinished.getTestCase().getTags();
        if (tags.contains("@web")) {
            RemoteWebDriver driver = SeleniumDriverManager.getCurrentDriver();
            if (driver != null && driver.getSessionId() != null) {
                testReport.log(testStepFinished.getTestStep().getCodeLocation());
                testReport.logImage(SeleniumDriverManager.getCurrentDriver().getScreenshotAs(OutputType.BASE64));
            }
        }
    }

    public void onPassedTest(TestCaseFinished testCaseFinished) {
        String testName = testCaseFinished.getTestCase().getName();
        LOG.info(String.format("Pass test %s", testName));
        testReport.pass(testName);
    }

    public void onSkippedTest(TestCaseFinished testCaseFinished) {
        String testName = testCaseFinished.getTestCase().getName();
        String reason = testCaseFinished.getResult().getError().getMessage();
        LOG.info(String.format("Skip test %s", testName));
        testReport.skip(testName, reason);
    }

    public void onFailedTest(TestCaseFinished testCaseFinished) {
        String testName = testCaseFinished.getTestCase().getName();
        String reason = testCaseFinished.getResult().getError().getMessage();
        LOG.info(String.format("Failed test %s", testName));
        List<String> tags = testCaseFinished.getTestCase().getTags();
        for (String tag : tags) {
            if (tag.toLowerCase().startsWith("@xfail")) {

                testReport.xfail(testName, reason);
                return;
            }
        }

        testReport.fail(testName, reason);
    }

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestRunStarted.class, this::onTestRunStarted);
        eventPublisher.registerHandlerFor(TestStepFinished.class, this::onTestStepFinished);
        eventPublisher.registerHandlerFor(TestCaseStarted.class, this::onTestCaseStarted);
        eventPublisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);

        eventPublisher.registerHandlerFor(TestCaseFinished.class, event -> {
            switch (event.getResult().getStatus()) {
                case PASSED:
                    onPassedTest(event);
                    break;
                case SKIPPED:
                    onSkippedTest(event);
                    break;
                case FAILED:
                    onFailedTest(event);
                    break;
                default:
                    break;
            }
        });

        eventPublisher.registerHandlerFor(TestRunFinished.class, this::onTestRunFinished);
    }
}
