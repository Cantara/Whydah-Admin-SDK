package net.whydah.sso.commands.adminapi.application;


import com.github.kevinsawicki.http.HttpRequest;
import net.whydah.sso.commands.baseclasses.BaseHttpPutHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.URI;

// TODO:  wait for https://github.com/Cantara/Whydah-UserAdminService/issues/35

public class CommandUpdateApplication extends BaseHttpPutHystrixCommand<String> {

  
	public static int DEFAULT_TIMEOUT = 6000;
    private String adminUserTokenId;
    private String applicationJson;
    private String applicationId;

    public CommandUpdateApplication(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String applicationId, String applicationJson) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);
        
    
        this.adminUserTokenId = adminUserTokenId;
        this.applicationJson = applicationJson;
        this.applicationId = applicationId;

        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || applicationJson == null) {
            log.error(TAG + " initialized with null-values - will fail");
        }

    }
    
    public CommandUpdateApplication(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String applicationId, String applicationJson, int timeout) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);
        
    
        this.adminUserTokenId = adminUserTokenId;
        this.applicationJson = applicationJson;
        this.applicationId = applicationId;

        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || applicationJson == null) {
            log.error(TAG + " initialized with null-values - will fail");
        }

    }
    
    @Override
    protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
    	return request.contentType("application/json").send(applicationJson);
    }
    
    @Override
    protected String dealWithFailedResponse(String responseBody, int statusCode) {
    	if(statusCode == 204){
    		return applicationJson;
    	}
    	return null;
    }
    
    @Override
    protected String getTargetPath() {
    	return myAppTokenId + "/" + adminUserTokenId + "/application/" + applicationId;
    }


}
