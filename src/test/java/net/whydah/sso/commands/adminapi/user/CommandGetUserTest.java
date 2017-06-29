package net.whydah.sso.commands.adminapi.user;

import net.whydah.sso.commands.appauth.CommandVerifyUASAccessByApplicationTokenId;
import net.whydah.sso.user.mappers.UserTokenMapper;
import net.whydah.sso.user.types.UserToken;
import net.whydah.sso.util.SystemTestBaseConfig;
import org.junit.BeforeClass;
import org.junit.Test;

public class CommandGetUserTest {

    private static SystemTestBaseConfig config;

    @BeforeClass
    public static void setup() throws Exception {
        config = new SystemTestBaseConfig();
    }


    @Test
    public void testCommandGetUser() throws Exception {



        if (config.isSystemTestEnabled()) {
            UserToken adminUser = config.logOnSystemTestApplicationAndSystemTestUser();

            boolean hasAccess = new CommandVerifyUASAccessByApplicationTokenId(config.userAdminServiceUri.toString(), config.myApplicationTokenID).execute();
            if(hasAccess){
            	 String userAggregateJson = new CommandGetUser(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getTokenid(), "acsmanager").execute();
                 System.out.println("userAggregateJson=" + userAggregateJson);
                 UserToken foundUserToken = UserTokenMapper.fromUserAggregateJson(userAggregateJson);
                 System.out.println(foundUserToken.toString());
            }
            
           


        }


    }

}