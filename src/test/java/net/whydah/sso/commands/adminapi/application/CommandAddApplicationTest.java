package net.whydah.sso.commands.adminapi.application;


import net.whydah.sso.application.helpers.ApplicationHelper;
import net.whydah.sso.application.helpers.ApplicationXpathHelper;
import net.whydah.sso.application.mappers.ApplicationMapper;
import net.whydah.sso.application.types.Application;
import net.whydah.sso.commands.appauth.CommandLogonApplication;
import net.whydah.sso.commands.appauth.CommandVerifyUASAccessByApplicationTokenId;
import net.whydah.sso.commands.userauth.CommandLogonUserByUserCredential;
import net.whydah.sso.user.helpers.UserXpathHelper;
import net.whydah.sso.util.AdminSystemTestBaseConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class CommandAddApplicationTest {

    static AdminSystemTestBaseConfig config;
    private static final Logger log = LoggerFactory.getLogger(CommandAddApplicationTest.class);


    @BeforeClass
    public static void setup() throws Exception {
        config = new AdminSystemTestBaseConfig();
        //config.setLocalTest();
    }

    public static String getDummyApplicationJson() {
        return ApplicationHelper.getDummyApplicationJson();
    }

    @Test
    public void testAddApplication() throws Exception {

        if (config.isSystemTestEnabled()) {


            log.debug("Adding application:\n" + ApplicationMapper.toPrettyJson(ApplicationMapper.fromJson(getDummyApplicationJson())));
            String myAppTokenXml = new CommandLogonApplication(config.tokenServiceUri, config.appCredential).execute();
            String myApplicationTokenID = ApplicationXpathHelper.getAppTokenIdFromAppTokenXml(myAppTokenXml);
            assertTrue(myApplicationTokenID != null && myApplicationTokenID.length() > 5);
            String userticket = UUID.randomUUID().toString();
            String userToken = new CommandLogonUserByUserCredential(config.tokenServiceUri, myApplicationTokenID, myAppTokenXml, config.userCredential, userticket).execute();
            String userTokenId = UserXpathHelper.getUserTokenId(userToken);
            assertTrue(userTokenId != null && userTokenId.length() > 5);

            boolean hasAccess = new CommandVerifyUASAccessByApplicationTokenId(config.userAdminServiceUri.toString(), myApplicationTokenID, userTokenId).execute();

            if (hasAccess) {
                int existingApplications = countApplications(myApplicationTokenID, userTokenId);

                Application newApplication = ApplicationMapper.fromJson(ApplicationHelper.getDummyApplicationJson());
                String applicationJson = ApplicationMapper.toJson(newApplication);

                String testAddApplication = new CommandAddApplication(config.userAdminServiceUri, myApplicationTokenID, userTokenId, applicationJson) {

                    protected String dealWithFailedResponse(String responseBody, int statusCode) {
                        return responseBody;
                    }

                    ;

                }.execute();
                Thread.sleep(40000);  // We have to sleep a little to ensure that the UAS cache times out
                log.debug(applicationJson);
                log.debug("Applications found:" + countApplications(myApplicationTokenID, userTokenId));
                int count = countApplications(myApplicationTokenID, userTokenId) - 1;
                assertTrue(existingApplications == count);
            } else {
                log.debug("HAVE NO UASACCESS permission to test");
            }

        }

    }

    private int countApplications(String myApplicationTokenID, String userTokenId) {
        String applicationsJson = new CommandListApplications(config.userAdminServiceUri, myApplicationTokenID).execute();
        log.debug("applicationsJson=" + applicationsJson);
        assertTrue(applicationsJson.length() > 100);
        List<Application> applications = ApplicationMapper.fromJsonList(applicationsJson);
        assertTrue(applications.size() > 2);
        return applications.size();


    }
}