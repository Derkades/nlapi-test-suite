package nl.rkslot.nlapi_test_suite;

import com.namelessmc.java_api.NamelessAPI;

public abstract class TestStage {

	public abstract void runTest(NamelessAPI api) throws Exception;

	protected void assertThat(boolean condition) {
		if (!condition) {
			throw new AssertionError("Assertion failed");
		}
	}

	protected void assertThat(boolean condition, String message) {
		if (!condition) {
			throw new AssertionError("Assertion failed: " + message);
		}
	}

}
