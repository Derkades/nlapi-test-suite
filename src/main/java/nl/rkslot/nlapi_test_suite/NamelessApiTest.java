package nl.rkslot.nlapi_test_suite;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessApiBuilder;
import nl.rkslot.nlapi_test_suite.tests.Groups;
import nl.rkslot.nlapi_test_suite.tests.WebsiteInfo;

public class NamelessApiTest {

	private static final TestStage[] TEST_STAGES = {
//			new Announcements(),
			new Groups(),
			new WebsiteInfo(),
	};

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: [api url] [enable debug]");
			System.exit(1);
		}

		String apiUrl = args[0];
		boolean enableDebug = Boolean.parseBoolean(args[1]);

		NamelessApiBuilder builder = NamelessAPI.builder()
				.apiUrl(apiUrl);
		if (enableDebug) {
			builder.withStdErrDebugLogging();
		}

		NamelessAPI api = builder.build();

		for (TestStage testStage : TEST_STAGES) {
			System.out.println("----------------- Starting test: " + testStage.getClass().getSimpleName() + " -----------------");
			testStage.runTest(api);
		}
	}

}
