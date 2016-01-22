package com.signaturit.api.java_sdk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Client {
	
	/**
	 * Signaturit's production API URL
	 */
	public static final String PROD_BASE_URL = "https://api.signaturit.com";
	
	/**
	 * Signaturit's sandbox API URL
	 */
	public static final String SANDBOX_BASE_URL = "https://api.sandbox.signaturit.com";
	
	/**
	 * API version
	 */
	public static final String API_VERSION = "/v3/";
	
	/**
	 * access token that provides access to the API methods
	 */
	private String accessToken;
	
	/**
	 * URL
	 */
	private String url; 

	/**
	 * @param accesToken the token that grant access and identify the user
	 * @param production define if use production or sandbox end-point.
	 */
	public Client(String accesToken, boolean production) 
	{
		this.accessToken = "Bearer "+ accesToken;
		this.url = production ? Client.PROD_BASE_URL : Client.SANDBOX_BASE_URL;
		this.url += Client.API_VERSION;

		Unirest.setDefaultHeader("Authorization", this.accessToken);
		Unirest.setDefaultHeader("user-agent", "signaturit-java-sdk 1.0.0");
	}
	
	/**
	 * @param accesToken the token that grant access and identify the user
	 */
	public Client(String accesToken) 
	{
		this.accessToken = "Bearer "+ accesToken;
		this.url = Client.SANDBOX_BASE_URL;
		this.url += Client.API_VERSION;

		Unirest.setDefaultHeader("Authorization", this.accessToken);
		Unirest.setDefaultHeader("user-agent", "signaturit-java-sdk 1.0.0");
	}

	/**
	 * 
	 * @throws IOException
	 */
	protected void shutDown() throws IOException 
	{
		Unirest.shutdown();
	}

	/**
	 * 
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> countSignatures() throws UnirestException 
	{
		String route = "signatures/count.json";
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param parameters
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> countSignatures(Map<String, Object> parameters) throws UnirestException 
	{
		String route = RequestHelper.putGetParamsToUrl("signatures/count.json?", parameters);
		
		return RequestHelper.requestGet(this.url + route);
	}

	/**
	 * 
	 * @param signatureId 
	 * @return 
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getSignature(String signatureId) throws UnirestException 
	{
		String route = (
			String.format("signatures/%s.json", signatureId)
		);
		
		return RequestHelper.requestGet(this.url + route);
	}

	/**
	 * 
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getSignatures() throws UnirestException 
	{
		String route = "signatures.json";
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param limit
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getSignatures(int limit) throws UnirestException 
	{
		String route = String.format("signatures.json?limit=%d", limit);
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getSignatures(int limit, int offset) throws UnirestException 
	{
		String route = String.format("signatures.json?limit=%doffset=%d", limit, offset);
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param parameters
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getSignatures(Map<String, Object> parameters) throws UnirestException 
	{
		String route = "signatures.json?";
		
		route = RequestHelper.putGetParamsToUrl(route, parameters);
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @param parameters
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getSignatures(int limit, int offset, Map<String, Object> parameters) throws UnirestException 
	{
		String route = String.format("signatures.json?limit=%doffset=%d", limit, offset);
		
		route = RequestHelper.putGetParamsToUrl(route, parameters);
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param signatureId
	 * @param documentId
	 * @return 
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> downloadAuditTrail(String signatureId, String documentId) 
			throws UnirestException
	{
		String route = String.format(
			"signatures/%s/documents/%s/download/audit_trail", signatureId, documentId
		);
		
		return RequestHelper.requestGet(this.url + route);
		//InputStream downloadedFile = RequestHelper.requestGetFile(this.url + route);
	}
	
	/**
	 * 
	 * @param signatureId
	 * @param documentId
	 * @return 
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> downloadSignedDocument(String signatureId, String documentId) 
			throws UnirestException
	{
		String route = String.format(
			"signatures/%s/documents/%s/download/signed", signatureId, documentId
		);
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param files
	 * @param recipients
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> createSignature(
		ArrayList<String> files,
		ArrayList<HashMap<String, Object>> recipients
	) throws UnirestException
	{
		HashMap<String, Object> parameters= new HashMap<String, Object>();
		
		return this.createSignature(files, recipients, parameters);
	}
	
	/**
	 * 
	 * @param files
	 * @param recipients
	 * @param parameters
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> createSignature(
		ArrayList<String> files,
		ArrayList<HashMap<String, Object>> recipients,
		Map<String, Object> parameters
	) throws UnirestException
	{
		String route = "signatures.json";
		
		parameters.put("files", files);
		
		RequestHelper.parseParameters(parameters, recipients, "recipients");
		
		return RequestHelper.requestPost(this.url + route, parameters, files);
	}
	
	/**
	 * 
	 * @param signatureId
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> cancelSignature(String signatureId) throws UnirestException
	{
		String route = String.format(
			"signatures/%s/cancel.json", signatureId
		);
		
		return RequestHelper.requestPatch(this.url + route, null);
	}
	
	/**
	 * 
	 * @param signatureId
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> sendSignatureReminder(String signatureId) throws UnirestException
	{
		String route = String.format(
			"signatures/%s/reminder.json", signatureId
		);
		return RequestHelper.requestPost(this.url + route, null, null);
	}
	
	/**
	 * 
	 * @param brandingId
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getBranding(String brandingId) throws UnirestException
	{
		String route = String.format(
			"brandings/%s.json", brandingId
		);
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getBrandings() throws UnirestException
	{
		String route = "brandings.json";
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param parameters
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> createBranding(HashMap<String, Object> parameters) throws UnirestException
	{
		String route = "brandings.json";
		return RequestHelper.requestPost(this.url + route, parameters, null);
	}
	
	/**
	 * 
	 * @param brandingId
	 * @param parameters
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> updateBranding(String brandingId, HashMap<String, Object> parameters) 
		throws UnirestException
	{
		String route = String.format(
			"brandings/%s.json", brandingId
		);
		return RequestHelper.requestPatch(this.url + route, parameters);
	}
	
	/**
	 * 
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getTemplates() throws UnirestException
	{
		String route = String.format(
			"templates.json"
		);
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 *
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getEmails() 
		throws UnirestException 
	{
		String route = "emails.json";
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param limit
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getEmails(int limit) 
		throws UnirestException 
	{
		String route = String.format("emails.json?limit=%d", limit);
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getEmails(int limit, int offset) 
		throws UnirestException 
	{
		String route = String.format("emails.json?limit=%doffset=%d", limit, offset);
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param parameters
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getEmails(Map<String, Object> parameters) 
		throws UnirestException 
	{
		String route = "emails.json?";
		
		route = RequestHelper.putGetParamsToUrl(route, parameters);
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @param parameters
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getEmails(int limit, int offset, Map<String, Object> parameters) 
		throws UnirestException 
	{
		String route = String.format("emails.json?limit=%doffset=%d", limit, offset);
		
		route = RequestHelper.putGetParamsToUrl(route, parameters);
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> countEmails() 
		throws UnirestException
	{
		String route = "emails/count.json";
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param parameters
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> countEmails(Map<String, Object> parameters) 
		throws UnirestException
	{
		String route = "emails/count.json?";
		route = RequestHelper.putGetParamsToUrl(route, parameters);
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param emailId
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> getEmail(String emailId) throws UnirestException
	{
		String route = String.format(
			"emails/%s.json", emailId
		);
		
		return RequestHelper.requestGet(this.url + route);
	}
	
	/**
	 * 
	 * @param files
	 * @param recipients
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> createEmail(
		ArrayList<String> files, 
		ArrayList<HashMap<String, Object>> recipients
	) throws UnirestException
	{
		String route = "emails.json";
		
		HashMap<String, Object>	parameters = new HashMap<String, Object>();
		
		parameters.put("files", files);
		
		RequestHelper.parseParameters(parameters, recipients, "recipients");
		
		return RequestHelper.requestPost(this.url + route, parameters, files);
	}
	
	/**
	 * 
	 * @param files
	 * @param recipients
	 * @param subject
	 * @param body
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> createEmail(
		ArrayList<String> files, 
		ArrayList<HashMap<String, Object>> recipients, 
		String subject, String body
	) throws UnirestException
	{	
		HashMap<String, Object>	parameters = new HashMap<String, Object>();
		
		return this.createEmail(files, recipients, subject, body, parameters);
	}
	
	/**
	 * 
	 * @param files
	 * @param recipients
	 * @param parameters
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> createEmail(
		ArrayList<String> files, 
		ArrayList<HashMap<String, Object>> recipients, 
		HashMap<String, Object> parameters
	) throws UnirestException
	{
		String route = "emails.json";
		
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}
		
		parameters.put("files", files);
		
		RequestHelper.parseParameters(parameters, recipients, "recipients");
		
		return RequestHelper.requestPost(this.url + route, parameters, files);
	}
	
	/**
	 * 
	 * @param files
	 * @param recipients
	 * @param subject
	 * @param body
	 * @param parameters
	 * @return
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> createEmail(
		ArrayList<String> files, 
		ArrayList<HashMap<String, Object>> recipients, 
		String subject, 
		String body, 
		HashMap<String, Object> parameters
	) throws UnirestException
	{
		String route = "emails.json";
		
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}
		
		parameters.put("files", files);
		parameters.put("body", body);
		parameters.put("subject", subject);
		
		RequestHelper.parseParameters(parameters, recipients, "recipients");
		
		return RequestHelper.requestPost(this.url + route, parameters, files);
	}
	
	/**
	 * 
	 * @param emailId
	 * @param certificateId
	 * @return 
	 * @throws UnirestException
	 */
	public HttpResponse<JsonNode> downloadEmailAuditTrail(String emailId, String certificateId) 
		throws UnirestException 
	{
		String route = String.format(
			"emails/%s/certificates/%s/download/audit_trail", emailId, certificateId
		);
		return RequestHelper.requestGet(this.url + route);
	}
}
