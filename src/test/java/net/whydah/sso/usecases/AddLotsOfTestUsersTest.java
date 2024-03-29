package net.whydah.sso.usecases;

import net.whydah.sso.commands.adminapi.user.CommandAddUser;
import net.whydah.sso.session.WhydahApplicationSession;
import net.whydah.sso.session.WhydahUserSession;
import net.whydah.sso.user.mappers.UserAggregateMapper;
import net.whydah.sso.user.types.UserAggregate;
import net.whydah.sso.user.types.UserApplicationRoleEntry;
import net.whydah.sso.user.types.UserToken;
import net.whydah.sso.util.AdminSystemTestBaseConfig;
import net.whydah.sso.util.LoggerUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

public class AddLotsOfTestUsersTest {

    static AdminSystemTestBaseConfig config;
    static char c;
    static WhydahApplicationSession applicationSession;
    static WhydahUserSession whydahUserSession;
    private static boolean stresstest = false;
    private static final Logger log = getLogger(AddLotsOfTestUsersTest.class);
    private static final int startFrom = 90000;


    @BeforeClass
    public static void setup() throws Exception {
        config = new AdminSystemTestBaseConfig();
        
        applicationSession = WhydahApplicationSession.getInstance(config.tokenServiceUri.toString(), config.appCredential);
        whydahUserSession = new WhydahUserSession(applicationSession, config.userCredential);
        Random r = new Random();
        c = (char) (r.nextInt(26) + 'a');

    }


    @Test
    public void testLotsOfUsers() throws Exception {
        if (config.isSystemTestEnabled() && stresstest) {

            UserToken adminUser = config.logOnSystemTestApplicationAndSystemTestUser();

            // Add 50 users
            addTestUsers(adminUser, startFrom, 70050);
        }
    }


    private void addTestUsers(UserToken adminUser, int startFrom, int countTo) throws InterruptedException {

        for (int i = startFrom; i < countTo; i++) {
            addATestUser(adminUser, i);
        }
        Thread.sleep((countTo - startFrom) * 100);
    }


    private void addATestUser(UserToken adminUser, int i) throws InterruptedException {
        UserAggregate ua = new UserAggregate(
                UUID.randomUUID().toString(),
                "s_username_" + i,
                "FirstName" + i,
                "lastName" + i,
                "personRef_" + i,
                "tester_" + i + "@whydah.com",
                "" + String.valueOf(10000000 + RandomUtils.nextInt(90000000)));
        ua.setRoleList(new ArrayList<UserApplicationRoleEntry>());
        String json = UserAggregateMapper.toJson(ua);

        //new CommandAddUser(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getTokenid(), json).queue();
        //Thread.sleep(200);
        System.out.println(json);
        String userAddRoleResult = new CommandAddUser(config.userAdminServiceUri, applicationSession.getActiveApplicationTokenId(), whydahUserSession.getActiveUserTokenId(), json).execute();
        log.info(i + " testAddUser:" + LoggerUtil.first50(userAddRoleResult));
        assertNotNull(userAddRoleResult);
        assertTrue(userAddRoleResult.length() > 100);

    }

}
