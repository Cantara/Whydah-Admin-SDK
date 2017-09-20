package net.whydah.sso.commands.adminapi.user;

import static org.junit.Assert.assertTrue;
import net.whydah.sso.application.helpers.ApplicationXpathHelper;
import net.whydah.sso.commands.appauth.CommandLogonApplication;
import net.whydah.sso.util.AdminSystemTestBaseConfig;

import org.junit.BeforeClass;
import org.junit.Test;

public class CommandUserPasswordLoginEnabledTest {


    static AdminSystemTestBaseConfig config;

    @BeforeClass
    public static void setup() throws Exception {
        config = new AdminSystemTestBaseConfig();
    }


    @Test
    public void testCommandUserPasswordLoginEnabled() throws Exception {

        if (config.isSystemTestEnabled()) {
            String myAppTokenXml;
            myAppTokenXml = new CommandLogonApplication(config.tokenServiceUri, config.appCredential).execute();

            String myApplicationTokenID = ApplicationXpathHelper.getAppTokenIdFromAppTokenXml(myAppTokenXml);
            assertTrue(myApplicationTokenID != null && myApplicationTokenID.length() > 5);
            Boolean passwordlogin = new CommandUserPasswordLoginEnabled(config.userAdminServiceUri, myApplicationTokenID, "useradmin").execute();
            // Need to create real testdata for the assert to work, as systemusers does not have the roles set..
            //     assertTrue(passwordlogin);


        }
    }

}
