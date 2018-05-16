package net.whydah.sso.commands.adminapi.user.role;

import java.net.URI;

import net.whydah.sso.commands.baseclasses.BaseHttpPostHystrixCommand;

import com.github.kevinsawicki.http.HttpRequest;


public class CommandAddUserRole extends BaseHttpPostHystrixCommand<String> {

    private String adminUserTokenId;
    private String userRoleJson;
    private String uId;
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandAddUserRole(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String uId,String roleJson) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);
        
        this.adminUserTokenId = adminUserTokenId;
        this.userRoleJson = roleJson;
        this.uId=uId;
        if (userAdminServiceUri == null || myAppTokenId == null || adminUserTokenId == null || uId == null || roleJson == null) {
            log.error(TAG + " initialized with null-values - will fail");
        }

        
    }

    public CommandAddUserRole(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String uId,String roleJson, int timeout) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);
        
        this.adminUserTokenId = adminUserTokenId;
        this.userRoleJson = roleJson;
        this.uId=uId;
        if (userAdminServiceUri == null || myAppTokenId == null || adminUserTokenId == null || uId == null || roleJson == null) {
            log.error(TAG + " initialized with null-values - will fail");
        }

        
    }
    
    
    @Override
    protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
    	return request.contentType("application/json").send(userRoleJson);
    }


	@Override
	protected String getTargetPath() {
		return myAppTokenId + "/" + adminUserTokenId + "/user/" +  uId + "/role";
	}


}

