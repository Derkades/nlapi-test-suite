package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessUser;
import com.namelessmc.java_api.exception.UsernameAlreadyExistsException;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Registration extends TestStage {

    @Test
    public void registerDuplicateUsername(final @NonNull NamelessAPI api) throws Exception {
        NamelessUser adminUser = api.getUserLazy(1);
        String username = adminUser.getUsername();
        // TODO check if mc integration is enabled & add uuid param as needed. this fails if mc integration is enabled
        try {
            api.registerUser(username, "irrelevant@email.com");
            throw new AssertionError("creating a user with duplicate username should fail");
        } catch (UsernameAlreadyExistsException ignored) {
        }
    }

}
