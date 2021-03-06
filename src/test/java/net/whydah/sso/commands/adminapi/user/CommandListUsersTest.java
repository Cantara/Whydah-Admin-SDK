package net.whydah.sso.commands.adminapi.user;

import net.whydah.sso.application.helpers.ApplicationXpathHelper;
import net.whydah.sso.commands.appauth.CommandLogonApplication;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class CommandListUsersTest {

    static AdminSystemTestBaseConfig config;

    private static final Logger log = LoggerFactory.getLogger(CommandListUsersTest.class);

    @BeforeClass
    public static void setup() throws Exception {
        config = new AdminSystemTestBaseConfig();
    }


    @Test
    public void testListUsersCommandWithFallback() throws Exception {

        if (config.isSystemTestEnabled()) {
            String myAppTokenXml;
            myAppTokenXml = new CommandLogonApplication(config.tokenServiceUri, config.appCredential).execute();

            String myApplicationTokenID = ApplicationXpathHelper.getAppTokenIdFromAppTokenXml(myAppTokenXml);
            assertTrue(myApplicationTokenID != null && myApplicationTokenID.length() > 5);
            String userticket = UUID.randomUUID().toString();
            String userToken;
            userToken = new CommandLogonUserByUserCredential(config.tokenServiceUri, myApplicationTokenID, myAppTokenXml, config.userCredential, userticket).execute();
            String userTokenId = UserXpathHelper.getUserTokenId(userToken);
            assertTrue(userTokenId != null && userTokenId.length() > 5);

            String usersListJson;
            usersListJson = new CommandListUsers(config.userAdminServiceUri, myApplicationTokenID, userTokenId, "*").execute();

            log.trace("usersListJson=" + first50(usersListJson));

        }
    }

    @Test
    @Ignore //new endpoint
    public void testUserExists() throws Exception {

        if (config.isSystemTestEnabled()) {
            String myAppTokenXml;
            myAppTokenXml = new CommandLogonApplication(config.tokenServiceUri, config.appCredential).execute();

            String myApplicationTokenID = ApplicationXpathHelper.getAppTokenIdFromAppTokenXml(myAppTokenXml);
            assertTrue(myApplicationTokenID != null && myApplicationTokenID.length() > 5);
            String userticket = UUID.randomUUID().toString();
            String userToken;
            userToken = new CommandLogonUserByUserCredential(config.tokenServiceUri, myApplicationTokenID, myAppTokenXml, config.userCredential, userticket).execute();
            String userTokenId = UserXpathHelper.getUserTokenId(userToken);
            assertTrue(userTokenId != null && userTokenId.length() > 5);

            boolean usersExist = false;
            usersExist = new CommandUserExists(config.userAdminServiceUri, myApplicationTokenID, userTokenId, "acsemployee").execute();
            assertTrue(usersExist);
            usersExist = new CommandUserExists(config.userAdminServiceUri, myApplicationTokenID, userTokenId, "acsempdloyee").execute();
            assertFalse(usersExist);
        }

    }


}


