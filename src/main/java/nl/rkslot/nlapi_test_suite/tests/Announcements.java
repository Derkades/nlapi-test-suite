package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessUser;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.jetbrains.annotations.NotNull;

public class Announcements extends TestStage {

    // TODO: Add tests for announcements
    public void runTest(final @NotNull NamelessAPI api) throws Exception {
        api.getAnnouncements();
        NamelessUser admin = api.getUser(1).orElseThrow();
        api.getAnnouncements(admin);
    }

}
