package net.whydah.sso.util;

import net.whydah.sso.commands.adminapi.user.CommandAddUser;
import net.whydah.sso.commands.adminapi.user.role.CommandAddUserRole;
import net.whydah.sso.user.mappers.UserIdentityMapper;
import net.whydah.sso.user.mappers.UserRoleMapper;
import net.whydah.sso.user.types.UserApplicationRoleEntry;
import net.whydah.sso.user.types.UserIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class WhydahAdminUtil extends WhydahUtil {
    private static final Logger log = LoggerFactory.getLogger(WhydahAdminUtil.class);






    /**
     * @param uasUri             URI to the User Admin Service
     * @param applicationTokenId TokenId fetched from the XML in logOnApplication
     * @param adminUserTokenId   TokenId fetched from the XML returned in logOnApplicationAndUser
     * @param userIdentity       The user identity you want to create.
     * @return UserIdentityXml
     */
    public static String addUser(String uasUri, String applicationTokenId, String adminUserTokenId, UserIdentity userIdentity) {
        String userId = null;

        String userIdentityJson = UserIdentityMapper.toJsonWithoutUID(userIdentity);
        // URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String roleJson
        String userAddRoleResult = new CommandAddUser(URI.create(uasUri), applicationTokenId, adminUserTokenId, userIdentityJson).execute();

        if (userAddRoleResult != null && userAddRoleResult.length() > 10) {
            log.debug("CommandAddUser - addUser - Log on OK with response {}", userAddRoleResult);
            return userAddRoleResult;
        }
        throw new IllegalArgumentException("Not found");
    }


    /**
     * @param uasUri             URI to the User Admin Service
     * @param applicationTokenId TokenId fetched from the XML in logOnApplication
     * @param adminUserTokenId   TokenId fetched from the XML returned in logOnApplicationAndUser
     * @param roles              List of roles to be created.
     * @return List of the roles that has been creatd. Empty list if no roles were created.
     */
    public static List<UserApplicationRoleEntry> addRolesToUser(String uasUri, String applicationTokenId, String adminUserTokenId, List<UserApplicationRoleEntry> roles) {

        List<String> createdRolesXml = new ArrayList<>();
        List<UserApplicationRoleEntry> createdRoles = new ArrayList<>();

        String userName = "";
        for (UserApplicationRoleEntry role : roles) {
            String roleXml = role.toXML();
            log.trace("Try to add role {}", roleXml);
            userName = role.getUserName();
            String userAddRoleResult = new CommandAddUserRole(URI.create(uasUri), applicationTokenId, adminUserTokenId, role.getUserId(), UserRoleMapper.toJson(role)).execute();

            if (userAddRoleResult != null && userAddRoleResult.length() > 5) {
                log.debug("CommandAddRole - addRoles - Created role ok {}", userAddRoleResult);
                createdRolesXml.add(userAddRoleResult);
            } else {
                //createdRolesXml.add("Failed to add role " + role.getRoleName() + ", reason: " + response.toString());
                log.trace("Failed to add role {}, {}", role.toString(), userAddRoleResult);
            }
        }
        for (String createdRoleXml : createdRolesXml) {
            UserApplicationRoleEntry createdUserRole = UserApplicationRoleEntry.fromXml(createdRoleXml);
            createdRoles.add(createdUserRole);
        }
        return createdRoles;
    }





}
