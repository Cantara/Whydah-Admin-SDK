package net.whydah.sso.commands.adminapi.user.role;

import java.util.List;

import net.whydah.sso.user.mappers.UserRoleMapper;
import net.whydah.sso.user.types.UserApplicationRoleEntry;
import net.whydah.sso.user.types.UserToken;
import net.whydah.sso.util.AdminSystemTestBaseConfig;

import org.junit.BeforeClass;
import org.junit.Test;

public class CommandGetUserRolesTest {


    public static AdminSystemTestBaseConfig config;


    @BeforeClass
    public static void setup() throws Exception {
        config = new AdminSystemTestBaseConfig();
    }


    @Test
    public void testListUserRoles() throws Exception {

        if (config.isSystemTestEnabled()) {

            UserToken adminUser = config.logOnSystemTestApplicationAndSystemTestUser();
            String userRolesJson = new CommandGetUserRoles(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getTokenid(), adminUser.getUid()).execute();
            System.out.println("Roles returned:" + userRolesJson);
            List<UserApplicationRoleEntry> roles = UserRoleMapper.fromJsonAsList(userRolesJson);
            System.out.println("Last role" + roles.listIterator().next());

        }
    }


    //  @Path("/{uid}/roles")
}
