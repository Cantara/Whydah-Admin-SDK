package net.whydah.sso.commands.adminapi.user;

import net.whydah.sso.commands.baseclasses.BaseHttpGetHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.URI;


public class CommandListUsersWithPagination extends BaseHttpGetHystrixCommand<String> {
   

    private String adminUserTokenId;
    private String userQuery;
    private String page;


    public CommandListUsersWithPagination(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String page, String userQuery) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", 6000);
    
        this.page = page;
        this.adminUserTokenId = adminUserTokenId;
        if (userQuery == null || userQuery.length() < 1) {
            userQuery = "*";
        }
        this.userQuery = userQuery;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || userQuery == null) {
            log.error("CommandListUsers initialized with null-values - will fail - userAdminServiceUri:{}, myAppTokenId:{}, adminUserTokenId:{}, userQuery:{}", userAdminServiceUri, myAppTokenId, adminUserTokenId, userQuery);

        }
    }

	@Override
	protected String getTargetPath() {
		return myAppTokenId + "/" + adminUserTokenId + "/users/query/" + page + "/" + userQuery;
	}


}
