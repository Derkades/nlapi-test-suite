package nl.rkslot.nlapi_test_suite;

import com.namelessmc.java_api.ApiError;
import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessApiBuilder;
import nl.rkslot.nlapi_test_suite.tests.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
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
            new Reports(),
    };

    public static void main(String @NotNull [] args) throws Exception {
        if (args.length < 1) {
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

        AtomicInteger testCount = new AtomicInteger();
        AtomicInteger testsPassed = new AtomicInteger();

        for (final @NotNull TestStage testStage : TEST_STAGES) {
            System.out.println("----------------- Starting test class: " + testStage.getClass().getSimpleName() + " -----------------");

            Arrays.stream(testStage.getClass().getDeclaredMethods()).filter(m -> {
                return m.getName().equals("beforeRun");
            }).findFirst().ifPresent(m -> {
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

            List<Method> tests = Arrays.stream(testStage.getClass().getDeclaredMethods()).filter(method -> {
                return method.isAnnotationPresent(Test.class)
                        && method.getParameterCount() == 1
                        && method.getName().startsWith("test_");
            }).collect(Collectors.toList());

            if (tests.isEmpty()) {
                System.out.println("⚠️  No tests found in " + testStage.getClass().getSimpleName());
            } else {
                tests.forEach(method -> {
                    final long startTest = Calendar.getInstance().getTimeInMillis();
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

                    final long finishedTest = Calendar.getInstance().getTimeInMillis();
                    final long taken = finishedTest - startTest;

                    if (pass) {
                        System.out.println("✅ Test passed: " + testName + " (" + taken + "ms)");
                        testsPassed.getAndIncrement();
                    } else {
                        System.out.println("❌ Test failed: " + testName + " (" + taken + "ms)");
                    }
                });
            }

            System.out.println();
        }

        long ended = Calendar.getInstance().getTimeInMillis();

        System.out.println("---------------------- All tests completed ----------------------");
        System.out.println("➡️  " + testsPassed + "/" + testCount + " tests passed");
        System.out.println("➡️  Made " + TestStage.getAssertions() + " assertions");
        System.out.println("➡️  Took " + (ended - started) + "ms");
    }

    private static @NotNull String getTestName(@NotNull String methodName) {
        return methodName.replace("test", "").replaceFirst("_", "").replace("_", " ");
    }

}
