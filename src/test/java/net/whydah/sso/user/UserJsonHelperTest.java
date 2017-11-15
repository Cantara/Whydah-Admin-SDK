package net.whydah.sso.user;


import static org.junit.Assert.assertTrue;
import net.whydah.sso.user.helpers.UserRoleJsonPathHelper;
import net.whydah.sso.user.helpers.UserRoleXpathHelper;
import net.whydah.sso.user.mappers.UserTokenMapper;
import net.whydah.sso.user.types.UserApplicationRoleEntry;
import net.whydah.sso.user.types.UserToken;

import org.junit.Test;

public class UserJsonHelperTest {

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
    public void testParseUserIdentityJson() throws Exception {
        String userJson = "{\"identity\":{\"username\":\"91905054\",\"firstName\":\"Thor Henning\",\"lastName\":\"Hetland\",\"personRef\":\"\",\"email\":\"totto@cantara.no\",\"cellPhone\":\"91905054\",\"uid\":\"00d07a25-efbe-484a-b00e-67859a106dd4\"},\"roles\":[],\"personName\":\"Thor Henning Hetland\",\"personRef\":\"\",\"uid\":\"00d07a25-efbe-484a-b00e-67859a106dd4\",\"lastName\":\"Hetland\",\"email\":\"totto@cantara.no\",\"firstName\":\"Thor Henning\",\"username\":\"91905054\",\"cellPhone\":\"91905054\"}";
        UserToken myToken = UserTokenMapper.fromUserIdentityJson(userJson);
        System.out.println(myToken);


    }

    @Test
    public void testUserAggregateTest() throws Exception {
        String userAggregateJson = "{\"uid\":\"useradmin\",\"username\":\"useradmin\",\"firstName\":\"UserAdmin\",\"lastName\":\"UserAdminWebApp\",\"personRef\":\"42\",\"email\":\"whydahadmin@getwhydah.com\",\"cellPhone\":\"87654321\",\"roles\": [{\"applicationId\":\"19\",\"applicationName\":\"\",\"applicationRoleName\":\"WhydahUserAdmin\",\"applicationRoleValue\":\"1\",\"organizationName\":\"\"}]}\n";
        UserToken myToken = UserTokenMapper.fromUserAggregateJson(userAggregateJson);
        System.out.println(myToken);
    }
    
    @Test
    public void testGetUserRoleFromUserAggregateJSON() throws Exception {
        UserApplicationRoleEntry roles[] = UserRoleJsonPathHelper.getUserRoleFromUserAggregateJson(userAggregateJson);
        assertTrue("applicationId".equals(roles[0].getApplicationId()));
        assertTrue("applicationId123".equals(roles[1].getApplicationId()));

    }


}
