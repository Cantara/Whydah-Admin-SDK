package net.whydah.sso.user;


import net.whydah.sso.user.helpers.UserRoleJsonPathHelper;
import net.whydah.sso.user.mappers.UserTokenMapper;
import net.whydah.sso.user.types.UserApplicationRoleEntry;
import net.whydah.sso.user.types.UserToken;
import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

public class UserJsonHelperTest {
    private static final Logger log = getLogger(UserJsonHelperTest.class);

    String userAggregateJson = """
            {
              "uid": "uid",
              "username": "usernameABC",
              "firstName": "firstName",
              "lastName": "lastName",
              "personRef": "personRef",
              "email": "email",
              "cellPhone": "12345678",
              "password": "password",
              "roles": [
                {
                  "applicationId": "applicationId",
                  "applicationName": "applicationName",
                  "organizationId": "organizationId",
                  "organizationName": "organizationName",
                  "applicationRoleName": "roleName",
                  "applicationRoleValue": "email"
                },
                {
                  "applicationId": "applicationId123",
                  "applicationName": "applicationName123",
                  "organizationId": "organizationId123",
                  "organizationName": "organizationName123",
                  "applicationRoleName": "roleName123",
                  "applicationRoleValue": "roleValue123"
                }
              ]
            }\
            """;
	
    @Test
    public void testParseUserIdentityJson() {
        String userJson = "{\"identity\":{\"username\":\"91905054\",\"firstName\":\"Thor Henning\",\"lastName\":\"Hetland\",\"personRef\":\"\",\"email\":\"totto@cantara.no\",\"cellPhone\":\"91905054\",\"uid\":\"00d07a25-efbe-484a-b00e-67859a106dd4\"},\"roles\":[],\"personName\":\"Thor Henning Hetland\",\"personRef\":\"\",\"uid\":\"00d07a25-efbe-484a-b00e-67859a106dd4\",\"lastName\":\"Hetland\",\"email\":\"totto@cantara.no\",\"firstName\":\"Thor Henning\",\"username\":\"91905054\",\"cellPhone\":\"91905054\"}";
        UserToken myToken = UserTokenMapper.fromUserIdentityJson(userJson);
        log.debug(myToken.toString());
    }

    @Test
    public void testUserAggregateTest() {
        String userAggregateJson = "{\"uid\":\"useradmin\",\"username\":\"useradmin\",\"firstName\":\"UserAdmin\",\"lastName\":\"UserAdminWebApp\",\"personRef\":\"42\",\"email\":\"whydahadmin@getwhydah.com\",\"cellPhone\":\"87654321\",\"roles\": [{\"applicationId\":\"19\",\"applicationName\":\"\",\"applicationRoleName\":\"WhydahUserAdmin\",\"applicationRoleValue\":\"1\",\"organizationName\":\"\"}]}\n";
        UserToken myToken = UserTokenMapper.fromUserAggregateJson(userAggregateJson);
        log.debug(myToken.toString());
    }
    
    @Test
    public void testGetUserRoleFromUserAggregateJSON() {
        UserApplicationRoleEntry roles[] = UserRoleJsonPathHelper.getUserRoleFromUserAggregateJson(userAggregateJson);
        assertEquals("applicationId", roles[0].getApplicationId());
        assertEquals("applicationId123", roles[1].getApplicationId());
    }
}
