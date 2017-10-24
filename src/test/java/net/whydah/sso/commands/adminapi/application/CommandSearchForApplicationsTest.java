package net.whydah.sso.commands.adminapi.application;

import net.whydah.sso.application.helpers.ApplicationXpathHelper;
import net.whydah.sso.commands.appauth.CommandLogonApplication;
import net.whydah.sso.commands.appauth.CommandVerifyUASAccessByApplicationTokenId;
import net.whydah.sso.commands.userauth.CommandLogonUserByUserCredential;
import net.whydah.sso.user.helpers.UserXpathHelper;
import net.whydah.sso.util.AdminSystemTestBaseConfig;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static net.whydah.sso.util.LoggerUtil.first50;
import static org.junit.Assert.assertTrue;

public class CommandSearchForApplicationsTest {

    private static final Logger log = LoggerFactory.getLogger(CommandSearchForApplicationsTest.class);

    static AdminSystemTestBaseConfig config;

    @BeforeClass
    public static void setup() throws Exception {
        config = new AdminSystemTestBaseConfig();
        //config.setLocalTest();
    }


    @Ignore
    @Test
    public void testSearchApplicationsCommand() throws Exception {
        if (config.isSystemTestEnabled()) {
            String myAppTokenXml = new CommandLogonApplication(config.tokenServiceUri, config.appCredential).execute();
            String myApplicationTokenID = ApplicationXpathHelper.getAppTokenIdFromAppTokenXml(myAppTokenXml);
            assertTrue(myApplicationTokenID != null && myApplicationTokenID.length() > 5);
            String userticket = UUID.randomUUID().toString();
            String userToken = new CommandLogonUserByUserCredential(config.tokenServiceUri, myApplicationTokenID, myAppTokenXml, config.userCredential, userticket).execute();
            String userTokenId = UserXpathHelper.getUserTokenId(userToken);
            assertTrue(userTokenId != null && userTokenId.length() > 5);

            boolean hasAccess = new CommandVerifyUASAccessByApplicationTokenId(config.userAdminServiceUri.toString(), myApplicationTokenID, userTokenId).execute();
            if (hasAccess) {
                String applicationsJson = new CommandListApplications(config.userAdminServiceUri, myApplicationTokenID).execute();
                log.debug("applicationsJson=" + first50(applicationsJson));

                String applicationsJsonl = new CommandSearchForApplications(config.userAdminServiceUri, myApplicationTokenID, userTokenId, config.appCredential.getApplicationID()).execute();
                log.debug("applicationsJson=" + first50(applicationsJsonl));
                assertTrue(applicationsJsonl != null);
            } else {
                log.debug("NO UASaccess permission to test");
            }

        }

    }


}
