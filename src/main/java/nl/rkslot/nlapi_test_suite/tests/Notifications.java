package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessUser;
import com.namelessmc.java_api.Notification;
import nl.rkslot.nlapi_test_suite.TestStage;

import java.util.List;

public class Notifications extends TestStage {

	@Override
	public void runTest(NamelessAPI api) throws Exception {
		NamelessUser adminUser = api.getUserLazy(1);
		int count = adminUser.getNotificationCount();
		List<Notification> notifications = adminUser.getNotifications();
		assertThat(notifications.size() == count);
		System.out.println("Admin user has " + count + " notifications");
	}

}
