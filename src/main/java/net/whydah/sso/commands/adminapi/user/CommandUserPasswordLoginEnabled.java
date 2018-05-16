package net.whydah.sso.commands.adminapi.user;

import net.whydah.sso.commands.baseclasses.BaseHttpGetHystrixCommandForBooleanType;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;

import java.net.URI;

public class CommandUserPasswordLoginEnabled extends BaseHttpGetHystrixCommandForBooleanType {

    private String userName;
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandUserPasswordLoginEnabled(URI userAdminServiceUri, String myAppTokenId, String userName) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);
//        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", 6000);

        this.userName = userName;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || userName == null) {
            log.error("CommandUserPasswordLoginEnabled initialized with null-values - will fail - userAdminServiceUri:{}, myAppTokenId:{}, userName:{}", userAdminServiceUri, myAppTokenId, userName);

        }

    }

    public CommandUserPasswordLoginEnabled(URI userAdminServiceUri, String myAppTokenId, String userName, int timeout) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);
//        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", 6000);

        this.userName = userName;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || userName == null) {
            log.error("CommandUserPasswordLoginEnabled initialized with null-values - will fail - userAdminServiceUri:{}, myAppTokenId:{}, userName:{}", userAdminServiceUri, myAppTokenId, userName);

        }

    }
    
    @Override
    protected Boolean dealWithResponse(String response) {
        return Boolean.valueOf(response);
    }


    @Override
    protected String getTargetPath() {
        return myAppTokenId + "/user/" + userName + "/password_login_enabled";
    }


}
