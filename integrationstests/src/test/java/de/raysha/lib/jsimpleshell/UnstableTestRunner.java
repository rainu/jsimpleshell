package de.raysha.lib.jsimpleshell;

import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * Our tests are sometimes unstable. In the most cases a restart of a tests
 * solve the problem. This test runner, retry a test if it failed. After
 * x tries the test will finally marked as failed.
 *
 * @author rainu
 */
public class UnstableTestRunner extends BlockJUnit4ClassRunner {
	private static final int RETRY_COUNT = 5;

    private int failedAttempts = 0;

    public UnstableTestRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    public void run(final RunNotifier notifier) {
        EachTestNotifier testNotifier = new EachTestNotifier(notifier,
                getDescription());
        Statement statement = classBlock(notifier);
        try {

            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            testNotifier.fireTestIgnored();
        } catch (StoppedByUserException e) {
            throw e;
        } catch (Throwable e) {
            retry(testNotifier, statement, e);
        }
    }

    @Override
    protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
        Description description = describeChild(method);
        if (method.getAnnotation(Ignore.class) != null) {
            notifier.fireTestIgnored(description);
        } else {
            runTestUnit(methodBlock(method), description, notifier);
        }
    }

    /**
     * Runs a {@link Statement} that represents a leaf (aka atomic) test.
     */
    protected final void runTestUnit(Statement statement, Description description,
            RunNotifier notifier) {
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        eachNotifier.fireTestStarted();
        try {
            statement.evaluate();
        } catch (AssumptionViolatedException e) {
            eachNotifier.addFailedAssumption(e);
        } catch (Throwable e) {
            retry(eachNotifier, statement, e);
        } finally {
            eachNotifier.fireTestFinished();
        }
    }

    public void retry(EachTestNotifier notifier, Statement statement, Throwable currentThrowable) {
        Throwable caughtThrowable = currentThrowable;
        while (RETRY_COUNT > failedAttempts) {
            try {
                statement.evaluate();
                return;
            } catch (Throwable t) {
                failedAttempts++;
                caughtThrowable = t;
            }
        }
        notifier.addFailure(caughtThrowable);
    }
}
