package net.whydah.sso.usecases;

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

import java.util.ArrayList;
import java.util.Random;

public class AddLotsOfTestUsersTest {

    static AdminSystemTestBaseConfig config;

    @BeforeClass
    public static void setup() throws Exception {
        config = new AdminSystemTestBaseConfig();
    }


    @Test
    public void testAddUser() throws Exception {
        if (config.isSystemTestEnabled()) {

            UserToken adminUser = config.logOnSystemTestApplicationAndSystemTestUser();

            // Add 50 users
            addTestUsers(adminUser, 10000, 10050);
        }
    }


    private void addTestUsers(UserToken adminUser, int startFrom, int countTo) throws InterruptedException {

        for (int i = startFrom; i < countTo; i++) {
            addATestUser(adminUser, i);
        }
    }


    private void addATestUser(UserToken adminUser, int i) {
        Random r = new Random();
        char c = (char) (r.nextInt(26) + 'a');
        UserAggregate ua = new UserAggregate(c + "j" +
                "s_uid-" + i, c + "s_username_" + i, "firstName_" + i, "lastName " + i, "personRef " + i, "tester" + i + "@whydah.com", String.valueOf(RandomUtils.nextInt(1000000000)));
        ua.setRoleList(new ArrayList<UserApplicationRoleEntry>());
        String json = UserAggregateMapper.toJson(ua);

        String userAddRoleResult = new CommandAddUser(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getTokenid(), json).execute();
        System.out.println("testAddUser:" + LoggerUtil.first50(userAddRoleResult));

    }

}
