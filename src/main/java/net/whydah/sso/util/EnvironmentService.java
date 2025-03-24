package net.whydah.sso.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentService {
    private static final Map<String, String> testEnvironment = new HashMap<>();

    public static void setTestVariables(Map<String, String> variables) {
        testEnvironment.putAll(variables);

        // Try to replace System.getenv with our overridden values
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");

            // First, get the existing environment
            Method getenv = System.class.getMethod("getenv");
            Map<String, String> env = (Map<String, String>) getenv.invoke(null);

            // Since Java 9+, we can't access the underlying fields but we can override specific calls
            testEnvironment.forEach((key, value) -> {
                System.out.println("Setting environment test value: " + key + "=" + value);
            });

            // The environment will still be accessed through System.getenv, but at least we've logged
            // our test values for debugging
        } catch (Exception e) {
            System.err.println("Note: Could not set environment variables directly due to Java security restrictions. " +
                    "Using EnvironmentService fallback instead.");
        }
    }

    public static String getVariable(String key) {
        // In test mode, check our custom map first
        if (testEnvironment.containsKey(key)) {
            return testEnvironment.get(key);
        }

        // Fall back to actual environment variable
        String value = System.getenv(key);
        if (value == null && "IAM_MODE".equals(key)) {
            System.out.println("IAM_MODE not found in environment, using default: TEST");
            return "TEST";
        }
        return value;
    }
}