package com.signaturit.api.java_sdk;

import okhttp3.MultipartBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class RequestHelperTest {

    private static final MockWebServer server = new MockWebServer();
    private static final String url = server.url("").toString();

    @BeforeClass
    public static void executedBeforeAll() {
        server.setDispatcher(
                new Dispatcher() {
                    @Override
                    public MockResponse dispatch(RecordedRequest request) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(
                                        request.getMethod()
                                );
                    }
                }
        );
    }

    @AfterClass
    public static void executedAfterAll() throws IOException {
        server.shutdown();
    }

    @Test
    public void testPutGetParamsToUrl() {
        CharSequence[] ids = new CharSequence[]{"one", "two"};

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ids", ids);
        parameters.put("foo", "bar");

        String route = RequestHelper.putGetParamsToUrl("/", parameters);

        assertEquals("/&foo=bar&ids=one,two", route);
    }

    @Test
    public void testParseParameters() {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        HashMap<String, Object> recipient = new HashMap<String, Object>();
        recipient.put("email", "email@domain.com");

        ArrayList<HashMap<String, Object>> recipients = new ArrayList<HashMap<String, Object>>();
        recipients.add(recipient);

        RequestHelper.parseParameters(builder, recipients, "");

        assertEquals(1, builder.build().size());
    }

    @Test
    public void testParseParametersPatch() {
        okhttp3.FormBody.Builder builder = new okhttp3.FormBody.Builder();

        HashMap<String, Object> recipient = new HashMap<String, Object>();
        recipient.put("email", "email@domain.com");

        ArrayList<HashMap<String, Object>> recipients = new ArrayList<HashMap<String, Object>>();
        recipients.add(recipient);

        RequestHelper.parseParametersPatch(builder, recipients, "");

        assertEquals("email@domain.com", builder.build().value(0));
    }

    @Test
    public void testRequestPost() throws IOException {
        Response response = RequestHelper.requestPost(url, "foo", null, null);
        ResponseBody body = response.body();

        assert body != null;

        assertEquals("POST", body.string());
    }

    @Test
    public void testRequestGet() throws IOException {
        Response response = RequestHelper.requestGet(url, "foo");

        ResponseBody body = response.body();

        assert body != null;

        assertEquals("GET", body.string());
    }

    @Test
    public void testRequestDelete() throws IOException {
        Response response = RequestHelper.requestDelete(url, "foo");

        ResponseBody body = response.body();

        assert body != null;

        assertEquals("DELETE", body.string());
    }

    @Test
    public void testRequestGetFile() throws IOException {
        InputStream response = RequestHelper.requestGetFile(url, "foo");

        Scanner scanner = new Scanner(response);
        StringBuilder buffer = new StringBuilder();

        while (scanner.hasNext()) {
            buffer.append(
                    scanner.nextLine()
            );
        }

        assertEquals("GET", buffer.toString());
    }

    @Test
    public void testRequestPatch() throws IOException {
        Response response = RequestHelper.requestPatch(url, "foo", null);

        ResponseBody body = response.body();

        assert body != null;

        assertEquals("PATCH", body.string());
    }
}
