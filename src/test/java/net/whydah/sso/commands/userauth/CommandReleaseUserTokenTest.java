package net.whydah.sso.commands.userauth;

import net.whydah.sso.application.mappers.ApplicationTokenMapper;
import net.whydah.sso.commands.adminapi.user.CommandCreatePinVerifiedUser;
import net.whydah.sso.commands.systemtestbase.SystemTestBaseConfig;
import net.whydah.sso.user.mappers.UserIdentityMapper;
import net.whydah.sso.user.mappers.UserTokenMapper;
import net.whydah.sso.user.types.UserIdentity;
import net.whydah.sso.user.types.UserToken;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class CommandReleaseUserTokenTest {

    static SystemTestBaseConfig config;

    @BeforeClass
    public static void setup() throws Exception {
        config = new SystemTestBaseConfig();
    }


    private UserIdentity getTestNewUserIdentity() {
        Random rand = new Random();
        rand.setSeed(new java.util.Date().getTime());
        UserIdentity user = new UserIdentity("TestUser-" + UUID.randomUUID().toString().replace("-", "").replace("_", "").substring(1, 10), "Mt Test", "Testesen", "0", UUID.randomUUID().toString().replace("-", "").replace("_", "").substring(1, 10) + "@getwhydah.com", "47" + Integer.toString(rand.nextInt(100000000)));
        return user;

    }
    
    @Test
    public void testCommandValidateUsertokenId() throws Exception {

        if (config.isSystemTestEnabled()) {
        	
        	
        	//admin logon
        	UserToken adminUser = config.logOnSystemTestApplicationAndSystemTestUser();
        
        	//add and activate user
        	UserIdentity uir = getTestNewUserIdentity();
        	String userIdentityJson = UserIdentityMapper.toJsonWithoutUID(uir);
        	String ticket = "67678687";
        	String phoneNo = "98765433";
        	String pin = "3434";
        	new CommandSendSmsPin(config.tokenServiceUri, config.myApplicationToken.getApplicationTokenId(), ApplicationTokenMapper.toXML(config.myApplicationToken), phoneNo, pin).execute();
        	String userAddRoleResult = new CommandCreatePinVerifiedUser(config.tokenServiceUri, config.myApplicationToken.getApplicationTokenId(), ApplicationTokenMapper.toXML(config.myApplicationToken), adminUser.getTokenid(), ticket, phoneNo, pin, userIdentityJson).execute();
        	System.out.println("testAddUser:" + userAddRoleResult);

        	//get token
        	UserToken foundUserToken = UserTokenMapper.fromUserTokenXml(userAddRoleResult);
        	
            //delete by token id
            assertTrue(new CommandReleaseUserToken(config.tokenServiceUri, config.myApplicationTokenID, config.myAppTokenXml, foundUserToken.getTokenid()).execute());

        }

    }

}
