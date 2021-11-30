package nl.rkslot.nlapi_test_suite;

public abstract class TestStage {

    // TODO: assertThrows(Class<? extends Throwable>, Runnable)
    // TODO: reinstallNameless(String pathToCliInstallFile)

    private static int assertions = 0;

    public static int getAssertions() {
        return assertions;
    }

    protected void assertThat(final boolean condition) {
        assertions++;

        if (!condition) {
            throw new AssertionError("Assertion failed");
        }
    }

    protected void assertThat(final boolean condition, final String message) {
        assertions++;

        if (!condition) {
            throw new AssertionError("Assertion failed: " + message);
        }
    }
}
