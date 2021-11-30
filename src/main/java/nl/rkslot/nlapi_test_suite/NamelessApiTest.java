package nl.rkslot.nlapi_test_suite;

import com.namelessmc.java_api.ApiError;
import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessApiBuilder;
import nl.rkslot.nlapi_test_suite.tests.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class NamelessApiTest {

	private static final @NotNull TestStage[] TEST_STAGES = {
			new Announcements(),
			new Groups(),
			new Notifications(),
			new Registration(),
			new WebsiteInfo(),
	};

	public static void main(String @NotNull [] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Usage: [api url] [enable debug]");
			System.exit(1);
		}

		final @NotNull String apiUrl = args[0];

		final @NotNull NamelessApiBuilder builder = NamelessAPI.builder()
				.userAgent("NamelessApiTestSuite")
				.apiUrl(apiUrl);

		if (args.length == 2 && Boolean.parseBoolean(args[1])) {
			builder.withStdErrDebugLogging();
		}

		final @NotNull NamelessAPI api = builder.build();

		AtomicInteger testCount = new AtomicInteger();
		AtomicInteger passed = new AtomicInteger();

		for (final @NotNull TestStage testStage : TEST_STAGES) {
			System.out.println("----------------- Starting test class: " + testStage.getClass().getSimpleName() + " -----------------");

			List<Method> tests = Arrays.stream(testStage.getClass().getMethods()).filter(method -> {
						return method.isAnnotationPresent(Test.class)
								&& method.getParameterCount() == 1
								&& method.getName().startsWith("test_");
					}).collect(Collectors.toList());

			if (tests.isEmpty()) {
				System.out.println("⚠️  No tests found in " + testStage.getClass().getSimpleName());
			} else {
				tests.forEach(method -> {
					testCount.getAndIncrement();
					final @NotNull String testName = getTestName(method.getName());

					System.out.println("⏳ Starting test: " + testName + "");

					boolean pass = true;

					try {
						method.invoke(testStage, api);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						if (e.getCause() instanceof AssertionError || e.getCause() instanceof ApiError) {
							// print the assertion error or api error directly, don't waste screen space on the whole stacktrace
							e.getCause().printStackTrace();
						} else {
							e.printStackTrace();
						}
						pass = false;
					}

					if (pass) {
						System.out.println("✅ Test passed: " + testName);
						passed.getAndIncrement();
					} else {
						System.out.println("❌ Test failed: " + testName);
					}
				});
			}

			System.out.println();
		}

		System.out.println("----------------- All tests completed -----------------");
		System.out.println("➡️  Ran " + testCount + " tests");
		System.out.println("➡️  " + passed + " tests passed");
	}

	private static @NotNull String getTestName(@NotNull String methodName) {
		return methodName.replace("test", "").replaceFirst("_", "").replace("_", " ");
	}

}
