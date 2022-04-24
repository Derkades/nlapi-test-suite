package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessUser;
import com.namelessmc.java_api.exception.CannotReportSelfException;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Reports extends TestStage {

    @Test
    public void reportSelf(final @NonNull NamelessAPI api) {
        NamelessUser user = api.getUserLazy(1);

        assertShouldThrow(CannotReportSelfException.class, () -> {
            user.createReport(user, "Fake report");
            return null;
        });
    }

    @Test
    public void reportReasonLength(final @NonNull NamelessAPI api) {
        NamelessUser user = api.getUserLazy(1);

        assertShouldThrow(IllegalArgumentException.class, () -> {
            user.createReport(user, "lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate " +
                    "velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
            return null;
        });
    }
}
