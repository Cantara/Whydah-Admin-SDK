package net.whydah.sso.commands.adminapi.user;

import net.whydah.sso.commands.userauth.CommandGetUserTokenByUserTicket;
import net.whydah.sso.user.mappers.UserIdentityMapper;
import net.whydah.sso.user.mappers.UserTokenMapper;
import net.whydah.sso.user.types.UserCredential;
import net.whydah.sso.user.types.UserIdentity;
import net.whydah.sso.user.types.UserToken;
import net.whydah.sso.util.AdminSystemTestBaseConfig;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommandUpdateUserTest {
	private static final Logger log = LoggerFactory.getLogger(CommandUpdateUserTest.class);

	static AdminSystemTestBaseConfig config;

	    @BeforeClass
	    public static void setup() {
            config = new AdminSystemTestBaseConfig();
        }

	    //wait until UAS is updated
	    @Test
	    @Ignore
	    public void testUpdateUser() {
	        if (config.isSystemTestEnabled()) {

	        	config.logOnSystemTestApplication();
	        	String userTicket1 = UUID.randomUUID().toString();
	        	String userTicket2 = UUID.randomUUID().toString();
	            UserToken adminUser = UserTokenMapper.fromUserTokenXml(config.logOnBySystemTestUserCredential(userTicket1));
	            UserToken normalUser = UserTokenMapper.fromUserTokenXml(config.logOnByUserCredential(userTicket2, new UserCredential(config.userName2, config.password2)));

				String userIdentityJson = new CommandGetUser(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getUserTokenId(), normalUser.getUid()).execute();
				log.debug("getuser:" + userIdentityJson);
	   
	           
	            UserIdentity updateMe = UserIdentityMapper.fromUserIdentityJson(userIdentityJson);
	           //keep the current attributes
	            String oldFirstName = updateMe.getFirstName();
	            String oldLastName = updateMe.getLastName();
	            String oldCellPhone = updateMe.getCellPhone();
	            String oldEmail = updateMe.getEmail();
	            String oldPerRef= updateMe.getPersonRef();
	            
	            //try updating
	            updateMe.setFirstName((updateMe.getFirstName().contains("_")? updateMe.getFirstName().substring(0, updateMe.getFirstName().indexOf('_')) : updateMe.getFirstName()) + "_" + userTicket2);
	            updateMe.setLastName((updateMe.getLastName().contains("_")? updateMe.getLastName().substring(0, updateMe.getLastName().indexOf('_')) : updateMe.getLastName()) + "_" + userTicket2);
	            updateMe.setCellPhone("99999999");
	            updateMe.setEmail("test@whydah.com");
	            updateMe.setPersonRef((updateMe.getPersonRef().contains("_")? updateMe.getPersonRef().substring(0, updateMe.getPersonRef().indexOf('_')) : updateMe.getPersonRef()) + "_" + userTicket2);
	            
	            //do update
				String userUpdateResult = new CommandUpdateUser(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getUserTokenId(), updateMe.getUsername(), UserIdentityMapper.toJson(updateMe)).execute();
				log.debug("update=" + userUpdateResult);
	            UserIdentity updated = UserIdentityMapper.fromUserIdentityJson(userUpdateResult);
	            //check for modifications
                assertEquals(updated.getFirstName(), updateMe.getFirstName());
				assertEquals(updated.getCellPhone(), updateMe.getCellPhone());
				assertEquals(updated.getEmail(), updateMe.getEmail());
				assertEquals(updated.getLastName(), updateMe.getLastName());
				assertEquals(updated.getPersonRef(), updateMe.getPersonRef());

				//NO NEED TO CALL REFRESH HERE ANY MORE, IT IS AUTOMATICALLY UPDATE WHEN GETTING A USERTOKEN
//	            String tokenXml = new CommandRefreshUserToken(config.tokenServiceUri, config.myApplicationToken.getApplicationTokenId(), config.myApplicationTokenID, normalUser.getTokenid()).execute();
//	    		assertTrue(tokenXml != null);

				//check if UserToken info changed in STS
				String utXml = new CommandGetUserTokenByUserTicket(config.tokenServiceUri, config.myApplicationToken.getApplicationTokenId(), config.myAppTokenXml, userTicket2).execute();
				UserToken ut = UserTokenMapper.fromUserTokenXml(utXml);
				assertTrue(ut.getFirstName().contains(userTicket2));
				assertEquals(ut.getFirstName(), updateMe.getFirstName());
				assertTrue(ut.getLastName().contains(userTicket2));
				assertEquals(ut.getLastName(), updateMe.getLastName());
				assertEquals("99999999", ut.getCellPhone());
				assertEquals("test@whydah.com", ut.getEmail());
				assertTrue(ut.getPersonRef().contains(userTicket2));
				assertEquals(ut.getPersonRef(), updateMe.getPersonRef());
				//Just get the old attributes back

	            updateMe.setFirstName(oldFirstName);
	            updateMe.setLastName(oldLastName);
	            updateMe.setCellPhone(oldCellPhone);
	            updateMe.setEmail(oldEmail);
	            updateMe.setPersonRef(oldPerRef);
	            
	            //do update
				new CommandUpdateUser(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getUserTokenId(), updateMe.getUsername(), UserIdentityMapper.toJson(updateMe)).execute();
			}
	    }
}
