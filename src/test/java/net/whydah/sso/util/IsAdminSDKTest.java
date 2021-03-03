package net.whydah.sso.util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class IsAdminSDKTest {

    @Test
    public void testIsAdminSDKUtil() {
        assertTrue(WhydahUtil.isAdminSdk());
    }

    @Test
    public void testIsAdminSDKUtil2() {
        assertTrue(WhydahUtil2.isAdminSdk());
    }

}
