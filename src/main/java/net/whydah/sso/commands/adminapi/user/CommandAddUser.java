package net.whydah.sso.commands.adminapi.user;

import com.github.kevinsawicki.http.HttpRequest;
import net.whydah.sso.commands.baseclasses.BaseHttpPostHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.URI;

public class CommandAddUser extends BaseHttpPostHystrixCommand<String> {
    
    private String adminUserTokenId;
    private String userIdentityJson;
    public static int DEFAULT_TIMEOUT = 6000;
    
    public CommandAddUser(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String userIdentityJson) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", 2000);

        this.adminUserTokenId=adminUserTokenId;
        this.userIdentityJson =userIdentityJson;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || userIdentityJson == null) {
            log.error(TAG + " initialized with null-values - will fail - userAdminServiceUri:{}, myAppTokenId:{}, adminUserTokenId:{}, userIdentityJson:{}", userAdminServiceUri, myAppTokenId, adminUserTokenId, userIdentityJson);
        }
    }
    public CommandAddUser(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String userIdentityJson, int timeout) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);

        this.adminUserTokenId=adminUserTokenId;
        this.userIdentityJson =userIdentityJson;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || userIdentityJson == null) {
            log.error(TAG + " initialized with null-values - will fail - userAdminServiceUri:{}, myAppTokenId:{}, adminUserTokenId:{}, userIdentityJson:{}", userAdminServiceUri, myAppTokenId, adminUserTokenId, userIdentityJson);
        }
    }

    
    @Override
    protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
    	return request.contentType("application/json").send(userIdentityJson);
    }

	@Override
	protected String getTargetPath() {
		return myAppTokenId + "/" + adminUserTokenId  +  "/user";
	}


}
