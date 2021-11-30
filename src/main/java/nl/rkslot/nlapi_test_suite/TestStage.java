package nl.rkslot.nlapi_test_suite;

public abstract class TestStage {

	protected void assertThat(final boolean condition) {
		if (!condition) {
			throw new AssertionError("Assertion failed");
		}
	}

	protected void assertThat(final boolean condition, final String message) {
		if (!condition) {
			throw new AssertionError("Assertion failed: " + message);
		}
	}
}
