package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.Group;
import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessException;
import com.namelessmc.java_api.NamelessUser;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Groups extends TestStage {

    @Test
    public void groupsExist(final @NonNull NamelessAPI api) throws NamelessException {
        assertThat(api.group("Admin").size() == 1, "admin group should exist");
        assertThat(api.group("Moderator").size() == 1, "moderator group should exist");
        assertThat(api.group("Does not exist").size() == 0, "non-existing group should not exist");
    }

    @Test
    public void memberGroupHasCorrectName(final @NonNull NamelessAPI api) throws NamelessException {
        Group memberGroup = api.group(1);
        assertThat(memberGroup != null, "member group should exist");
        assertThat(memberGroup.getName().equals("Member"), "name of member group should be 'Member'");
    }

    @Test
    public void addRemoveUserGroups(final @NonNull NamelessAPI api) throws NamelessException {
        NamelessUser adminUser = api.user(1);
        assertThat(adminUser != null, "admin user should exist");
        Group memberGroup = api.group(1);
        assertThat(memberGroup != null, "member group should exist");

        adminUser.addGroups(memberGroup);
        assertThat(adminUser.groups().contains(memberGroup), "admin user should have member group after adding it, but it has groups: " + adminUser.groups());
        adminUser.removeGroups(memberGroup);
        assertThat(!adminUser.groups().contains(memberGroup), "admin user should not have member group after removing it, but it has groups: " + adminUser.groups());
    }
}
