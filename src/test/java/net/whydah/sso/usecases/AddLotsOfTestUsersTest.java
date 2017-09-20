package net.whydah.sso.usecases;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Random;

import net.whydah.sso.commands.adminapi.user.CommandAddUser;
import net.whydah.sso.user.mappers.UserAggregateMapper;
import net.whydah.sso.user.types.UserAggregate;
import net.whydah.sso.user.types.UserApplicationRoleEntry;
import net.whydah.sso.user.types.UserToken;
import net.whydah.sso.util.AdminSystemTestBaseConfig;
import net.whydah.sso.util.LoggerUtil;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class AddLotsOfTestUsersTest {

    static AdminSystemTestBaseConfig config;
    static char c;

    @BeforeClass
    public static void setup() throws Exception {
        config = new AdminSystemTestBaseConfig();
        Random r = new Random();
        c = (char) (r.nextInt(26) + 'a');

    }


    @Test
    public void testAddUser() throws Exception {
        if (config.isSystemTestEnabled()) {

            UserToken adminUser = config.logOnSystemTestApplicationAndSystemTestUser();

            // Add 50 users
            addTestUsers(adminUser, 60000, 60150);
        }
    }


    private void addTestUsers(UserToken adminUser, int startFrom, int countTo) throws InterruptedException {

        for (int i = startFrom; i < countTo; i++) {
            addATestUser(adminUser, i);
        }
        Thread.sleep((countTo - startFrom) * 10);
    }


    private void addATestUser(UserToken adminUser, int i) throws InterruptedException {
        UserAggregate ua = new UserAggregate(
                c + "j" + "s_uid-" + i,
                c + "s_username_" + i,
                "firstName_" + i,
                "lastName_" + i,
                "personRef_" + i,
                "tester_" + i + "@whydah.com", String.valueOf(RandomUtils.nextInt(1000000000)));
        ua.setRoleList(new ArrayList<UserApplicationRoleEntry>());
        String json = UserAggregateMapper.toJson(ua);

        //new CommandAddUser(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getTokenid(), json).queue();
        //Thread.sleep(200);
        String userAddRoleResult = new CommandAddUser(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getTokenid(), json).execute();
        System.out.println(i + " testAddUser:" + LoggerUtil.first50(userAddRoleResult));
        assertNotNull(userAddRoleResult);
        assertTrue(userAddRoleResult.length() > 100);

    }

}
