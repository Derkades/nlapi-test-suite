package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.jetbrains.annotations.NotNull;

public class Announcements extends TestStage {

    @Test
    public void test_returns_correct_announcements(final @NotNull NamelessAPI api) throws Exception {
        assertThat(api.getAnnouncements().size() == 0, "Announcements should be empty, got: " + api.getAnnouncements().size());

        // TODO: add tests/api endpoint for creating announcements
    }

}
