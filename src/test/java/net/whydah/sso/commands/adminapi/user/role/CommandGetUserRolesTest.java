package net.whydah.sso.commands.adminapi.user.role;

import net.whydah.sso.user.mappers.UserRoleMapper;
import net.whydah.sso.user.types.UserApplicationRoleEntry;
import net.whydah.sso.user.types.UserToken;
import net.whydah.sso.util.AdminSystemTestBaseConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommandGetUserRolesTest {
    public static AdminSystemTestBaseConfig config;

    private static final Logger log = LoggerFactory.getLogger(CommandGetUserRolesTest.class);

    @BeforeClass
    public static void setup() {
        config = new AdminSystemTestBaseConfig();
    }


    @Test
    public void testListUserRoles() {
        if (config.isSystemTestEnabled()) {
            UserToken adminUser = config.logOnSystemTestApplicationAndSystemTestUser();
            String userRolesJson = new CommandGetUserRoles(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getUserTokenId(), adminUser.getUid()).execute();
            log.debug("Roles returned:" + userRolesJson);
            List<UserApplicationRoleEntry> roles = UserRoleMapper.fromJsonAsList(userRolesJson);
            log.debug("Last role" + roles.listIterator().next());
        }
    }
    //  @Path("/{uid}/roles")
}
