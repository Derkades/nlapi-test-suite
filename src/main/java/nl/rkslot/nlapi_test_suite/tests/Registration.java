package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessUser;
import com.namelessmc.java_api.exception.UsernameAlreadyExistsException;
import nl.rkslot.nlapi_test_suite.TestStage;

public class Registration extends TestStage {

	@Override
	public void runTest(NamelessAPI api) throws Exception {
		NamelessUser adminUser = api.getUserLazy(1);
		String username = adminUser.getUsername();
		try {
			api.registerUser(username, "irrelevant@email.com");
			throw new AssertionError("creating a user with duplicate username should fail");
		} catch (UsernameAlreadyExistsException ignored) {}
	}

}
