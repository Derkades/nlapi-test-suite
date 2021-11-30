package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.jetbrains.annotations.NotNull;

public class WebsiteInfo extends TestStage {

    private static final @NotNull String NAMELESS_VERSION = "2.0.0-pr12";

    @Test
    public void test_language_matches_English_UK(final @NotNull NamelessAPI api) throws Exception {
        assertThat(api.getWebsite().getLanguage().equals("EnglishUK"), "expected EnglishUK, got " + api.getWebsite().getLanguage());
    }

    @Test
    public void test_module_count_is_correct(final @NotNull NamelessAPI api) throws Exception {
        assertThat(api.getWebsite().getModules().length == 4, "expected 4 modules installed by default, got " + api.getWebsite().getModules().length);
    }

    @Test
    public void test_website_version_is_correct(final @NotNull NamelessAPI api) throws Exception {
        assertThat(api.getWebsite().getVersion().equals(NAMELESS_VERSION), "expected " + NAMELESS_VERSION + ", got " + api.getWebsite().getVersion());
    }

}
