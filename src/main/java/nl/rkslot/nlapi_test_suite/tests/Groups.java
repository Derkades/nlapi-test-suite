package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.Group;
import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessException;
import com.namelessmc.java_api.NamelessUser;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.jetbrains.annotations.NotNull;

public class Groups extends TestStage {

	@Test
	public void test_groups_exist(final @NotNull NamelessAPI api) throws NamelessException {
		assertThat(api.getGroup("Admin").size() == 1, "admin group should exist");
		assertThat(api.getGroup("Moderator").size() == 1, "moderator group should exist");
		assertThat(api.getGroup("Does not exist").size() == 0, "non-existing group should not exist");
	}

	@Test
	public void test_member_group_has_correct_name(final @NotNull NamelessAPI api) throws NamelessException {
		Group memberGroup = api.getGroup(1).orElseThrow();
		assertThat(memberGroup.getName().equals("Member"), "name of member group should be 'Member'");
	}

	@Test
	public void test_adding_and_removing_groups_from_user(final @NotNull NamelessAPI api) throws NamelessException {
		NamelessUser adminUser = api.getUser(1).orElseThrow();
		Group memberGroup = api.getGroup(1).orElseThrow();

		adminUser.addGroups(memberGroup);
		assertThat(adminUser.getGroups().contains(memberGroup), "admin user should have member group after adding it, but it has groups: " + adminUser.getGroups());
		adminUser.removeGroups(memberGroup);
		assertThat(!adminUser.getGroups().contains(memberGroup), "admin user should not have member group after removing it, but it has groups: " + adminUser.getGroups());
	}
}
