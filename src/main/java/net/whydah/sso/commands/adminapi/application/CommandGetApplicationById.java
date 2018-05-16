package net.whydah.sso.commands.adminapi.application;

import com.github.kevinsawicki.http.HttpRequest;
import net.whydah.sso.commands.baseclasses.BaseHttpGetHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UserTokenId;

import java.net.HttpURLConnection;
import java.net.URI;


public class CommandGetApplicationById extends BaseHttpGetHystrixCommand<String> {

    private final String adminUserTokenId;
    private final String applicationId;
    int retryCnt = 0;
    public static int DEFAULT_TIMEOUT = 6000;

    public CommandGetApplicationById(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String applicationId) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", DEFAULT_TIMEOUT);
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || applicationId == null) {
            log.error(TAG + " initialized with null-values - will fail - userAdminServiceUri={}, myAppTokenId={}, adminUserTokenId={}, applicationId={}", userAdminServiceUri, myAppTokenId, adminUserTokenId, applicationId);
        }
        this.adminUserTokenId = adminUserTokenId;
        this.applicationId = applicationId;
    }

    public CommandGetApplicationById(URI userAdminServiceUri, String myAppTokenId, String adminUserTokenId, String applicationId, int timeout) {
        super(userAdminServiceUri, "", myAppTokenId, "UASUserAdminGroup", timeout);
        if (userAdminServiceUri == null || !ApplicationTokenID.isValid(myAppTokenId) || !UserTokenId.isValid(adminUserTokenId) || applicationId == null) {
            log.error(TAG + " initialized with null-values - will fail - userAdminServiceUri={}, myAppTokenId={}, adminUserTokenId={}, applicationId={}", userAdminServiceUri, myAppTokenId, adminUserTokenId, applicationId);
        }
        this.adminUserTokenId = adminUserTokenId;
        this.applicationId = applicationId;
    }

    @Override
    protected String dealWithFailedResponse(String responseBody, int statusCode) {
        if (statusCode != HttpURLConnection.HTTP_CONFLICT && retryCnt < 1) {
            retryCnt++;
            return doGetCommand();
        } else {
            return null;
        }
    }

    @Override
    protected String getTargetPath() {
        return myAppTokenId + "/" + adminUserTokenId + "/application/" + applicationId;
    }

    @Override
    protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
        return super.dealWithRequestBeforeSend(request);
    }

}
