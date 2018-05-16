package net.whydah.sso.internal.commands.uib.userauth;

import com.github.kevinsawicki.http.HttpRequest;
import net.whydah.sso.commands.baseclasses.BaseHttpPostHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UID;

import java.net.URI;

public class CommandChangeUserPasswordUsingToken extends BaseHttpPostHystrixCommand<String> {
    static final String CHANGE_USERCRED_TOKEN_KEY = "changePasswordToken";

    public static int DEFAULT_TIMEOUT = 6000;
    
    private String uid;
    private String changePasswordToken;
    private String json;


    public CommandChangeUserPasswordUsingToken(String uibUri, String applicationtokenId, String uid, String changePasswordToken, String json) {
    	super(URI.create(uibUri),"", applicationtokenId,"SSOAUserAuthGroup", DEFAULT_TIMEOUT);
       
        this.uid = uid;
        this.changePasswordToken = changePasswordToken;
        this.json = json;
        if (uibUri == null || !ApplicationTokenID.isValid(applicationtokenId) || !UID.isValid(uid) || changePasswordToken == null || json == null) {
            log.error("{} initialized with null-values - will fail", CommandChangeUserPasswordUsingToken.class.getSimpleName());
        }
    }
    
    public CommandChangeUserPasswordUsingToken(String uibUri, String applicationtokenId, String uid, String changePasswordToken, String json, int timeout) {
    	super(URI.create(uibUri),"", applicationtokenId,"SSOAUserAuthGroup", timeout);
       
        this.uid = uid;
        this.changePasswordToken = changePasswordToken;
        this.json = json;
        if (uibUri == null || !ApplicationTokenID.isValid(applicationtokenId) || !UID.isValid(uid) || changePasswordToken == null || json == null) {
            log.error("{} initialized with null-values - will fail", CommandChangeUserPasswordUsingToken.class.getSimpleName());
        }
    }


    @Override
    protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
    	return request.contentType("application/json").send(json);
    }

    @Override
    protected Object[] getQueryParameters() {
        return new String[]{CHANGE_USERCRED_TOKEN_KEY, changePasswordToken};
    }
    
	@Override
	protected String getTargetPath() {
		return myAppTokenId + "/user/" + uid + "/change_password";
	}
}
