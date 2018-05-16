package net.whydah.sso.commands.adminapi.user.role;

import java.net.URI;

import net.whydah.sso.commands.baseclasses.BaseHttpGetHystrixCommand;

public class CommandGetUserRoles extends BaseHttpGetHystrixCommand<String> {

    private String myAppTokenId;
    private String adminUserTokenId;
    private String userID;
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandGetUserRoles(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String userID) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);


        this.adminUserTokenId = adminUserTokenId;
        this.myAppTokenId = myAppTokenId;
        this.userID = userID;
        if (userAdminServiceUri == null || myAppTokenId == null || adminUserTokenId == null || userID == null) {
            log.error(TAG + " initialized with null-values - will fail");
        }

    }
    
    public CommandGetUserRoles(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String userID, int timeout) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);


        this.adminUserTokenId = adminUserTokenId;
        this.myAppTokenId = myAppTokenId;
        this.userID = userID;
        if (userAdminServiceUri == null || myAppTokenId == null || adminUserTokenId == null || userID == null) {
            log.error(TAG + " initialized with null-values - will fail");
        }

    }

//    @Override
//    protected String run() {
//        log.trace("CommandGetUser - myAppTokenId={}", myAppTokenId);
//
//        Client uasClient = ClientBuilder.newClient();
//
//        WebTarget getUser = uasClient.target(userAdminServiceUri).path(myAppTokenId).path(adminUserTokenId).path("user").path(userID);
//        Response response = getUser.request().get();
//        if (response.getStatus() == OK.getStatusCode()) {
//            String responseJson = response.readEntity(String.class);
//            log.debug("CommandGetUser - Returning user {}", responseJson);
//            return responseJson;
//        }
//        return null;
//
//
//    }
//
//    @Override
//    protected String getFallback() {
//        log.warn("CommandGetUser - fallback - whydahServiceUri={}", userAdminServiceUri.toString());
//        return null;
//    }

    @Override
    protected String getTargetPath() {
        return myAppTokenId + "/" + adminUserTokenId + "/user" + "/" + userID + "/roles";
    }


}
