package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessException;
import com.namelessmc.java_api.NamelessUser;
import com.namelessmc.java_api.exception.AlreadyHasOpenReportException;
import com.namelessmc.java_api.exception.CannotReportSelfException;
import com.namelessmc.java_api.exception.ReportUserBannedException;
import com.namelessmc.java_api.exception.UnableToCreateReportException;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.jetbrains.annotations.NotNull;

public class Reports extends TestStage {

    @Test
    public void test_cannot_make_a_report_for_self(final @NotNull NamelessAPI api) {
        NamelessUser user = api.getUserLazy(1);

        boolean thrown = false;

        try {
            user.createReport(user, "Fake report");
        } catch (NamelessException | ReportUserBannedException | UnableToCreateReportException | AlreadyHasOpenReportException e) {
            e.printStackTrace();
        } catch (CannotReportSelfException e) {
            thrown = true;
        }

        assertThat(thrown, "expected CannotReportSelfException to be thrown");
    }

    @Test
    public void test_cannot_exceed_report_length(final @NotNull NamelessAPI api) {
        NamelessUser user = api.getUserLazy(1);

        boolean thrown = false;

        try {
            user.createReport(user, "lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate " +
                    "velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        } catch (NamelessException | ReportUserBannedException | UnableToCreateReportException | AlreadyHasOpenReportException | CannotReportSelfException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        assertThat(thrown, "expected IllegalStateException to be thrown");
    }
}
