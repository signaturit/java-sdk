package com.signaturit.api.java_sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;

public class RequestHelper {

	/**
	 * 
	 * @param route
	 * @param parameters
	 * @return
	 */
	protected static String putGetParamsToUrl(String route, Map<String, Object> parameters)
	{
		if (parameters != null) {
			for (Entry<String, Object>  entry : parameters.entrySet()) {
				StringBuilder routeBuilder = new StringBuilder();
			    if (entry.getKey().equals("ids")) {
			    	
			    	String delim="";
			    	for (CharSequence i : (CharSequence[]) entry.getValue()) {
			    		routeBuilder.append(delim).append(i);
			    		delim = ",";
			    	}
			    }
			    
			    route += String.format(
			    	"&%s=%s", entry.getKey(), routeBuilder.toString()
			    );
			}
		}
		return route;
	}
	
	/**
	 * 
	 * @param parameters
	 * @param recipients
	 * @param key
	 */
	protected static void parseParameters(Map<String, Object> parameters, Object recipients, String key) {
		
		if (!(recipients instanceof ArrayList || recipients instanceof HashMap)) {
			parameters.put(key, recipients);
		} else if (recipients instanceof ArrayList<?>) {
			int i = 0;
			for ( HashMap<String, Object> recipient: (ArrayList<HashMap<String, Object>>) recipients ) {
				for (Entry<String, Object>  entry : recipient.entrySet()) {
					
					if (entry.getValue() instanceof ArrayList<?> || entry.getValue() instanceof HashMap) {
						parseParameters(parameters, entry.getValue(), key+"["+i+"]["+entry.getKey()+"]");
					} else if (entry.getValue() instanceof HashMap) {
						parseParameters(parameters, entry.getValue(), key+"["+i+"]["+entry.getKey()+"]");
						parameters.put(key+"["+i+"]["+entry.getKey()+"]", entry.getValue());
					} else {
						parseParameters(parameters, entry.getValue(), key+"["+entry.getKey()+"]");
					}
				}
				++i;
			}
		} else if (recipients instanceof HashMap) {
			for (Entry<String, Object> entry: ((Map<String, Object>) recipients).entrySet()) {
				if (entry.getValue() instanceof ArrayList<?> || entry.getValue() instanceof HashMap) {
					parseParameters(parameters, entry.getValue(), key+"["+entry.getKey()+"]");
				} else if (entry.getValue() instanceof HashMap) {
					parseParameters(parameters, entry.getValue(), key+"["+entry.getKey()+"]");
				} else {
					parseParameters(parameters, entry.getValue(), key+"["+entry.getKey()+"]");
				}
			}
		}
	}
	
	/**
	 * 
	 * @param route
	 * @param parameters
	 * @param files
	 * @return
	 * @throws UnirestException exception from unirest lib
	 */
	protected static HttpResponse<JsonNode> requestPost(String route, Map<String, Object> parameters, ArrayList<String> files) throws UnirestException 
	{
		MultipartBody request = Unirest.post(route)
				.fields(parameters);
			
		if (files != null) {
			for (String temp : files) {
				request.field("files", new File(temp));
			}
		}
		
		return request.asJson();
	}
	
	/**
	 * 
	 * @param route
	 * @param parameters
	 * @param file
	 * @return
	 * @throws UnirestException exception from unirest lib
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	protected static HttpResponse<JsonNode> requestPut(String route, Map<String, Object> parameters, String file) throws UnirestException, URISyntaxException, IOException 
	{
		
		final InputStream stream = new FileInputStream(file);
		final byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
		stream.close();
		final HttpResponse<JsonNode> jsonResponse = Unirest.put(route)
			.body(bytes)
			.asJson();
		
		return jsonResponse;
	}
	
	/**
	 * 
	 * @param route
	 * @return
	 * @throws UnirestException exception from unirest lib
	 */
	protected static HttpResponse<JsonNode> requestGet(String route) throws UnirestException 
	{
		HttpResponse<JsonNode> jsonResponse = Unirest.get(route)
				.asJson();
		
		return jsonResponse;
	}
	
	/**
	 * 
	 * @param route
	 * @return
	 * @throws UnirestException exception from unirest lib
	 */
	protected static InputStream requestGetFile(String route) throws UnirestException 
	{
		InputStream byteResponse = Unirest.get(route)
			.asBinary().getBody();
		return byteResponse;
	}
	
	/**
	 * 
	 * @param route
	 * @param parameters
	 * @return
	 * @throws UnirestException exception from unirest lib
	 */
	protected static HttpResponse<JsonNode> requestPatch(String route, HashMap<String, Object> parameters) throws UnirestException {
		HttpResponse<JsonNode> jsonResponse = Unirest.patch(route)
			.fields(parameters)
			.asJson();
		
		return jsonResponse;
	}
}
