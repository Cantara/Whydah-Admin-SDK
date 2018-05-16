package net.whydah.sso.commands.adminapi.application;

import net.whydah.sso.commands.baseclasses.BaseHttpGetHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.URI;


public class CommandGetApplication extends BaseHttpGetHystrixCommand<String> {

    int retryCnt = 0;
    String applicationId;
    String adminUserTokenId;
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandGetApplication(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String applicationId) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || adminUserTokenId == null) {
            log.error(TAG + " initialized with null-values - will fail - userAdminServiceUri={}, myAppTokenId={}", userAdminServiceUri, myAppTokenId);
        }
        
        this.applicationId = applicationId;
        this.adminUserTokenId = adminUserTokenId;
    }
    
    public CommandGetApplication(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String applicationId, int timeout) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || adminUserTokenId == null) {
            log.error(TAG + " initialized with null-values - will fail - userAdminServiceUri={}, myAppTokenId={}", userAdminServiceUri, myAppTokenId);
        }
        
        this.applicationId = applicationId;
        this.adminUserTokenId = adminUserTokenId;
    }


    @Override
    protected String getTargetPath() {
        return myAppTokenId + "/" + adminUserTokenId + "/application/" + applicationId;
    }


}
