package net.whydah.sso.commands.adminapi.application;

import net.whydah.sso.commands.appauth.CommandVerifyUASAccessByApplicationTokenId;
import net.whydah.sso.session.WhydahApplicationSession;
import net.whydah.sso.session.WhydahUserSession;
import net.whydah.sso.user.types.UserCredential;
import net.whydah.sso.util.AdminSystemTestBaseConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.whydah.sso.util.LoggerUtil.first50;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CommandSearchForApplicationsTest {

    private static final Logger log = LoggerFactory.getLogger(CommandSearchForApplicationsTest.class);

    static AdminSystemTestBaseConfig config;
    public static String userName = "admin";
    public static String password = "whydahadmin";
    private static WhydahApplicationSession applicationSession;



    @BeforeClass
    public static void setup() throws Exception {
        config = new AdminSystemTestBaseConfig();
        userName = config.userName;
        password = config.password;
        if (config.isStatisticsExtensionSystemtestEnabled()) {
            applicationSession = WhydahApplicationSession.getInstance(config.tokenServiceUri.toString(), config.appCredential);
        }

    }


    //    @Ignore
    @Test
    public void testSearchApplicationsCommand() throws Exception {
        if (config.isSystemTestEnabled()) {
            UserCredential userCredential = new UserCredential(userName, password);
            assertTrue(applicationSession.checkActiveSession());
            WhydahUserSession userSession = new WhydahUserSession(applicationSession, userCredential);
            assertTrue(userSession.hasActiveSession());
            assertNotNull(userSession.getActiveUserToken());
            assertTrue(applicationSession.checkActiveSession());


            boolean hasAccess = new CommandVerifyUASAccessByApplicationTokenId(config.userAdminServiceUri.toString(), applicationSession.getActiveApplicationTokenId(), userSession.getActiveUserTokenId()).execute();
            if (hasAccess) {
                String applicationsJson = new CommandListApplications(config.userAdminServiceUri, applicationSession.getActiveApplicationTokenId()).execute();
                log.debug("applicationsJson=" + first50(applicationsJson));

                String applicationsJsonl = new CommandSearchForApplications(config.userAdminServiceUri, applicationSession.getActiveApplicationTokenId(), userSession.getActiveUserTokenId(), config.appCredential.getApplicationID()).execute();
                log.debug("applicationsJson=" + first50(applicationsJsonl));
                assertTrue(applicationsJsonl != null);
            } else {
                log.debug("NO UASaccess permission to test");
            }

        }

    }


}
