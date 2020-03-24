package com.signaturit.api.java_sdk;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ClientTest extends TestCase {
    public ClientTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(ClientTest.class);
    }

    public void testClient() {
        Client client = new Client("a_token");

        assertTrue(client instanceof Client);
    }
}
