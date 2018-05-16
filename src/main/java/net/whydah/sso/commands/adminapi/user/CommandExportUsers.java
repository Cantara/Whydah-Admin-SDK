package net.whydah.sso.commands.adminapi.user;

import java.net.URI;

import net.whydah.sso.commands.baseclasses.BaseHttpGetHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

public class CommandExportUsers extends BaseHttpGetHystrixCommand<String> {
	 
    private String adminUserTokenId;
    private String page;
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandExportUsers(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String page) {
    	super(userAdminServiceUri, "", myAppTokenId,"UASUserAdminGroup", DEFAULT_TIMEOUT);
    	
        
        this.adminUserTokenId = adminUserTokenId;
        this.page = page;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId)) {
            log.error(TAG + " initialized with null-values - will fail");
        }

    }
    
    public CommandExportUsers(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String page, int timeout) {
    	super(userAdminServiceUri, "", myAppTokenId,"UASUserAdminGroup", timeout);
    	
        
        this.adminUserTokenId = adminUserTokenId;
        this.page = page;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId)) {
            log.error(TAG + " initialized with null-values - will fail");
        }

    }


	@Override
	protected String getTargetPath() {
        return myAppTokenId + "/" + adminUserTokenId + "/users/export/" + page;
    }


}
