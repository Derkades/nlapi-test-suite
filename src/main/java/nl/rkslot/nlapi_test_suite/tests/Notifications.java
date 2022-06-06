package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessUser;
import com.namelessmc.java_api.Notification;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class Notifications extends TestStage {

    @Test
    public void adminUserNotificationCount(final @NonNull NamelessAPI api) throws Exception {
        NamelessUser adminUser = api.userLazy(1);
        int count = adminUser.notificationCount();
        List<Notification> notifications = adminUser.notifications();
        assertThat(notifications.size() == count, "notifcation count should be: " + count + ", but was: " + notifications.size());
    }
}
