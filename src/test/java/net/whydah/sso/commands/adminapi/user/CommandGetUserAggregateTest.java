package net.whydah.sso.commands.adminapi.user;


import static org.junit.Assert.assertTrue;

import java.util.UUID;

import net.whydah.sso.application.helpers.ApplicationXpathHelper;
import net.whydah.sso.commands.appauth.CommandLogonApplication;
import net.whydah.sso.commands.userauth.CommandLogonUserByUserCredential;
import net.whydah.sso.user.helpers.UserXpathHelper;
import net.whydah.sso.user.mappers.UserTokenMapper;
import net.whydah.sso.user.types.UserToken;
import net.whydah.sso.util.AdminSystemTestBaseConfig;

import org.junit.BeforeClass;
import org.junit.Test;

public class CommandGetUserAggregateTest {

    static AdminSystemTestBaseConfig config;

    @BeforeClass
    public static void setup() throws Exception {
        config = new AdminSystemTestBaseConfig();
    }


    @Test
    public void testGetUserAggregate() throws Exception {

        if (config.isSystemTestEnabled()) {
            String myAppTokenXml = new CommandLogonApplication(config.tokenServiceUri, config.appCredential).execute();
            System.out.println(myAppTokenXml);
            String myApplicationTokenID = ApplicationXpathHelper.getAppTokenIdFromAppTokenXml(myAppTokenXml);
            System.out.println(myApplicationTokenID);
            assertTrue(myApplicationTokenID != null && myApplicationTokenID.length() > 5);
            String userticket = UUID.randomUUID().toString();
            String userToken;
            userToken = new CommandLogonUserByUserCredential(config.tokenServiceUri, myApplicationTokenID, myAppTokenXml, config.userCredential, userticket).execute();
            String userTokenId = UserXpathHelper.getUserTokenId(userToken);
            assertTrue(userTokenId != null && userTokenId.length() > 5);

            String usersAggregateJson = "";
            usersAggregateJson = new CommandGetUserAggregate(config.userAdminServiceUri, myApplicationTokenID, userTokenId, "useradmin").execute();
            assertTrue(usersAggregateJson.length() > 10);
            assertTrue(usersAggregateJson.contains("useradmin"));
            UserToken returnedUserAggregateAsUserToken = UserTokenMapper.fromUserAggregateJson(usersAggregateJson);
            assertTrue(returnedUserAggregateAsUserToken.getUserName().equalsIgnoreCase("useradmin"));

            //System.out.println("userJson=" + usersAggregateJson);
        }
    }

}