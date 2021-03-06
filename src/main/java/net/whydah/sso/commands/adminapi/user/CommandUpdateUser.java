package net.whydah.sso.commands.adminapi.user;

import com.github.kevinsawicki.http.HttpRequest;
import net.whydah.sso.commands.baseclasses.BaseHttpPutHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.URI;

public class CommandUpdateUser extends BaseHttpPutHystrixCommand<String> {
  
    private String adminUserTokenId;
    private String userJson;
    private String uid;
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandUpdateUser(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String uid, String userJson) {
    	super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);
        
    	this.uid = uid;
        this.adminUserTokenId = adminUserTokenId;
        this.userJson = userJson;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || userJson == null) {
            log.error("CommandUpdateUser initialized with null-values - will fail");
        }

    }

    public CommandUpdateUser(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String uid, String userJson, int timeout) {
    	super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);
        
    	this.uid = uid;
        this.adminUserTokenId = adminUserTokenId;
        this.userJson = userJson;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || userJson == null) {
            log.error("CommandUpdateUser initialized with null-values - will fail");
        }

    }
    
    @Override
    protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
    	return request.contentType("application/json").send(userJson);
    }

//    @Override
//    protected String run() {
//        log.trace("CommandUpdateUser - myAppTokenId={}", myAppTokenId);
//
//        Client uasClient = ClientBuilder.newClient();
//
//        WebTarget updateUser = uasClient.target(userAdminServiceUri).path(myAppTokenId).path(adminUserTokenId).path("/xxx");
//        Response response = updateUser.request().post(Entity.json(userJson));
//        throw new UnsupportedOperationException();
//        //return null;
//
//    }

//    @Override
//    protected String getFallback() {
//        log.warn("CommandUpdateUser - fallback - whydahServiceUri={}", userAdminServiceUri.toString());
//        return null;
//    }

	@Override
	protected String getTargetPath() {
		return myAppTokenId + "/" + adminUserTokenId  +  "/user/" + uid;
	}


}
