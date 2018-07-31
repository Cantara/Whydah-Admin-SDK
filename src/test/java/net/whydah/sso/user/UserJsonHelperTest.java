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

    String userAggregateJson = "{\n" +
            "  \"uid\": \"uid\",\n" +
            "  \"username\": \"usernameABC\",\n" +
            "  \"firstName\": \"firstName\",\n" +
            "  \"lastName\": \"lastName\",\n" +
            "  \"personRef\": \"personRef\",\n" +
            "  \"email\": \"email\",\n" +
            "  \"cellPhone\": \"12345678\",\n" +
            "  \"password\": \"password\",\n" +
            "  \"roles\": [\n" +
            "    {\n" +
            "      \"applicationId\": \"applicationId\",\n" +
            "      \"applicationName\": \"applicationName\",\n" +
            "      \"organizationId\": \"organizationId\",\n" +
            "      \"organizationName\": \"organizationName\",\n" +
            "      \"applicationRoleName\": \"roleName\",\n" +
            "      \"applicationRoleValue\": \"email\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"applicationId\": \"applicationId123\",\n" +
            "      \"applicationName\": \"applicationName123\",\n" +
            "      \"organizationId\": \"organizationId123\",\n" +
            "      \"organizationName\": \"organizationName123\",\n" +
            "      \"applicationRoleName\": \"roleName123\",\n" +
            "      \"applicationRoleValue\": \"roleValue123\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
	
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
