package net.whydah.sso.usecases;

import net.whydah.sso.application.helpers.ApplicationHelper;
import net.whydah.sso.application.mappers.ApplicationMapper;
import net.whydah.sso.application.types.Application;
import net.whydah.sso.application.types.ApplicationCredential;
import net.whydah.sso.commands.adminapi.application.CommandAddApplication;
import net.whydah.sso.commands.adminapi.application.CommandListApplications;
import net.whydah.sso.commands.userauth.CommandGetUsertokenByUsertokenId;
import net.whydah.sso.session.baseclasses.BaseAdminWhydahServiceClient;
import net.whydah.sso.user.helpers.UserXpathHelper;
import net.whydah.sso.util.AdminSystemTestBaseConfig;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static net.whydah.sso.util.AdminSystemTestBaseConfig.SYSTEST_PROPERTY_ANONYMOUSTOKEN;
import static net.whydah.sso.util.AdminSystemTestBaseConfig.SYSTEST_PROPERTY_fulltokenapplications;
import static net.whydah.sso.util.LoggerUtil.first50;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AddUserRoleTest {
    static AdminSystemTestBaseConfig config;
    static BaseAdminWhydahServiceClient client;
    private static final Logger log = LoggerFactory.getLogger(AddUserRoleTest.class);

    static final String ROLE_NAME = "Mydata-" + UUID.randomUUID().toString();

	@BeforeClass
	public static void setup() throws Exception {
        config = new AdminSystemTestBaseConfig();

        if (config.isSystemTestEnabled()) {
            ApplicationCredential applicationCredential = new ApplicationCredential(config.TEMPORARY_APPLICATION_ID, config.TEMPORARY_APPLICATION_NAME, config.TEMPORARY_APPLICATION_SECRET);

            client = new BaseAdminWhydahServiceClient(config.tokenServiceUri.toString(), config.userAdminServiceUri.toString(), applicationCredential);
            client.getWAS().updateApplinks();
		}
	}

	@Test
	public void testUpdateRoleAndRefreshUserTokenWithExistingApplication(){
		if (config.isSystemTestEnabled()) {
			String tokenxml= config.logOnSystemTestApplicationAndSystemTestUser_getUserTokenXML(); 
            assertTrue(client.updateOrCreateUserApplicationRoleEntry("", "ACSResource", "Whydah", ROLE_NAME, "welcome", tokenxml));
        }
	}
	
	@Test
	public void testUpdateRoleAndRefreshUserTokenWithNonExistingApplciation(){
		if (config.isSystemTestEnabled()) {
            assertFalse(client.updateOrCreateUserApplicationRoleEntry("", "NON-EXISTING", "Whydah", ROLE_NAME, "welcome", config.logOnSystemTestApplicationAndSystemTestUser_getUserTokenXML()));
        }
	}
	
    @Ignore
    @Test
    public void testUpdateRoleAndRefreshUserTokenWithNonExistingApplciation2() throws Exception {
        if (config.isSystemTestEnabled()) {
			//create a new application now
		  
			String userTokenXml1 = config.logOnSystemTestApplicationAndSystemTestUser_getUserTokenXML();
            String userTokenId = UserXpathHelper.getUserTokenId(userTokenXml1);
            int existingApplications = countApplications(config.myApplicationTokenID, userTokenId);
            Application newApplication = ApplicationMapper.fromJson(ApplicationHelper.getDummyApplicationJson());
            
            
            
            String applicationJson = ApplicationMapper.toJson(newApplication);
            String testAddApplication = new CommandAddApplication(config.userAdminServiceUri, config.myApplicationTokenID, userTokenId, applicationJson).execute();
            log.trace("new app : " + testAddApplication);
            assertTrue(existingApplications == (countApplications(config.myApplicationTokenID, userTokenId) - 1));

            String newApplicationId = ApplicationMapper.fromJson(testAddApplication).getId();
            log.trace("new applicationId : " + newApplicationId);

            newApplication.setId(newApplicationId);


            //however, client cannot update because it is holding the old application list
            
           //NOTE: don't use newApplication.getId(). This is wrong b/c application is assigned a new unique ID from UIB
            //assertFalse(client.updateOrCreateUserApplicationRoleEntry(newApplication.getId(), newApplication.getName(), "Whydah", roleName, "welcome", userTokenXml1));
            assertFalse(client.updateOrCreateUserApplicationRoleEntry("", newApplication.getName(), "Whydah", ROLE_NAME, "welcome", userTokenXml1));
            //should update the application list
			client.getWAS().updateApplinks(true);
			
			
			//now, it is ok to update role
			
			//NOTE: don't use newApplication.getId(). This is wrong b/c application is assigned a new unique ID from UIB
            //assertTrue(client.updateOrCreateUserApplicationRoleEntry(newApplication.getId(), newApplication.getName(), "Whydah", roleName, "welcome", userTokenXml1));
			assertTrue(client.updateOrCreateUserApplicationRoleEntry("", newApplication.getName(), "Whydah", ROLE_NAME, "welcome", userTokenXml1));

            Thread.sleep(1000);  //

			// Check for correct UserToken
			String userTokenXml2 = new CommandGetUsertokenByUsertokenId(config.tokenServiceUri, config.myApplicationTokenID, config.myAppTokenXml, userTokenId).execute();
            assertTrue(userTokenXml2.contains(ROLE_NAME));

			//this works as expected as it contains the added role
			String userTokenXml3 = config.logOnSystemTestApplicationAndSystemTestUser_getUserTokenXML();
			
			
            //assertTrue(userTokenXml2.contains(roleName));
            assertTrue(userTokenXml3.contains(ROLE_NAME));


            // Check that we do not get anonympous usertoken
            assertTrue(userTokenXml2.contains("SystemTestUser"));
            // Check that we get the new role


            if (SYSTEST_PROPERTY_ANONYMOUSTOKEN) {
                // detele the role and verify that we now get an anonymous usertoken
            }
            if (!SYSTEST_PROPERTY_fulltokenapplications) {
                // check that we do not get all roles if we use an applicationid which is not set in the list of fulltokenapplications list/configuration
            }
           

		}
	}
	
	 private int countApplications(String myApplicationTokenID, String userTokenId) {
	        String applicationsJson = new CommandListApplications(config.userAdminServiceUri, myApplicationTokenID).execute();
         log.trace("applicationsJson=" + first50(applicationsJson));
         assertTrue(applicationsJson.length() > 100);
	        List<Application> applications = ApplicationMapper.fromJsonList(applicationsJson);
	        assertTrue(applications.size() > 2);
	        return applications.size();


	    }
}
