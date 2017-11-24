package net.whydah.sso.commands.adminapi.application;

import com.github.kevinsawicki.http.HttpRequest;
import net.whydah.sso.commands.baseclasses.BaseHttpPostHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.URI;


public class CommandAddApplication extends BaseHttpPostHystrixCommand<String> {
  
    
    private String adminUserTokenId;
    private String applicationJson;


    public CommandAddApplication(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String applicationJson) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", 60000);
      
        this.adminUserTokenId = adminUserTokenId;
        this.applicationJson = applicationJson;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || applicationJson == null) {
            log.error(TAG + " initialized with null-values - will fail");
        }

    }
    
    @Override
    protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
    	return request.contentType("application/json").send(applicationJson);
    }



	@Override
	protected String getTargetPath() {
		return myAppTokenId + "/" + adminUserTokenId + "/application/";
	}

}
