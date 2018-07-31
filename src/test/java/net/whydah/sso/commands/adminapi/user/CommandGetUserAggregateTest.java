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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandGetUserAggregateTest {
    private static final Logger log = LoggerFactory.getLogger(CommandGetUserAggregateTest.class);

    static AdminSystemTestBaseConfig config;

    @BeforeClass
    public static void setup() {
        config = new AdminSystemTestBaseConfig();
    }


    @Test
    public void testGetUserAggregate() {
        if (config.isSystemTestEnabled()) {
            String myAppTokenXml = new CommandLogonApplication(config.tokenServiceUri, config.appCredential).execute();
            log.debug(myAppTokenXml);
            String myApplicationTokenID = ApplicationXpathHelper.getAppTokenIdFromAppTokenXml(myAppTokenXml);
            log.debug(myApplicationTokenID);
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

            //log.debug("userJson=" + usersAggregateJson);
        }
    }
}