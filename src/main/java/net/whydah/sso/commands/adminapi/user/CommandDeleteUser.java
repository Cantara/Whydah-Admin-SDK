package net.whydah.sso.commands.adminapi.user;

import java.net.URI;

import net.whydah.sso.commands.baseclasses.BaseHttpDeleteHystrixCommandForBooleanType;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

public class CommandDeleteUser extends BaseHttpDeleteHystrixCommandForBooleanType  {

	private String adminUserTokenId;

	private String uid;

	public static int DEFAULT_TIMEOUT = 6000;
	
	public CommandDeleteUser(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String uid) {
		super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);

		this.uid = uid;
		this.adminUserTokenId = adminUserTokenId;

		if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId)) {
			log.error("CommandDeleteUser initialized with null-values - will fail");
		}

	}
	public CommandDeleteUser(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String uid, int timeout) {
		super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);

		this.uid = uid;
		this.adminUserTokenId = adminUserTokenId;

		if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId)) {
			log.error("CommandDeleteUser initialized with null-values - will fail");
		}

	}

	@Override
	protected Boolean dealWithResponse(String response) {
		return false;
	}
	
	@Override
	protected Boolean dealWithFailedResponse(String responseBody, int statusCode) {
		if(statusCode == 204) {
			return true;
		}
		return super.dealWithFailedResponse(responseBody, statusCode);
	}

	@Override
	protected String getTargetPath() {
		return myAppTokenId + "/" + adminUserTokenId  +  "/user/" + uid;
	}

}
