package nl.rkslot.nlapi_test_suite;

import com.namelessmc.java_api.ApiError;
import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessApiBuilder;
import nl.rkslot.nlapi_test_suite.tests.*;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class NamelessApiTest {

    private static final TestStage[] TEST_STAGES = {
            new Announcements(),
            new Groups(),
            new Notifications(),
            new Registration(),
            new WebsiteInfo(),
            new Reports(),
    };

    public static void main(final @NonNull String @NonNull[] args) throws Exception {
        final String apiUrlStr = System.getenv("NAMELESS_API_URL");
        final String apiKey = System.getenv("NAMELESS_API_KEY");

        if (apiUrlStr == null || apiKey == null) {
            System.err.println("Please specify the environment variables: NAMELESS_API_URL, NAMELESS_API_KEY, NAMELESS_DEBUG");
            System.exit(1);
        }

        final URL apiUrl = new URL(apiUrlStr);

        boolean enableDebug = System.getenv("NAMELESS_DEBUG") != null;
        boolean exitOnFail = System.getenv("NAMELESS_EXIT_ON_FAIL") != null;

        long started = Calendar.getInstance().getTimeInMillis();

        final NamelessApiBuilder builder = NamelessAPI.builder(apiUrl, apiKey)
                .userAgent("NamelessApiTestSuite");

        if (enableDebug) {
            builder.withStdErrDebugLogging();
        }

        final NamelessAPI api = builder.build();

        List<TestStage> failedTests = new ArrayList<>();

        for (final @NonNull TestStage testStage : TEST_STAGES) {
            System.out.println("----------------- Starting test class: " + testStage.getClass().getSimpleName() + " -----------------");

            Arrays.stream(testStage.getClass().getDeclaredMethods())
                    .filter(m -> m.getName().equals("beforeRun"))
                    .findFirst()
                    .ifPresent(m -> {
                            try {
                                if (m.getParameterCount() == 1 && m.getParameterTypes()[0].equals(NamelessAPI.class)) {
                                    m.invoke(testStage, api);
                                } else {
                                    m.invoke(testStage);
                                }
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                    });

            List<Method> testMethods = Arrays.stream(testStage.getClass().getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(Test.class))
                    .collect(Collectors.toList());

            if (testMethods.isEmpty()) {
                System.out.println("⚠️  No tests found in " + testStage.getClass().getSimpleName());
            } else {
                boolean allSuccess = true;
                for (Method testMethod : testMethods) {
                    final long startTest = Calendar.getInstance().getTimeInMillis();

                    if (testMethod.getParameterCount() != 1) {
                        System.err.println("Skipped test method " + testMethod.getName() + " with invalid signature");
                        allSuccess = false;
                        continue;
                    }

                    System.out.println("⏳ Starting test: " + testMethod.getName() + "");

                    boolean pass;
                    try {
                        testMethod.invoke(testStage, api);
                        pass = true;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        pass = false;
                    } catch (InvocationTargetException e) {
                        if (e.getCause() instanceof AssertionError || e.getCause() instanceof ApiError) {
                            // print the assertion error or api error directly, don't waste screen space on the whole stacktrace
                            e.getCause().printStackTrace();
                        } else {
                            e.printStackTrace();
                        }
                        pass = false;
                    }

                    final long finishedTest = Calendar.getInstance().getTimeInMillis();
                    final long taken = finishedTest - startTest;

                    if (pass) {
                        System.out.printf("✅ Test passed: %s (took %s ms)\n", testMethod.getName(), taken);
                    } else {
                        System.out.printf("❌ Test failed: %s (took %s ms)\n", testMethod.getName(), taken);
                        allSuccess = false;
                        if (exitOnFail) {
                            System.exit(1);
                        }
                    }
                }
                if (!allSuccess) {
                    failedTests.add(testStage);
                }
            }

            System.out.println();
        }

        long ended = Calendar.getInstance().getTimeInMillis();

        System.out.println("---------------------- All tests completed ----------------------");
        System.out.println("➡️  Failed tests: " + failedTests.stream().map(s -> s.getClass().getSimpleName()).collect(Collectors.joining(", ")));
        System.out.println("➡️  Made " + TestStage.getAssertions() + " assertions");
        System.out.println("➡️  Took " + (ended - started) + "ms");

        System.exit(failedTests.isEmpty() ? 0 : 1);
    }

}
