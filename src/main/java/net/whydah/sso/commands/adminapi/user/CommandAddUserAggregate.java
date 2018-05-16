package net.whydah.sso.commands.adminapi.user;

import com.github.kevinsawicki.http.HttpRequest;
import net.whydah.sso.commands.baseclasses.BaseHttpPostHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.URI;

public class CommandAddUserAggregate extends BaseHttpPostHystrixCommand<String> {

    private String adminUserTokenId;
    private String userAggregateJson;
    public static int DEFAULT_TIMEOUT = 6000;
    
    public CommandAddUserAggregate(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String userAggregateJson) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);
      
        this.adminUserTokenId = adminUserTokenId;
        this.userAggregateJson = userAggregateJson;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || userAggregateJson == null) {
            log.error(TAG + " initialized with null-values - will fail - userAdminServiceUri:{}, myAppTokenId:{}, adminUserTokenId:{}, userAggregateJson:{}", userAdminServiceUri, myAppTokenId, adminUserTokenId, userAggregateJson);
        }
    }
    
    public CommandAddUserAggregate(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String userAggregateJson, int timeout) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);
      
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

//    @Override
//    protected String run() {
//
//        log.trace("CommandAddUserAggregate - myAppTokenId={}, adminUserTokenId={{}, userAggregateJson={}", myAppTokenId, adminUserTokenId, userAggregateJson);
//        Client tokenServiceClient = ClientBuilder.newClient();
//        WebTarget addUser = tokenServiceClient.target(userAdminServiceUri).path(myAppTokenId).path(adminUserTokenId).path("useraggregate");
//        Response response = addUser.request().post(Entity.json(userAggregateJson));
//        if (response.getStatus() == OK.getStatusCode()) {
//            String responseXML = response.readEntity(String.class);
//            log.info("CommandAddUserAggregate - addUser - Create user OK with response {}", responseXML);
//            return responseXML;
//        }
//
//        log.warn("CommandAddUserAggregate - addUser - failed with status code " + response.getStatus());
//        return null;
//
//    }
//
//    @Override
//    protected String getFallback() {
//        log.warn("CommandAddUserAggregate - fallback - whydahServiceUri={}", userAdminServiceUri.toString());
//        return null;
//    }


}
