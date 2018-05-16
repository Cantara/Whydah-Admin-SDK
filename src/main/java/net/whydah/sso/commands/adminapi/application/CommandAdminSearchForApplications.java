package net.whydah.sso.commands.adminapi.application;

import com.github.kevinsawicki.http.HttpRequest;
import net.whydah.sso.commands.baseclasses.BaseHttpGetHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.URI;


public class CommandAdminSearchForApplications extends BaseHttpGetHystrixCommand<String> {

	public static int DEFAULT_TIMEOUT = 6000;
    private String applicationQuery;
    private String adminUserTokenId = null;

    public CommandAdminSearchForApplications(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String applicationQuery) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);
        this.applicationQuery = applicationQuery;
        this.adminUserTokenId = adminUserTokenId;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || applicationQuery == null || adminUserTokenId == null) {
            log.error(TAG + " initialized with null-values - will fail");
        }
    }
    
    public CommandAdminSearchForApplications(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String applicationQuery, int timeout) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);
        this.applicationQuery = applicationQuery;
        this.adminUserTokenId = adminUserTokenId;
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || applicationQuery == null || adminUserTokenId == null) {
            log.error(TAG + " initialized with null-values - will fail");
        }
    }



    @Override
    protected String getTargetPath() {
        return myAppTokenId + "/find/applications/" + applicationQuery;
//        return myAppTokenId +  "/" + adminUserTokenId + "/applications/find/" + applicationQuery;
    }

    @Override
    protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
        // return request.contentType("application/json").send(applicationQuery);
        return super.dealWithRequestBeforeSend(request);
    }

}
