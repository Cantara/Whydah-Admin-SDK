package net.whydah.sso.commands.adminapi.user;


import net.whydah.sso.commands.baseclasses.BaseHttpGetHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.URI;

public class CommandGetUserAggregate extends BaseHttpGetHystrixCommand<String> {
    
    private String adminUserTokenId;
    private String userID;
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandGetUserAggregate(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String userID) {
    	super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);
        
        this.adminUserTokenId = adminUserTokenId;
        this.userID = userID;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || userID == null) {
            log.error(TAG + " initialized with null-values - will fail");
        }

    }
    
    public CommandGetUserAggregate(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String userID, int timeout) {
    	super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);
        
        this.adminUserTokenId = adminUserTokenId;
        this.userID = userID;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || userID == null) {
            log.error(TAG + " initialized with null-values - will fail");
        }

    }
//
//    @Override
//    protected String run() {
//        log.trace("CommandGetUserAggregate - myAppTokenId={}", myAppTokenId);
//
//        Client uasClient = ClientBuilder.newClient();
//
//        WebTarget updateUser = uasClient.target(userAdminServiceUri).path(myAppTokenId).path(adminUserTokenId).path("useraggregate").path(userID);
//        Response response = updateUser.request().get();
//        if (response.getStatus() == OK.getStatusCode()) {
//            String responseJson = response.readEntity(String.class);
//            log.debug("CommandGetUserAggregate - Returning user {}", responseJson);
//            return responseJson;
//        }
//        return null;
//
//
//    }

//    @Override
//    protected String getFallback() {
//        log.warn("CommandGetUserAggregate - fallback - whydahServiceUri={}", userAdminServiceUri.toString());
//        return null;
//    }

	@Override
	protected String getTargetPath() {
		return myAppTokenId + "/" + adminUserTokenId + "/useraggregate" + "/" + userID;
	}


}
