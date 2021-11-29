package nl.rkslot.nlapi_test_suite.tests;

import com.namelessmc.java_api.NamelessAPI;
import com.namelessmc.java_api.Website;
import nl.rkslot.nlapi_test_suite.TestStage;

public class WebsiteInfo extends TestStage {

	public void runTest(NamelessAPI api) throws Exception {
		Website website = api.getWebsite();

		assertThat(website.getLanguage().equals("EnglishUK"));
		website.getVersion();
		System.out.println("NamelessMC version " + website.getParsedVersion());
		assertThat(website.getModules().length > 0);
		website.getUpdate();
	}

}
