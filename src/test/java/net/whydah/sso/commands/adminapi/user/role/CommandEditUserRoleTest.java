package net.whydah.sso.commands.adminapi.user.role;

import net.whydah.sso.user.mappers.UserRoleMapper;
import net.whydah.sso.user.types.UserApplicationRoleEntry;
import net.whydah.sso.user.types.UserToken;
import net.whydah.sso.util.AdminSystemTestBaseConfig;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CommandEditUserRoleTest {
    private static final Logger log = LoggerFactory.getLogger(CommandEditUserRoleTest.class);

    public static AdminSystemTestBaseConfig config;

    @BeforeClass
    public static void setup() {
        config = new AdminSystemTestBaseConfig();
    }


    @Ignore   // Not working yet... some trouble with parsing/missing roleid it seems
    @Test
    public void testEditUserRole() {

        if (config.isSystemTestEnabled()) {

            UserToken adminUser = config.logOnSystemTestApplicationAndSystemTestUser();

            UserApplicationRoleEntry role = getTestNewUserRole(adminUser.getUid(), config.TEMPORARY_APPLICATION_ID);
            String userRoleJson = role.toJson();
            // URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String roleJson
            String userAddRoleResult = new CommandAddUserRole(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getUserTokenId(), adminUser.getUid(), userRoleJson).execute();
            log.debug("userAddRoleResult:" + userAddRoleResult);
            assertNotNull(userAddRoleResult);

            String userRolesJson = new CommandGetUserRoles(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getUserTokenId(), adminUser.getUid()).execute();
            log.debug("Roles returned:" + userRolesJson);
            assertTrue(userRolesJson.contains(role.getRoleName()));
            assertTrue(userRolesJson.contains(role.getRoleValue()));
            List<UserApplicationRoleEntry> roles = UserRoleMapper.fromJsonAsList(userRolesJson);
            for (UserApplicationRoleEntry irole : roles) {
                if (irole.getRoleName().equalsIgnoreCase(role.getRoleName())) {
                    if (role.getRoleName().toLowerCase().indexOf("admin") < 0) {  // Do not change UserAdmin roles
                        role.setId(irole.getId());
                    }
//                    log.debug("=====================>  match for "+role.getRoleName()+" in "+irole.getRoleName() +" - "+irole.getId());

                } else {
//                    log.debug("No match for "+role.getRoleName()+" in "+irole.getRoleName() +" - "+UserRoleMapper.toJson(irole));
                }

            }
            role.setRoleValue("newRolevalue-" + UUID.randomUUID());
            assertNotNull(role.getId());
            String editedUserRoleResult = new CommandUpdateUserRole(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getUserTokenId(), adminUser.getUid(), role.getId(), userRoleJson).execute();
            log.debug("returned: " + editedUserRoleResult);

            String newRolesJson = new CommandGetUserRoles(config.userAdminServiceUri, config.myApplicationToken.getApplicationTokenId(), adminUser.getUserTokenId(), adminUser.getUid()).execute();
            log.debug("Roles returned:" + userRolesJson);
            assertTrue(newRolesJson.contains(role.getRoleName()));
            assertTrue(newRolesJson.contains(role.getRoleValue()));
            List<UserApplicationRoleEntry> roles2 = UserRoleMapper.fromJsonAsList(newRolesJson);
            for (UserApplicationRoleEntry irole2 : roles2) {
                if (irole2.getRoleName().equalsIgnoreCase(role.getRoleName())) {
                    assertTrue(role.getRoleValue().equalsIgnoreCase(irole2.getRoleValue()));
//                    log.debug("=====================>  match for "+role.getRoleName()+" in "+irole.getRoleName() +" - "+irole.getId());

                } else {
//                    log.debug("No match for "+role.getRoleName()+" in "+irole.getRoleName() +" - "+UserRoleMapper.toJson(irole));
                }

            }
        }

    }

    private UserApplicationRoleEntry getTestNewUserRole(String userId, String applicationId) {
        UserApplicationRoleEntry role = new UserApplicationRoleEntry(userId, applicationId, "TestOrg-" + UUID.randomUUID(), "", "TestRoleName-" + UUID.randomUUID(), "TestRoleValue");
        return role;
    }
}


