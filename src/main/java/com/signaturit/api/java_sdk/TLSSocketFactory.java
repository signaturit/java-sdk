package com.signaturit.api.java_sdk;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TLSSocketFactory extends SSLSocketFactory {

    private final SSLSocketFactory internalSSLSocketFactory;
    private final SSLContext internalContext;

    private final String tlsVersion = "TLSv1.2";

    public TLSSocketFactory() throws KeyManagementException, NoSuchAlgorithmException {
        internalContext = SSLContext.getInstance(tlsVersion);
        internalContext.init(null, null, null);
        internalSSLSocketFactory = internalContext.getSocketFactory();
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return internalSSLSocketFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return internalSSLSocketFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(s, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port, localHost, localPort));
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(address, port, localAddress, localPort));
    }

    private Socket enableTLSOnSocket(Socket socket) {
        String[] cypherList = getCipherList();

        if (socket instanceof SSLSocket) {
            ((SSLSocket) socket).setEnabledProtocols(new String[]{tlsVersion});
            ((SSLSocket) socket).setEnabledCipherSuites(cypherList);
        }
        return socket;
    }

    protected String[] getCipherList() {
        String[] preferredCiphers = {
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
                "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
                "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384",
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
                "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
                "TLS_RSA_WITH_AES_128_CBC_SHA256",
                "TLS_RSA_WITH_AES_128_GCM_SHA256",
                "TLS_RSA_WITH_AES_256_CBC_SHA256",
                "TLS_RSA_WITH_AES_256_GCM_SHA384"
        };

        String[] availableCiphers;

        try {
            SSLSocketFactory factory = internalContext.getSocketFactory();
            availableCiphers = factory.getSupportedCipherSuites();
            Arrays.sort(availableCiphers);
        } catch (Exception e) {
            return new String[]{
                    "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
                    "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
                    "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384",
                    "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
                    "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
                    "TLS_RSA_WITH_AES_128_CBC_SHA256",
                    "TLS_RSA_WITH_AES_128_GCM_SHA256",
                    "TLS_RSA_WITH_AES_256_CBC_SHA256",
                    "TLS_RSA_WITH_AES_256_GCM_SHA384",
                    "TLS_DHE_DSS_WITH_AES_128_CBC_SHA",
                    "TLS_DHE_DSS_WITH_AES_256_CBC_SHA",
                    "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
                    "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
                    "TLS_RSA_WITH_AES_256_CBC_SHA256",
                    "TLS_RSA_WITH_AES_256_CBC_SHA",
                    "TLS_RSA_WITH_AES_128_CBC_SHA256",
                    "TLS_RSA_WITH_AES_128_CBC_SHA",
                    "TLS_EMPTY_RENEGOTIATION_INFO_SCSV"
            };
        }

        List<String> aa = new ArrayList<String>();

        for (String preferredCipher : preferredCiphers) {
            int idx = Arrays.binarySearch(availableCiphers, preferredCipher);

            if (idx >= 0) {
                aa.add(preferredCipher);
            }
        }

        aa.add("TLS_EMPTY_RENEGOTIATION_INFO_SCSV");

        return aa.toArray(new String[0]);
    }
}
