package net.whydah.sso.commands.adminapi.user;

import net.whydah.sso.commands.baseclasses.BaseHttpGetHystrixCommandForBooleanType;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.URI;

public class CommandUserExists extends BaseHttpGetHystrixCommandForBooleanType {
  
    private String adminUserTokenId;
    private String username;
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandUserExists(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String username) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);
       
        this.adminUserTokenId = adminUserTokenId;
        this.username = username;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || adminUserTokenId.length() < 10 || username == null) {
            log.error("CommandUserExists initialized with null-values - will fail - userAdminServiceUri:{}, myAppTokenId:{}, adminUserTokenId:{}, userQuery:{}", userAdminServiceUri, myAppTokenId, adminUserTokenId, username);

        }

    }
    public CommandUserExists(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String userQuery, int timeout) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);
       
        this.adminUserTokenId = adminUserTokenId;
        this.username = userQuery;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || adminUserTokenId.length() < 10 || userQuery == null) {
            log.error("CommandUserExists initialized with null-values - will fail - userAdminServiceUri:{}, myAppTokenId:{}, adminUserTokenId:{}, userQuery:{}", userAdminServiceUri, myAppTokenId, adminUserTokenId, userQuery);

        }

    }
    
    //make compatible with older version of UIB's response
    @Override
    protected Boolean dealWithResponse(String response) {
    	return response.contentEquals("{\"result\":true}")  || response.contentEquals("true");
    }
    
    @Override
    protected Boolean dealWithFailedResponse(String responseBody, int statusCode) {
    	return false;
    }


	@Override
	protected String getTargetPath() {
		return myAppTokenId + "/" + adminUserTokenId + "/users" + "/checkexist/" + username;
	}


}
