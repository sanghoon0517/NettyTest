package kr.co.jsh.test;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SimpleJSONObj {
	private JSONObject json;
	private ObjectMapper mapper;
	private JSONParser parser;
	
	public JSONObject getTestData() {
		
		this.mapper = new ObjectMapper();
		this.parser = new JSONParser();
		
		JSONObject retJson = new JSONObject();
		String retData = "";
		
		retJson.put("author", "jsh");
		retJson.put("data", "TEST DATA");
		
		return retJson;
		
	}
	
	public String prettyJson(JSONObject obj) {
		
		this.mapper = new ObjectMapper();
		String retData = "";
		try {
			retData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retData;
	}
	
}
