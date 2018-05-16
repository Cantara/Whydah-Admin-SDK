package net.whydah.sso.commands.adminapi.user;

import java.net.URI;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.github.kevinsawicki.http.HttpRequest;

import net.whydah.sso.commands.baseclasses.BaseHttpPostHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

public class CommandCheckDuplicateUsers extends BaseHttpPostHystrixCommand<String> {
	 
    private String adminUserTokenId;
    private List<String> userNames;
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandCheckDuplicateUsers(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, List<String> userNames) {
    	super(userAdminServiceUri, "", myAppTokenId,"UASUserAdminGroup", DEFAULT_TIMEOUT);
    	
        
        this.adminUserTokenId = adminUserTokenId;
        this.userNames = userNames;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || userNames==null) {
            log.error(TAG + " initialized with null-values - will fail");
        }

    }
    public CommandCheckDuplicateUsers(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, List<String> userNames, int timeout) {
    	super(userAdminServiceUri, "", myAppTokenId,"UASUserAdminGroup", timeout);
    	
        
        this.adminUserTokenId = adminUserTokenId;
        this.userNames = userNames;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || userNames==null) {
            log.error(TAG + " initialized with null-values - will fail");
        }

    }
    
    @Override
    protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
    	String json = "[" + (userNames.size()>0 ? StringUtils.join(userNames, ','):"") + "]";
    	return request.contentType("application/json").send(json);
    }

	@Override
	protected String getTargetPath() {
        return myAppTokenId + "/" + adminUserTokenId + "/users/checkduplicates";
    }


}
