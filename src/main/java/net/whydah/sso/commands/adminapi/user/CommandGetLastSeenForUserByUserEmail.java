package net.whydah.sso.commands.adminapi.user;

import net.whydah.sso.commands.baseclasses.BaseHttpGetHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;

import java.net.URI;


public class CommandGetLastSeenForUserByUserEmail extends BaseHttpGetHystrixCommand<String> {

    private String userEmail;
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandGetLastSeenForUserByUserEmail(URI tokenServiceUri, String myAppTokenId, String userEmail) {
    	super(tokenServiceUri, "", myAppTokenId, "STSUserQueries", DEFAULT_TIMEOUT);
        
        this.userEmail = userEmail;
        if (tokenServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || userEmail == null) {
            log.error(TAG + " initialized with null-values - will fail tokenServiceUri:{} myAppTokenId:{}, userEmail:{}", tokenServiceUri, myAppTokenId, userEmail);
        }

    }
    public CommandGetLastSeenForUserByUserEmail(URI tokenServiceUri, String myAppTokenId, String userEmail, int timeout) {
    	super(tokenServiceUri, "", myAppTokenId, "STSUserQueries", timeout);
        
        this.userEmail = userEmail;
        if (tokenServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || userEmail == null) {
            log.error(TAG + " initialized with null-values - will fail tokenServiceUri:{} myAppTokenId:{}, userEmail:{}", tokenServiceUri, myAppTokenId, userEmail);
        }

    }

	@Override
	protected String getTargetPath() {
        return "user/" + myAppTokenId + "/" + userEmail + "/last_seen";
    }


}
