package com.signaturit.api.java_sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	 * @param inputStream
	 * @param path
	 */
	protected static void writeToPath(InputStream inputStream, String path) 
	{
		OutputStream outputStream = null;
		
		try {
			outputStream = new FileOutputStream(new File(path));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {

					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

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
			    if (entry.getKey().equals("data")) {
			    	Map<String, Object> map = (Map<String, Object>) entry.getValue();
			    	
			    	for(Entry<String, Object> valueEntry : map.entrySet()) {
			    		route += String.format(
			    			"&data[%s]=%s", valueEntry.getKey(), valueEntry.getValue()
			    		);
			    	}
			    	continue;
			    }
			    
			    if (entry.getKey().equals("ids")) {
			    	String.join(",", (CharSequence[]) entry.getValue());
			    }
			    
			    route += String.format(
			    	"&%s=%s", entry.getKey(), entry.getValue()
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
	protected static void parseParameters(Map<String, Object> parameters, ArrayList<HashMap<String, Object>> recipients, String key) {
		int i = 0;
		for ( HashMap<String, Object> recipient: recipients ) {
			for (Entry<String, Object>  entry : recipient.entrySet()) {
				
				if (key.equals("recipients")) {
					parameters.put(key+"["+i+"]["+entry.getKey()+"]", entry.getValue());
				} else {
					parameters.put(key+"["+entry.getKey()+"]", entry.getValue());
				}
			}
			++i;
		}
	}
	
	/**
	 * 
	 * @param route
	 * @param parameters
	 * @param files
	 * @return
	 * @throws UnirestException
	 */
	protected static String requestPost(String route, Map<String, Object> parameters, ArrayList<String> files) throws UnirestException 
	{
		MultipartBody request = Unirest.post(route)
				.fields(parameters);
			
		if (files != null) {
			for (String temp : files) {
				request.field("files", new File(temp));
			}
		}
		
		return request.asString().getBody().toString();
	}
	
	/**
	 * 
	 * @param route
	 * @param parameters
	 * @param file
	 * @return
	 * @throws UnirestException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	protected static String requestPut(String route, Map<String, Object> parameters, String file) throws UnirestException, URISyntaxException, IOException 
	{
		
		final InputStream stream = new FileInputStream(file);
		final byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
		stream.close();
		final HttpResponse<JsonNode> jsonResponse = Unirest.put(route)
			.body(bytes)
			.asJson();
		
		return jsonResponse.getBody().toString();
	}
	
	/**
	 * 
	 * @param route
	 * @return
	 * @throws UnirestException
	 */
	protected static String requestGet(String route) throws UnirestException 
	{
		HttpResponse<JsonNode> jsonResponse = Unirest.get(route)
				.asJson();
		
		return jsonResponse.getBody()
				.toString();
	}
	
	/**
	 * 
	 * @param route
	 * @return
	 * @throws UnirestException
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
	 * @throws UnirestException
	 */
	protected static String requestPatch(String route, HashMap<String, Object> parameters) throws UnirestException {
		HttpResponse<JsonNode> jsonResponse = Unirest.patch(route)
			.fields(parameters)
			.asJson();
		
		return jsonResponse.getBody().toString();
	}

	
}
