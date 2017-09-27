package net.whydah.sso.commands.adminapi.application;


import net.whydah.sso.application.helpers.ApplicationHelper;
import net.whydah.sso.application.mappers.ApplicationMapper;
import net.whydah.sso.application.types.Application;
import net.whydah.sso.session.WhydahApplicationSession;
import net.whydah.sso.session.WhydahUserSession;
import net.whydah.sso.util.AdminSystemTestBaseConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
            WhydahApplicationSession applicationSession = WhydahApplicationSession.getInstance(config.tokenServiceUri.toString(), config.appCredential);
            WhydahUserSession whydahUserSession = new WhydahUserSession(applicationSession, config.userCredential);


            log.debug("Adding application:\n" + ApplicationMapper.toPrettyJson(ApplicationMapper.fromJson(getDummyApplicationJson())));


            if (whydahUserSession.hasActiveSession()) {
                int existingApplications = countApplications(applicationSession.getActiveApplicationTokenId());
                log.debug("Applications before:" + existingApplications);

                Application newApplication = ApplicationMapper.fromJson(ApplicationHelper.getDummyApplicationJson());
                String applicationJson = ApplicationMapper.toJson(newApplication);

                String testAddApplication = new CommandAddApplication(config.userAdminServiceUri, applicationSession.getActiveApplicationTokenId(), whydahUserSession.getActiveUserTokenId(), applicationJson).execute();
                Thread.sleep(25000);  // We have to sleep a little to ensure that the UAS cache times out
                log.debug(testAddApplication);
                int after = countApplications(applicationSession.getActiveApplicationTokenId());
                log.debug("Applications found:" + after);
                assertTrue(existingApplications == after - 1);
            } else {
                log.debug("HAVE NO UASACCESS permission to test");
            }

        }

    }

    private int countApplications(String myApplicationTokenID) {
        String applicationsJson = new CommandListApplications(config.userAdminServiceUri, myApplicationTokenID).execute();
        log.debug("applicationsJson=" + applicationsJson);
        assertTrue(applicationsJson.length() > 100);
        List<Application> applications = ApplicationMapper.fromJsonList(applicationsJson);
        assertTrue(applications.size() > 2);
        return applications.size();


    }
}