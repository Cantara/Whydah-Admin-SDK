package net.whydah.sso.commands.adminapi.user;

import java.net.URI;

import com.github.kevinsawicki.http.HttpRequest;

import net.whydah.sso.commands.baseclasses.BaseHttpPutHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

public class CommandUpdateUserAggregate extends BaseHttpPutHystrixCommand<String> {

    private String adminUserTokenId;
    private String userAggregateJson;

    public CommandUpdateUserAggregate(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String userAggregateJson) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup");
      
        this.adminUserTokenId = adminUserTokenId;
        this.userAggregateJson = userAggregateJson;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || userAggregateJson == null) {
            log.error(TAG + " initialized with null-values - will fail - userAdminServiceUri:{}, myAppTokenId:{}, adminUserTokenId:{}, userAggregateJson:{}", userAdminServiceUri, myAppTokenId, adminUserTokenId, userAggregateJson);
        }
    }

	@Override
	protected String getTargetPath() {
		return myAppTokenId + "/" + adminUserTokenId + "/useraggregate";
	}
	
	@Override
	protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
		return request.contentType("application/json").send(userAggregateJson);
		
	}


}
