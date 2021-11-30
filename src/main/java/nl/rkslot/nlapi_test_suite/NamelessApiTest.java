package nl.rkslot.nlapi_test_suite;

import com.namelessmc.java_api.ApiError;
import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessApiBuilder;
import nl.rkslot.nlapi_test_suite.tests.Announcements;
import nl.rkslot.nlapi_test_suite.tests.Groups;
import nl.rkslot.nlapi_test_suite.tests.Notifications;
import nl.rkslot.nlapi_test_suite.tests.Registration;
import nl.rkslot.nlapi_test_suite.tests.Reports;
import nl.rkslot.nlapi_test_suite.tests.WebsiteInfo;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class NamelessApiTest {

    private static final @NotNull TestStage[] TEST_STAGES = {
            new Announcements(),
            new Groups(),
            new Notifications(),
            new Registration(),
            new WebsiteInfo(),
            new Reports(),
    };

    public static void main(String @NotNull [] args) throws Exception {
        if (args.length == 0 || args.length > 2) {
            System.err.println("Usage: [api url] [enable debug]");
            System.exit(1);
        }

        long started = Calendar.getInstance().getTimeInMillis();

        final @NotNull NamelessApiBuilder builder = NamelessAPI.builder()
                .userAgent("NamelessApiTestSuite")
                .apiUrl(args[0]);

        if (args.length == 2 && Boolean.parseBoolean(args[1])) {
            builder.withStdErrDebugLogging();
        }

        final @NotNull NamelessAPI api = builder.build();

        List<TestStage> failedTests = new ArrayList<>();

        for (final @NotNull TestStage testStage : TEST_STAGES) {
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

                    boolean pass = true;

                    try {
                        testMethod.invoke(testStage, api);
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
                        allSuccess = false;
                    }

                    final long finishedTest = Calendar.getInstance().getTimeInMillis();
                    final long taken = finishedTest - startTest;

                    if (pass) {
                        System.out.printf("✅ Test passed: %s (took %s ms)\n", testMethod.getName(), taken);
                    } else {
                        System.out.printf("❌ Test failed: %s (took %s ms)\n", testMethod.getName(), taken);
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
    }

}
