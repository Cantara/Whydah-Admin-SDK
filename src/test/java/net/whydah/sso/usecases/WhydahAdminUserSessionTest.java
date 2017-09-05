package net.whydah.sso.usecases;

import net.whydah.sso.session.WhydahAdminUserSession;
import net.whydah.sso.util.AdminSystemTestBaseConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

public class WhydahAdminUserSessionTest {

    private static final Logger log = getLogger(WhydahAdminUserSessionTest.class);
    static AdminSystemTestBaseConfig config;
    
    @BeforeClass
    public static void setup() throws Exception {
        config = new AdminSystemTestBaseConfig();
    }
    
    @Test
    public void testTimecalculations() throws Exception {
        log.trace("testTimecalculations() - starting test");
        long i = System.currentTimeMillis()+200;
        assertTrue(!WhydahAdminUserSession.expiresBeforeNextSchedule(i));
        i = System.currentTimeMillis() + 10;
        assertTrue(WhydahAdminUserSession.expiresBeforeNextSchedule(i));
        log.trace("testTimecalculations() - done");

    }

   

    
    
}
