package com.signaturit.api.java_sdk;

import okhttp3.Response;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ClientTest {

    private static final String token = "token";
    private static final String recipientEmail = "foo";
    private static final String recipientPhone = "34555555555";

    private static final MockWebServer server = new MockWebServer();
    private static final Client client = new Client(token);

    @BeforeClass
    public static void executedBeforeAll() throws IOException, IllegalAccessException, NoSuchFieldException {
        server.start();

        server.setDispatcher(
                new Dispatcher() {
                    @Override
                    public MockResponse dispatch(RecordedRequest request) {
                        return new MockResponse().setResponseCode(200);
                    }
                }
        );

        String url = server.url(Client.API_VERSION).toString();

        Field field = client.getClass().getDeclaredField("url");
        field.setAccessible(true);
        field.set(client, url);
    }

    @AfterClass
    public static void executedAfterAll() throws IOException {
        server.shutdown();
    }

    @Test
    public void testCountSignatures() throws IOException, InterruptedException {
        Response response = client.countSignatures();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/signatures/count.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCountSignaturesWithParameters() throws IOException, InterruptedException {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("foo", "bar");

        Response response = client.countSignatures(parameters);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/signatures/count.json?&foo=bar", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetSignature() throws IOException, InterruptedException {
        Response response = client.getSignature("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/signatures/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetSignatures() throws IOException, InterruptedException {
        Response response = client.getSignatures();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/signatures.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetSignaturesWithLimit() throws IOException, InterruptedException {
        Response response = client.getSignatures(5);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/signatures.json?limit=5", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetSignaturesWithLimitAndOffset() throws IOException, InterruptedException {
        Response response = client.getSignatures(5, 2);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/signatures.json?limit=5&offset=2", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetSignaturesWithParameters() throws IOException, InterruptedException {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("foo", "bar");

        Response response = client.getSignatures(parameters);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/signatures.json?&foo=bar", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetSignaturesWithLimitAndOffsetAndParameters() throws IOException, InterruptedException {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("foo", "bar");

        Response response = client.getSignatures(5, 2, parameters);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/signatures.json?limit=5&offset=2&foo=bar", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testDownloadAuditTrail() throws IOException, InterruptedException {
        Response response = client.downloadAuditTrail("foo", "bar");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/signatures/foo/documents/bar/download/audit_trail", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testDownloadSignedDocument() throws IOException, InterruptedException {
        Response response = client.downloadSignedDocument("foo", "bar");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/signatures/foo/documents/bar/download/signed", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCreateSignature() throws IOException, InterruptedException {
        ArrayList<File> files = getFiles();
        ArrayList<HashMap<String, Object>> recipients = getRecipients();

        Response response = client.createSignature(files, recipients);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/signatures.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCreateSignatureWithParameters() throws IOException, InterruptedException {
        ArrayList<File> files = getFiles();
        ArrayList<HashMap<String, Object>> recipients = getRecipients();
        HashMap<String, Object> parameters = new HashMap<String, Object>();

        Response response = client.createSignature(files, recipients, parameters);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/signatures.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCancelSignature() throws IOException, InterruptedException {
        Response response = client.cancelSignature("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("PATCH", recordedRequest.getMethod());
        assertEquals("/v3/signatures/foo/cancel.json", recordedRequest.getPath());
        assertEquals("=", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testSendSignatureReminder() throws IOException, InterruptedException {
        Response response = client.sendSignatureReminder("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/signatures/foo/reminder.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetBranding() throws IOException, InterruptedException {
        Response response = client.getBranding("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/brandings/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetBrandings() throws IOException, InterruptedException {
        Response response = client.getBrandings();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/brandings.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCreateBranding() throws IOException, InterruptedException {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("foo", "bar");

        Response response = client.createBranding(parameters);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/brandings.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testUpdateBranding() throws IOException, InterruptedException {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("foo", "bar");

        Response response = client.updateBranding("foo", parameters);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("PATCH", recordedRequest.getMethod());
        assertEquals("/v3/brandings/foo.json", recordedRequest.getPath());
        assertEquals("foo=bar", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetTemplates() throws IOException, InterruptedException {
        Response response = client.getTemplates();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/templates.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetEmails() throws IOException, InterruptedException {
        Response response = client.getEmails();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/emails.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetEmailsWithLimit() throws IOException, InterruptedException {
        Response response = client.getEmails(5);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/emails.json?limit=5", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetEmailsWithLimitAndOffset() throws IOException, InterruptedException {
        Response response = client.getEmails(5, 3);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/emails.json?limit=5&offset=3", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetEmailsWithParameters() throws IOException, InterruptedException {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("foo", "bar");

        Response response = client.getEmails(parameters);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/emails.json?&foo=bar", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetEmailsWithLimitAndOffsetAndParameters() throws IOException, InterruptedException {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("foo", "bar");

        Response response = client.getEmails(5, 3, parameters);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/emails.json?limit=5&offset=3&foo=bar", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCountEmails() throws IOException, InterruptedException {
        Response response = client.countEmails();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/emails/count.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCountEmailsWithParameters() throws IOException, InterruptedException {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("foo", "bar");

        Response response = client.countEmails(parameters);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/emails/count.json?&foo=bar", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetEmail() throws IOException, InterruptedException {
        Response response = client.getEmail("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/emails/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCreateEmail() throws IOException, InterruptedException {
        ArrayList<File> files = getFiles();
        ArrayList<HashMap<String, Object>> recipients = getRecipients();

        Response response = client.createEmail(files, recipients);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/emails.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCreateEmailWithSubjectAndBody() throws IOException, InterruptedException {
        ArrayList<File> files = getFiles();
        ArrayList<HashMap<String, Object>> recipients = getRecipients();

        Response response = client.createEmail(files, recipients, "foo", "bar");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/emails.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCreateEmailWithParameters() throws IOException, InterruptedException {
        ArrayList<File> files = getFiles();
        ArrayList<HashMap<String, Object>> recipients = getRecipients();
        HashMap<String, Object> parameters = new HashMap<String, Object>();

        Response response = client.createEmail(files, recipients, parameters);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/emails.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCreateEmailWithSubjectAndBodyAndParameters() throws IOException, InterruptedException {
        ArrayList<File> files = getFiles();
        ArrayList<HashMap<String, Object>> recipients = getRecipients();
        HashMap<String, Object> parameters = new HashMap<String, Object>();

        Response response = client.createEmail(files, recipients, "foo", "bar", parameters);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/emails.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testDownloadEmailAuditTrail() throws IOException, InterruptedException {
        Response response = client.downloadEmailAuditTrail("foo", "bar");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/emails/foo/certificates/bar/download/audit_trail", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetContacts() throws IOException, InterruptedException {
        Response response = client.getContacts();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/contacts.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetContact() throws IOException, InterruptedException {
        Response response = client.getContact("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/contacts/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testUpdateContact() throws IOException, InterruptedException {
        Response response = client.updateContact("foo", "bar", "baz");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("PATCH", recordedRequest.getMethod());
        assertEquals("/v3/contacts/foo.json", recordedRequest.getPath());
        assertEquals("name=baz&email=bar", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testDeleteContact() throws IOException, InterruptedException {
        Response response = client.deleteContact("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("DELETE", recordedRequest.getMethod());
        assertEquals("/v3/contacts/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCreateContact() throws IOException, InterruptedException {
        Response response = client.createContact("email@domain.com", "foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/contacts.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetPackages() throws IOException, InterruptedException {
        Response response = client.getPackages();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/packages.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetPackage() throws IOException, InterruptedException {
        Response response = client.getPackage("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/packages/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testDownloadPackageAuditTrail() throws IOException, InterruptedException {
        InputStream response = client.downloadPackageAuditTrail("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/packages/foo/download/audit_trail", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCreateSignaturePackage() {
        Response response = client.createSignaturePackage();

        assertNull(response);
    }

    @Test
    public void testGetTeam() throws IOException, InterruptedException {
        Response response = client.getTeam();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/team.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetUsers() throws IOException, InterruptedException {
        Response response = client.getUsers();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/team/users.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetUser() throws IOException, InterruptedException {
        Response response = client.getUser("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/team/users/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testRemoveUser() throws IOException, InterruptedException {
        Response response = client.removeUser("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("DELETE", recordedRequest.getMethod());
        assertEquals("/v3/team/users/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetSeats() throws IOException, InterruptedException {
        Response response = client.getSeats();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/team/seats.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testRemoveSeat() throws IOException, InterruptedException {
        Response response = client.removeSeat("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("DELETE", recordedRequest.getMethod());
        assertEquals("/v3/team/seats/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testInviteUser() throws IOException, InterruptedException {
        Response response = client.inviteUser("foo", "bar");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/team/users.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testChangeUseRole() throws IOException, InterruptedException {
        Response response = client.changeUseRole("foo", "bar");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("PATCH", recordedRequest.getMethod());
        assertEquals("/v3/team/users/foo.json", recordedRequest.getPath());
        assertEquals("role=bar", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetGroups() throws IOException, InterruptedException {
        Response response = client.getGroups();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/team/groups.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetGroup() throws IOException, InterruptedException {
        Response response = client.getGroup("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/team/groups/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testDeleteGroup() throws IOException, InterruptedException {
        Response response = client.deleteGroup("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("DELETE", recordedRequest.getMethod());
        assertEquals("/v3/team/groups/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCreateGroup() throws IOException, InterruptedException {
        Response response = client.createGroup("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/team/groups.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testUpdateGroup() throws IOException, InterruptedException {
        Response response = client.updateGroup("foo", "bar");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("PATCH", recordedRequest.getMethod());
        assertEquals("/v3/team/groups/foo.json", recordedRequest.getPath());
        assertEquals("name=bar", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testAddMemberToGroup() throws IOException, InterruptedException {
        Response response = client.addMemberToGroup("foo", "bar");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/team/groups/foo/members/bar.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testRemoveMemberFromGroup() throws IOException, InterruptedException {
        Response response = client.removeMemberFromGroup("foo", "bar");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("DELETE", recordedRequest.getMethod());
        assertEquals("/v3/team/groups/foo/members/bar.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testAddManagerToGroup() throws IOException, InterruptedException {
        Response response = client.addManagerToGroup("foo", "bar");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/team/groups/foo/managers/bar.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testRemoveManagerFromGroup() throws IOException, InterruptedException {
        Response response = client.removeManagerFromGroup("foo", "bar");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("DELETE", recordedRequest.getMethod());
        assertEquals("/v3/team/groups/foo/managers/bar.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetSubscriptions() throws IOException, InterruptedException {
        Response response = client.getSubscriptions();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/subscriptions.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetSubscription() throws IOException, InterruptedException {
        Response response = client.getSubscription("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/subscriptions/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCreateSubscription() throws IOException, InterruptedException {
        String[] events = new String[0];

        Response response = client.createSubscription("foo", events);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/subscriptions.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testUpdateSubscription() throws IOException, InterruptedException {
        String[] events = new String[0];

        Response response = client.updateSubscription("foo", "bar", events);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("PATCH", recordedRequest.getMethod());
        assertEquals("/v3/subscriptions/foo.json", recordedRequest.getPath());
        assertEquals("url=bar", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testDeleteSubscription() throws IOException, InterruptedException {
        Response response = client.deleteSubscription("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("DELETE", recordedRequest.getMethod());
        assertEquals("/v3/subscriptions/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetSMS() throws IOException, InterruptedException {
        Response response = client.getSMS();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/sms.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testGetSingleSMS() throws IOException, InterruptedException {
        Response response = client.getSingleSMS("foo");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/sms/foo.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCountSMS() throws IOException, InterruptedException {
        Response response = client.countSMS();

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/sms/count.json", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCountSMSWithParameters() throws IOException, InterruptedException {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("foo", "bar");

        Response response = client.countSMS(parameters);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/sms/count.json?&foo=bar", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testCreateSMS() throws IOException, InterruptedException {
        ArrayList<File> filePath = getFiles();
        ArrayList<HashMap<String, Object>> recipients = getRecipients();
        HashMap<String, Object> parameters = new HashMap<String, Object>();

        Response response = client.createSMS(filePath, recipients, "foo", parameters);

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/v3/sms.json", recordedRequest.getPath());
        // assertEquals("", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testDownloadSmsAuditTrail() throws IOException, InterruptedException {
        InputStream response = client.downloadSmsAuditTrail("foo", "bar");

        RecordedRequest recordedRequest = server.takeRequest();

        assertNotNull(response);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/v3/sms/foo/certificates/bar/download/audit_trail", recordedRequest.getPath());
        assertEquals("", recordedRequest.getBody().readUtf8());
    }

    private ArrayList<HashMap<String, Object>> getRecipients() {
        HashMap<String, Object> recipient = new HashMap<String, Object>();
        recipient.put("email", recipientEmail);
        recipient.put("phone", recipientPhone);

        ArrayList<HashMap<String, Object>> recipients = new ArrayList<HashMap<String, Object>>();
        recipients.add(recipient);

        return recipients;
    }

    private ArrayList<File> getFiles() {
        File file = new File("document.pdf");

        ArrayList<File> filePath = new ArrayList<File>();
        filePath.add(file);

        return filePath;
    }
}
