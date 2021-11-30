package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessUser;
import com.namelessmc.java_api.Notification;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Notifications extends TestStage {

    @Test
    public void adminUserNotificationCount(final @NotNull NamelessAPI api) throws Exception {
        NamelessUser adminUser = api.getUserLazy(1);
        int count = adminUser.getNotificationCount();
        List<Notification> notifications = adminUser.getNotifications();
        assertThat(notifications.size() == count, "notifcation count should be: " + count + ", but was: " + notifications.size());
    }
}
