package net.whydah.sso.internal.commands.uib.userauth;

import net.whydah.sso.commands.baseclasses.BaseHttpPostHystrixCommand;
import net.whydah.sso.ddd.model.application.ApplicationTokenID;
import net.whydah.sso.ddd.model.user.UID;

import java.net.URI;

public class CommandResetUserPasswordUAS extends BaseHttpPostHystrixCommand<String> {
    private String uid;

    public CommandResetUserPasswordUAS(String uibUri, String applicationtokenId, String uid) {
        super(URI.create(uibUri), "", applicationtokenId, "SSOAUserAuthGroup");
        this.uid = uid;
        if (uibUri == null || !ApplicationTokenID.isValid(applicationtokenId) || !UID.isValid(uid)) {
            this.log.error("{} initialized with null-values - will fail", CommandResetUserPasswordUAS.class.getSimpleName());
        }

    }

    protected String getTargetPath() {
        return this.myAppTokenId + "/user/" + this.uid + "/reset_password";
    }
}
