package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessUser;
import com.namelessmc.java_api.exception.ApiError;
import com.namelessmc.java_api.exception.ApiException;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Registration extends TestStage {

    @Test
    public void registerDuplicateUsername(final @NonNull NamelessAPI api) throws Exception {
        NamelessUser adminUser = api.getUserLazy(1);
        String username = adminUser.getUsername();
        try {
            api.registerUser(username, "irrelevant@email.com");
        } catch (ApiException e) {
            if (e.apiError() == ApiError.CORE_USERNAME_ALREADY_EXISTS) {
                return;
            }
        }

        throw new AssertionError("creating a user with duplicate username should fail");
    }

}
