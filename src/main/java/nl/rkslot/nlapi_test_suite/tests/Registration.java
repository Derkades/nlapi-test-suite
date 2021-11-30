package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessUser;
import com.namelessmc.java_api.exception.UsernameAlreadyExistsException;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.jetbrains.annotations.NotNull;

public class Registration extends TestStage {

	@Test
	public void test_cannot_create_user_with_duplicate_username(final @NotNull NamelessAPI api) throws Exception {
		NamelessUser adminUser = api.getUserLazy(1);
		String username = adminUser.getUsername();
		try {
			api.registerUser(username, "irrelevant@email.com");
			throw new AssertionError("creating a user with duplicate username should fail");
		} catch (UsernameAlreadyExistsException ignored) {}
	}

}