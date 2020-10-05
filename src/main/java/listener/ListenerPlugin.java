package listener;


import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import utils.TestInitialization;

import java.time.LocalDateTime;

public class ListenerPlugin implements ConcurrentEventListener {

    public void onTestRunStarted(TestRunStarted testRunStarted) {
        TestInitialization.init();
        System.out.println("TEST RUN STARTED AT: " + LocalDateTime.now().toString());
    }

    public void onTestCaseStarted(TestCaseStarted testCaseStarted) {
        System.out.println("TEST CASE STARTED: " + testCaseStarted.getTestCase().getName());
    }


    public void onTestRunFinished(TestRunFinished testRunFinished) {
        System.out.println("TEST RUN FINISHED AT: " + LocalDateTime.now().toString());
    }

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestRunStarted.class, this::onTestRunStarted);
        eventPublisher.registerHandlerFor(TestRunFinished.class, this::onTestRunFinished);
        eventPublisher.registerHandlerFor(TestCaseStarted.class, this::onTestCaseStarted);
    }
}
