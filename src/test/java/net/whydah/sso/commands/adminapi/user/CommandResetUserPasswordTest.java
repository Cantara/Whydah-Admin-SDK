package net.whydah.sso.commands.adminapi.user;

import net.whydah.sso.user.types.UserToken;
import net.whydah.sso.util.AdminSystemTestBaseConfig;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandResetUserPasswordTest {
    private static final Logger log = LoggerFactory.getLogger(CommandResetUserPasswordTest.class);

    private static AdminSystemTestBaseConfig config;

    @BeforeClass
    public static void setup() {
        config = new AdminSystemTestBaseConfig();
    }


    @Test
    @Ignore
    public void testCommandResetUserPassword() throws Exception {
        if (config.isSystemTestEnabled()) {
            UserToken adminUser = config.logOnSystemTestApplicationAndSystemTestUser();

            boolean result = new CommandResetUserPassword(config.userAdminServiceUri, config.myApplicationTokenID, config.userName).execute();
            log.debug("CommandResetUserPassword return=" + result);

            Thread.sleep(5000);
            result = new CommandResetUserPassword(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), config.userName, "NewUserPasswordResetEmail.ftl").execute();
            log.debug("CommandResetUserPassword return=" + result);
        }
    }
}