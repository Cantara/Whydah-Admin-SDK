package net.whydah.sso.commands.adminapi.application;

import net.whydah.sso.commands.baseclasses.BaseHttpDeleteHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.URI;


public class CommandDeleteApplication extends BaseHttpDeleteHystrixCommand<String> {

    int retryCnt = 0;
    String applicationId;
    String tokenId;
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandDeleteApplication(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String applicationId) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || tokenId == null) {
            log.error(TAG + " initialized with null-values - will fail - userAdminServiceUri={}, myAppTokenId={}", userAdminServiceUri, myAppTokenId);
        }
        
        this.applicationId = applicationId;
        this.tokenId = adminUserTokenId;
    }
    
    public CommandDeleteApplication(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String applicationId, int timeout) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || tokenId == null) {
            log.error(TAG + " initialized with null-values - will fail - userAdminServiceUri={}, myAppTokenId={}", userAdminServiceUri, myAppTokenId);
        }
        
        this.applicationId = applicationId;
        this.tokenId = adminUserTokenId;
    }


    @Override
    protected String getTargetPath() {
        return myAppTokenId + "/" + tokenId + "/application/" + applicationId;
    }
    
    @Override
    protected String dealWithFailedResponse(String responseBody, int statusCode) {
    	if(statusCode == 204){
    		return Boolean.toString(true);
    	}
    	return Boolean.toString(false);
    }

}
