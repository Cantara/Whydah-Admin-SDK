package net.whydah.sso.commands.adminapi.user;

import net.whydah.sso.commands.baseclasses.BaseHttpGetHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.URI;

public class CommandGetUser extends BaseHttpGetHystrixCommand<String> {
 
    private String adminUserTokenId;
    private String userId;


    public CommandGetUser(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String userID) {
    	super(userAdminServiceUri, "", myAppTokenId,"UASUserAdminGroup", 3000);
    	
        
        this.adminUserTokenId = adminUserTokenId;
        this.userId = userID;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || userID == null) {
            log.error(TAG + " initialized with null-values - will fail");
        }

    }


	@Override
	protected String getTargetPath() {
        return myAppTokenId + "/" + adminUserTokenId + "/user/" + userId;
    }


}
