package net.whydah.sso.commands.adminapi.user;

import net.whydah.sso.commands.appauth.CommandVerifyUASAccessByApplicationTokenId;
import net.whydah.sso.user.mappers.UserTokenMapper;
import net.whydah.sso.user.types.UserToken;
import net.whydah.sso.util.AdminSystemTestBaseConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandGetUserTest {
    private static final Logger log = LoggerFactory.getLogger(CommandGetUserTest.class);

    private static AdminSystemTestBaseConfig config;

    @BeforeClass
    public static void setup() {
        config = new AdminSystemTestBaseConfig();
    }


    @Test
    public void testCommandGetUser() {
        if (config.isSystemTestEnabled()) {
            UserToken adminUser = config.logOnSystemTestApplicationAndSystemTestUser();

            boolean hasAccess = new CommandVerifyUASAccessByApplicationTokenId(config.userAdminServiceUri.toString(), config.myApplicationTokenID).execute();
            if(hasAccess){
                String userAggregateJson = new CommandGetUser(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getUserTokenId(), "acsmanager").execute();
                 log.debug("userAggregateJson=" + userAggregateJson);
                 UserToken foundUserToken = UserTokenMapper.fromUserAggregateJson(userAggregateJson);
                 log.debug(foundUserToken.toString());
            }
        }
    }
}