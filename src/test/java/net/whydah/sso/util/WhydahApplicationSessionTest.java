package net.whydah.sso.util;

import net.whydah.sso.session.WhydahApplicationSession;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

public class WhydahApplicationSessionTest {

    private static final Logger log = getLogger(WhydahApplicationSessionTest.class);
    static SystemTestBaseConfig config;
    
    @BeforeClass
    public static void setup() throws Exception {
        config = new SystemTestBaseConfig();
    }
    
    @Test
    public void testTimecalculations() throws Exception {
        log.trace("testTimecalculations() - starting test");
        long i = System.currentTimeMillis()+200;
        assertTrue(!WhydahApplicationSession.expiresBeforeNextSchedule(i));
        i = System.currentTimeMillis() + 10;
        assertTrue(WhydahApplicationSession.expiresBeforeNextSchedule(i));
        log.trace("testTimecalculations() - done");

    }

   

    
    
}
