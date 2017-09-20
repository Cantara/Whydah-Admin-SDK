package net.whydah.sso.commands.adminapi.application;

import net.whydah.sso.commands.baseclasses.BaseHttpGetHystrixCommand;

import java.net.URI;


public class CommandGetApplication extends BaseHttpGetHystrixCommand<String> {

    int retryCnt = 0;
    String applicationId;
    String adminUserTokenId;

    public CommandGetApplication(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String applicationId) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", 3000);
        if (userAdminServiceUri == null || myAppTokenId == null || applicationId == null || adminUserTokenId == null) {
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
