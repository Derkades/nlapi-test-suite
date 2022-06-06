package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.NamelessVersion;
import com.namelessmc.java_api.Website;
import nl.rkslot.nlapi_test_suite.Test;
import nl.rkslot.nlapi_test_suite.TestStage;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;

public class WebsiteInfo extends TestStage {

    private static final @NonNull NamelessVersion NAMELESS_VERSION = NamelessVersion.V2_0_0_PR_12;

    @Test
    public void langauge(final @NonNull NamelessAPI api) throws Exception {
        final Website website = api.website();
        assertThat(website.rawLocale().equals("en_UK"), "expected en_UK, got " + website.rawLocale());
        assertThat(website.locale().equals(new Locale("en", "UK")), "unexpected Locale object, got " + website.locale());
    }

    @Test
    public void moduleCount(final @NonNull NamelessAPI api) throws Exception {
        assertThat(api.website().getModules().length == 4, "expected 4 modules installed by default, got " + api.website().getModules().length);
    }

    @Test
    public void versionIsLatest(final @NonNull NamelessAPI api) throws Exception {
        assertThat(api.website().parsedVersion() == NAMELESS_VERSION,
                "expected " + NAMELESS_VERSION + ", got " + api.website().rawVersion());
    }

}
