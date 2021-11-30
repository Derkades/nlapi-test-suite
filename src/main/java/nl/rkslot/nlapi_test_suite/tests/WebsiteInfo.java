package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessVersion;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.jetbrains.annotations.NotNull;

public class WebsiteInfo extends TestStage {

    private static final @NotNull NamelessVersion NAMELESS_VERSION = NamelessVersion.V2_0_0_PR_12;

    @Test
    public void langaugeEnglishUk(final @NotNull NamelessAPI api) throws Exception {
        assertThat(api.getWebsite().getLanguage().equals("EnglishUK"), "expected EnglishUK, got " + api.getWebsite().getLanguage());
    }

    @Test
    public void moduleCount(final @NotNull NamelessAPI api) throws Exception {
        assertThat(api.getWebsite().getModules().length == 4, "expected 4 modules installed by default, got " + api.getWebsite().getModules().length);
    }

    @Test
    public void versionIsLatest(final @NotNull NamelessAPI api) throws Exception {
        assertThat(api.getWebsite().getParsedVersion() == NAMELESS_VERSION,
                "expected " + NAMELESS_VERSION + ", got " + api.getWebsite().getVersion());
    }

}
