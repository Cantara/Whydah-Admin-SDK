package net.whydah.sso.session;

import net.whydah.sso.commands.userauth.CommandValidateUserTokenId;
import net.whydah.sso.user.helpers.UserTokenXpathHelper;
import net.whydah.sso.user.helpers.UserXpathHelper;
import net.whydah.sso.user.types.UserCredential;
import net.whydah.sso.util.WhydahAdminUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WhydahAdminUserSession {


    private static final Logger log = LoggerFactory.getLogger(WhydahAdminUserSession.class);
    private static final int SESSION_CHECK_INTERVAL = 60;
    private WhydahApplicationSession was;
    private UserCredential userCredential;
    private String userTokenId;
    private String userTokenXML;

    public WhydahAdminUserSession(WhydahApplicationSession was, UserCredential userCredential) {
        if (was == null || was.getActiveApplicationTokenId() == null || was.getActiveApplicationTokenId().length() < 4) {
            log.error("Error, unable to initialize new user session, application session invalid:" + was.getActiveApplicationTokenId());

        }

        this.was = was;
        this.userCredential = userCredential;
        initializeUserSession();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> sf = scheduler.scheduleAtFixedRate(
                new Runnable() {
                    public void run() {
                        renewWhydahUserSession();
                    }
                },
                1, SESSION_CHECK_INTERVAL, TimeUnit.SECONDS);
    }

    public static Integer calculateTokenRemainingLifetimeInSeconds(String userTokenXml) {
        Long tokenLifespanMs = UserTokenXpathHelper.getLifespan(userTokenXml);
        Long tokenTimestampMsSinceEpoch = UserTokenXpathHelper.getTimestamp(userTokenXml);

        if (tokenLifespanMs == null || tokenTimestampMsSinceEpoch == null) {
            return null;
        }

        long endOfTokenLifeMs = tokenTimestampMsSinceEpoch + tokenLifespanMs;
        long remainingLifeMs = endOfTokenLifeMs - System.currentTimeMillis();
        return (int) (remainingLifeMs / 1000);
    }

    public static boolean expiresBeforeNextSchedule(Long timestamp) {

        long i = System.currentTimeMillis();
        long j = timestamp;
        long diffSeconds = j - i;
        if (diffSeconds < SESSION_CHECK_INTERVAL) {
            return true;
        }
        return false;
    }

    public String getActiveUserTokenId() {
        return userTokenId;
    }

    public String getActiveUserToken() {
        return userTokenXML;
    }

    /*
    * @return true is session is active and working
     */
    public boolean hasActiveSession() {
        if (userTokenXML == null || userTokenXML.length() < 4) {
            return false;
        }
        try {
            URI stsURI = new URI(was.getSTS());
            return new CommandValidateUserTokenId(stsURI, was.getActiveApplicationTokenId(), getActiveUserTokenId()).execute();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasRole(String roleName) {
        return UserXpathHelper.hasRoleFromUserToken(userTokenXML, was.getActiveApplicationTokenId(), roleName);
    }

    private void renewWhydahUserSession() {
        log.info("Renew user session");
        userTokenXML = WhydahAdminUtil.logOnUser(was, userCredential);
        if (!hasActiveSession()) {
            log.error("Error, unable to initialize new user session, userTokenXML:" + userTokenXML);
            for (int n = 0; n < 7 || hasActiveSession(); n++) {
                userTokenXML = WhydahAdminUtil.logOnUser(was, userCredential);
                log.warn("Retrying renewing user session");
                try {
                    Thread.sleep(1000 * n);
                } catch (InterruptedException ie) {
                }
                // If we keep failing, let us force renew of application session too
                if (n > 3) {
                    was.resetApplicationSession();
                    n = 0;
                }
            }

        } else {
            log.info("Renew user session successfull.  userTokenXml:" + userTokenXML);
            Long expires = UserXpathHelper.getTimestampFromUserTokenXml(userTokenXML) + UserXpathHelper.getLifespanFromUserTokenXml(userTokenXML);
            if (expiresBeforeNextSchedule(expires)) {
                this.userTokenXML = WhydahAdminUtil.extendUserSession(was, userCredential);
                userTokenId = UserXpathHelper.getUserTokenId(this.userTokenXML);
            }
        }

    }

    private void initializeUserSession() {
        log.info("Initializing new user session");
        userTokenXML = WhydahAdminUtil.logOnUser(was, userCredential);
        if (userTokenXML == null || userTokenXML.length() < 4) {
            log.error("Error, unable to initialize new user session, userTokenXML:" + userTokenXML);
        } else {
            log.info("Initializing user session successfull.  userTokenXml:" + userTokenXML);
            userTokenId = UserXpathHelper.getUserTokenId(this.userTokenXML);
        }
    }
}


