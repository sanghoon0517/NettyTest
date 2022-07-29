package kr.co.jsh.test;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;

public class SimpleJSONTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONObject jo = new JSONObject();
		String prettyJson = "";
		
		jo.put("author", "jsh");
		jo.put("data", "TEST DATA");
		
		System.out.println(jo.toString());
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jo);
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
		
		System.out.println(prettyJson);
		
		
		SimpleJSONObj sJsonObj = new SimpleJSONObj();
		JSONObject json = sJsonObj.getTestData();
		String prettyJSON = sJsonObj.prettyJson(json);
		
		System.out.println(json);
		System.out.println(prettyJSON);
	}

}
