package com.signaturit.api.java_sdk;

import okhttp3.*;
import okhttp3.MultipartBody.Builder;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

class RequestHelper {

    /**
     * User agent
     */
    public static final String USER_AGENT = "signaturit-java-sdk 1.2.1";

    /**
     * Request time out
     */
    public static final int TIMEOUT = 120;

    protected static String putGetParamsToUrl(String route, Map<String, Object> parameters) {
        if (parameters != null) {
            StringBuilder routeBuilder = new StringBuilder();

            for (Entry<String, Object> entry : parameters.entrySet()) {
                if (entry.getKey().equals("ids")) {
                    routeBuilder.append("&ids=");

                    String delim = "";

                    for (CharSequence i : (CharSequence[]) entry.getValue()) {
                        routeBuilder.append(delim).append(i);

                        delim = ",";
                    }
                } else {
                    routeBuilder.append(
                            String.format("&%s=%s", entry.getKey(), entry.getValue())
                    );
                }
            }

            route += routeBuilder.toString();
        }

        return route;
    }

    protected static void parseParameters(Builder bodyBuilder, Object recipients, String key) {
        if (recipients instanceof String || recipients instanceof Integer || recipients instanceof Double || recipients instanceof Float) {
            bodyBuilder.addFormDataPart(key, recipients.toString());
        } else if (recipients instanceof int[]) {
            int[] listArray = (int[]) recipients;

            int i = 0;

            for (Object item : listArray) {
                bodyBuilder.addFormDataPart(key + "[" + i + "]", item.toString());
                ++i;
            }
        } else if (recipients instanceof String[]) {
            String[] listArray = (String[]) recipients;

            int i = 0;

            for (Object item : listArray) {
                bodyBuilder.addFormDataPart(key + "[" + i + "]", item.toString());

                ++i;
            }
        } else if (recipients instanceof ArrayList<?>) {
            int i = 0;

            for (Object recipient : (ArrayList<Object>) recipients) {
                parseParameters(bodyBuilder, recipient, key + "[" + i + "]");

                ++i;
            }
        } else if (recipients instanceof HashMap) {
            for (Entry<String, Object> entry : ((Map<String, Object>) recipients).entrySet()) {
                parseParameters(bodyBuilder, entry.getValue(), key + "[" + entry.getKey() + "]");
            }
        }
    }

    protected static void parseParametersPatch(okhttp3.FormBody.Builder requestPostBuilder, Object recipients, String key) {
        if (recipients instanceof String || recipients instanceof Integer) {
            requestPostBuilder.addEncoded(key, recipients.toString());
        } else if (recipients instanceof int[]) {
            int[] listArray = (int[]) recipients;

            int i = 0;

            for (Object item : listArray) {
                requestPostBuilder.addEncoded(key + "[" + i + "]", item.toString());

                ++i;
            }
        } else if (recipients instanceof String[]) {
            String[] listArray = (String[]) recipients;

            int i = 0;

            for (Object item : listArray) {
                requestPostBuilder.addEncoded(key + "[" + i + "]", item.toString());

                ++i;
            }
        } else if (recipients instanceof ArrayList<?>) {
            int i = 0;

            for (HashMap<String, Object> recipient : (ArrayList<HashMap<String, Object>>) recipients) {
                for (Entry<String, Object> entry : recipient.entrySet()) {
                    parseParametersPatch(requestPostBuilder, entry.getValue(), key + "[" + i + "][" + entry.getKey() + "]");
                }

                ++i;
            }
        } else if (recipients instanceof HashMap) {
            for (Entry<String, Object> entry : ((Map<String, Object>) recipients).entrySet()) {
                parseParametersPatch(requestPostBuilder, entry.getValue(), key + "[" + entry.getKey() + "]");
            }
        }
    }

    protected static Response requestPost(String route, String token, Map<String, Object> parameters, ArrayList<File> files) throws IOException {
        OkHttpClient client = RequestHelper.defaultClient();

        Builder requestPostBuilder = new okhttp3.MultipartBody.Builder().setType(okhttp3.MultipartBody.FORM);

        if (parameters != null) {
            for (Entry<String, Object> entry : parameters.entrySet()) {
                parseParameters(requestPostBuilder, entry.getValue(), entry.getKey());
            }
        }

        if (files != null) {
            int i = 0;

            for (File temp : files) {
                requestPostBuilder.addFormDataPart(
                        "files[" + i + "]",
                        temp.getName(),
                        RequestBody.create(MediaType.parse("*/*"), temp)
                );

                ++i;
            }
        }

        RequestBody requestBody;

        if (files == null && parameters == null) {
            requestPostBuilder.addFormDataPart("", "");
        }

        requestBody = requestPostBuilder.build();

        Request request = new Request.Builder()
                .post(requestBody)
                .addHeader("Authorization", token)
                .addHeader("user-agent", RequestHelper.USER_AGENT)
                .url(route)
                .build();

        return client.newCall(request).execute();
    }

    protected static Response requestGet(String route, String token) throws IOException {
        OkHttpClient client = RequestHelper.defaultClient();

        Request request = new Request.Builder()
                .get()
                .addHeader("Authorization", token)
                .addHeader("user-agent", RequestHelper.USER_AGENT)
                .url(route)
                .build();

        return client.newCall(request).execute();
    }

    protected static Response requestDelete(String route, String token) throws IOException {
        OkHttpClient client = RequestHelper.defaultClient();

        Request request = new Request.Builder()
                .delete()
                .addHeader("Authorization", token)
                .addHeader("user-agent", RequestHelper.USER_AGENT)
                .url(route)
                .build();

        return client.newCall(request).execute();
    }

    protected static InputStream requestGetFile(String route, String token) throws IOException {
        OkHttpClient client = RequestHelper.defaultClient();

        Request request = new Request.Builder()
                .get()
                .addHeader("Authorization", token)
                .addHeader("user-agent", RequestHelper.USER_AGENT)
                .url(route)
                .build();

        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();

        assert body != null;

        return body.byteStream();
    }

    protected static Response requestPatch(String route, String token, HashMap<String, Object> parameters) throws IOException {
        OkHttpClient client = RequestHelper.defaultClient();

        okhttp3.FormBody.Builder requestPostBuilder = new okhttp3.FormBody.Builder();

        if (parameters != null) {
            for (Entry<String, Object> entry : parameters.entrySet()) {
                parseParametersPatch(requestPostBuilder, entry.getValue(), entry.getKey());
            }
        }

        RequestBody requestBody;

        if (parameters == null) {
            requestPostBuilder.addEncoded("", "");
        }

        requestBody = requestPostBuilder.build();

        Request request = new Request.Builder()
                .patch(requestBody)
                .addHeader("Authorization", token)
                .addHeader("user-agent", RequestHelper.USER_AGENT)
                .url(route)
                .build();

        return client.newCall(request).execute();
    }

    private static OkHttpClient defaultClient() {
        TLSSocketFactory socketFactory = null;

        try {
            socketFactory = new TLSSocketFactory();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        X509TrustManager trustManager = getTrustManager();

        assert socketFactory != null;
        assert trustManager != null;

        return new OkHttpClient.Builder()
                .connectTimeout(RequestHelper.TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(RequestHelper.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(RequestHelper.TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(socketFactory, trustManager)
                .build();
    }

    private static X509TrustManager getTrustManager() {
        TrustManagerFactory trustManagerFactory;

        try {
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                return null;
            }
            return (X509TrustManager) trustManagers[0];
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            return null;
        } catch (KeyStoreException e) {
            e.printStackTrace();

            return null;
        }
    }
}
