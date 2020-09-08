package com.signaturit.api.java_sdk;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TLSSocketFactoryTest {

    private static final MockWebServer server = new MockWebServer();
    private static final String hostname = server.getHostName();
    private static final int port = server.getPort();

    private final TLSSocketFactory factory = new TLSSocketFactory();

    public TLSSocketFactoryTest() throws NoSuchAlgorithmException, KeyManagementException {
        // left blank intentionally
    }

    @Test
    public void testGetCipherList() {
        String[] list = factory.getCipherList();

        assertTrue(list.length > 0);
    }

    @Test
    public void testCreateSocket() throws IOException {
        Socket socket = factory.createSocket(hostname, port);

        assertEquals(port, socket.getPort());
    }
}
