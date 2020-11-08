package net.whydah.sso.commands.adminapi.user;

import net.whydah.sso.commands.baseclasses.BaseHttpGetHystrixCommandForBooleanType;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.URI;

public class CommandUserExists extends BaseHttpGetHystrixCommandForBooleanType {
  
    private String adminUserTokenId;
    private String userQuery;
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandUserExists(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String userQuery) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);
       
        this.adminUserTokenId = adminUserTokenId;
        this.userQuery = userQuery;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || adminUserTokenId.length() < 10 || userQuery == null) {
            log.error("CommandUserExists initialized with null-values - will fail - userAdminServiceUri:{}, myAppTokenId:{}, adminUserTokenId:{}, userQuery:{}", userAdminServiceUri, myAppTokenId, adminUserTokenId, userQuery);

        }

    }
    public CommandUserExists(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String userQuery, int timeout) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);
       
        this.adminUserTokenId = adminUserTokenId;
        this.userQuery = userQuery;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || adminUserTokenId.length() < 10 || userQuery == null) {
            log.error("CommandUserExists initialized with null-values - will fail - userAdminServiceUri:{}, myAppTokenId:{}, adminUserTokenId:{}, userQuery:{}", userAdminServiceUri, myAppTokenId, adminUserTokenId, userQuery);

        }

    }
    
    @Override
    protected Boolean dealWithResponse(String response) {
    	return response.contentEquals("true");
    }


	@Override
	protected String getTargetPath() {
		return myAppTokenId + "/" + adminUserTokenId + "/users" + "/checkexist/" + userQuery;
	}


}
