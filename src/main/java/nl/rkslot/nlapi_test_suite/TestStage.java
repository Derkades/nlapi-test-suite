package nl.rkslot.nlapi_test_suite;

import java.util.concurrent.Callable;

public abstract class TestStage {

    // TODO: reinstallNameless(String pathToCliInstallFile)

    private static int assertions = 0;

    public static int getAssertions() {
        return assertions;
    }

    protected void assertThat(final boolean condition, final String message) {
        assertions++;

        if (!condition) {
            throw new AssertionError("Assertion failed: " + message);
        }
    }

    protected void assertShouldThrow(final Class<? extends Throwable> expectedException, final Callable<Void> callable) {
        assertions++;

        try {
            callable.call();
        } catch (Exception e) {
            if (e.getClass().equals(expectedException)) {
                return;
            }

            throw new AssertionError("Expected exception " + expectedException.getName() + " but got " + e.getClass().getName());
        }

        throw new AssertionError("Expected exception " + expectedException.getName() + " but no exception was thrown");
    }

}
