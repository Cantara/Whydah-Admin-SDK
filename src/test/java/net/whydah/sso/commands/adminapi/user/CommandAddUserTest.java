package net.whydah.sso.commands.adminapi.user;

import net.whydah.sso.user.mappers.UserIdentityMapper;
import net.whydah.sso.user.types.UserIdentity;
import net.whydah.sso.user.types.UserToken;
import net.whydah.sso.util.AdminSystemTestBaseConfig;
import net.whydah.sso.util.LoggerUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CommandAddUserTest {
    private static final Logger log = LoggerFactory.getLogger(CommandAddUserTest.class);

    static AdminSystemTestBaseConfig config;

    @BeforeClass
    public static void setup() {
        config = new AdminSystemTestBaseConfig();
    }




    @Test
    public void testAddUser() {
        if (config.isSystemTestEnabled()) {

            UserToken adminUser = config.logOnSystemTestApplicationAndSystemTestUser();

            UserIdentity uir = getTestNewUserIdentity();
            String userIdentityJson = UserIdentityMapper.toJsonWithoutUID(uir);
            String userAddRoleResult = new CommandAddUser(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getUserTokenId(), userIdentityJson).execute();
            log.debug("testAddUser:" + LoggerUtil.first50(userAddRoleResult));

            assertNotNull(userAddRoleResult);
            assertTrue(userAddRoleResult.length() > 100);
            String usersListJson = new CommandListUsers(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getUserTokenId(), "*").execute();
            log.debug("usersListJson=" + LoggerUtil.first50(usersListJson));

            // simple test to detect paging results
            if (!usersListJson.contains("\"rows\":\"250\"")) {
                assertTrue(usersListJson.contains(uir.getUsername()));
            }

        }

    }

    private UserIdentity getTestNewUserIdentity() {
        Random rand = new Random();
        rand.setSeed(new java.util.Date().getTime());
        UserIdentity user = new UserIdentity("Test-User-" +
                UUID.randomUUID().toString().replace("-", "").replace("_", "").substring(1, 10),
                "Mt Test",
                "Testesen",
                "PersonRef0",
                UUID.randomUUID().toString().replace("-", "").replace("_", "").substring(1, 10) + "@getwhydah.com",
                "47" + Integer.toString(rand.nextInt(100000000)));
        return user;

    }
}


