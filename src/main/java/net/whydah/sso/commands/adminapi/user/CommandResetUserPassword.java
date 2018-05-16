package net.whydah.sso.commands.adminapi.user;

import java.net.URI;

import net.whydah.sso.commands.baseclasses.BaseHttpPostHystrixCommandForBooleanType;

public class CommandResetUserPassword extends BaseHttpPostHystrixCommandForBooleanType {

    private String userName;
    private String emailTemplateName = "";
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandResetUserPassword(URI userAdminServiceUri, String myAppTokenId, String userName) {
        super(userAdminServiceUri, null, myAppTokenId, "UASUserActionGroup", DEFAULT_TIMEOUT);

        this.userName = userName;
        if (userAdminServiceUri == null || userName == null) {
            log.error("CommandResetUserPassword initialized with null-values - will fail - userAdminServiceUri:{}, userName:{}, adminUserTokenId:{}, userQuery:{}", userAdminServiceUri, userName);

        }

    }
    
    public CommandResetUserPassword(URI userAdminServiceUri, String myAppTokenId, String userName, int timeout) {
        super(userAdminServiceUri, null, myAppTokenId, "UASUserActionGroup", timeout);

        this.userName = userName;
        if (userAdminServiceUri == null || userName == null) {
            log.error("CommandResetUserPassword initialized with null-values - will fail - userAdminServiceUri:{}, userName:{}, adminUserTokenId:{}, userQuery:{}", userAdminServiceUri, userName);

        }

    }

    public CommandResetUserPassword(URI userAdminServiceUri, String myAppTokenId, String userName, String emailTemplateName) {
        super(userAdminServiceUri, null, myAppTokenId, "UASUserActionGroup", DEFAULT_TIMEOUT);

        this.userName = userName;
        this.emailTemplateName = emailTemplateName;
        if (userAdminServiceUri == null || userName == null) {
            log.error("CommandResetUserPassword initialized with null-values - will fail - userAdminServiceUri:{}, userName:{}, adminUserTokenId:{}, userQuery:{}", userAdminServiceUri, userName);

        }

    }
    
    public CommandResetUserPassword(URI userAdminServiceUri, String myAppTokenId, String userName, String emailTemplateName, int timeout) {
        super(userAdminServiceUri, null, myAppTokenId, "UASUserActionGroup", timeout);

        this.userName = userName;
        this.emailTemplateName = emailTemplateName;
        if (userAdminServiceUri == null || userName == null) {
            log.error("CommandResetUserPassword initialized with null-values - will fail - userAdminServiceUri:{}, userName:{}, adminUserTokenId:{}, userQuery:{}", userAdminServiceUri, userName);

        }

    }

    @Override
    protected Boolean dealWithResponse(String response) {
        return response.length() < 32;
    }


    @Override
    protected String getTargetPath() {

        if (emailTemplateName == null || emailTemplateName.length() < 3) {
            return myAppTokenId + "/auth/password/reset/username/" + userName;
        }
        return myAppTokenId + "/auth/password/reset/username/" + userName + "/template/" + emailTemplateName;
    }

}
