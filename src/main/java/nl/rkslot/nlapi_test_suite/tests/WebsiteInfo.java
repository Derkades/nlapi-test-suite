package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.jetbrains.annotations.NotNull;

public class WebsiteInfo extends TestStage {

    @Test
    public void test_language_matches_English_UK(final @NotNull NamelessAPI api) throws Exception {
        assertThat(api.getWebsite().getLanguage().equals("EnglishUK"));
    }

    @Test
    public void test_module_count_is_correct(final @NotNull NamelessAPI api) throws Exception {
        assertThat(api.getWebsite().getModules().length > 0);
    }

}
