package TestCases;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC_ReqResAPIs {
	public String id = "";
	@Test(priority = 1, enabled = true)
	public void test_listOfAllObjects(){
		Response response = RestAssured.get("https://api.restful-api.dev/objects");
		System.out.println(response.getBody().asPrettyString());
		/**validations
		 * Status code : 200
		 * Content-Type : "application/json"
		 * */
		
		Assert.assertEquals(response.getStatusCode(), 200);
		System.out.println("Status code = " + response.getStatusCode());
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
		System.out.println("Content-Type = " + response.getHeader("Content-Type"));
		System.out.println("**********************************");
	}
	
	@Test(priority = 2, enabled = true)
	public void test_addObject() {
		//URL : https://api.restful-api.dev/objects
		RestAssured.baseURI = "https://api.restful-api.dev";
		RequestSpecification rs = RestAssured.given();
		rs.header("Content-Type", "application/json");
		
		JSONObject obj = new JSONObject();
		obj.put("name","Apple MacBook Pro 16");
		
		JSONObject data = new JSONObject();
		data.put("year", "2019");
		data.put("price", "1849.99");
		data.put("CPU Model", "Intel Core i9");
		data.put("Hard Disk Size", "1 TB");
		
		obj.put("data", data);
		
		rs.body(obj.toString());
		Response response = rs.post("/objects");
		System.out.println(response.getBody().asPrettyString());
		
		//Validations
		Assert.assertEquals(response.getStatusCode(), 200);
		System.out.println("Status code = " + response.getStatusCode());
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
		System.out.println("Content-Type = " + response.getHeader("Content-Type"));
		Assert.assertEquals(response.getBody().asString().contains(data.toString()), true);
		System.out.println("**********************************");
		
		//Updating id
		JsonPath jsonpath = response.jsonPath();
		id = jsonpath.getString("id");
		//System.out.println(id);
	}
	
	@Test(priority = 3, enabled = true)
	public void test_listOfObjectsById() {
		Response response = RestAssured.get("https://api.restful-api.dev/objects?id=" + id);
		System.out.println(response.getBody().asPrettyString());
		
		//Validations
		Assert.assertEquals(response.getStatusCode(), 200);
		System.out.println("Status code = " + response.getStatusCode());
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
		System.out.println("Content-Type = " + response.getHeader("Content-Type"));
		Assert.assertEquals(response.getBody().asString().contains(id), true);
		System.out.println("**********************************");
	}
	
	@Test(priority = 4, enabled = true)
	public void test_updateObject() {
		RestAssured.baseURI = "https://api.restful-api.dev";
		RequestSpecification rs = RestAssured.given();
		rs.header("Content-Type","application/json");

		JSONObject obj = new JSONObject();
		obj.put("name", "Apple MacBook Pro 16");
		
		JSONObject data = new JSONObject();
		data.put("year", "2019");
		data.put("price", "2049.99");
		data.put("CPU model", "Intel Core i9");
		data.put("Hard disk size", "1 TB");
		data.put( "color", "silver");
		
		obj.put("data", data);
		
		rs.body(obj.toString());
		Response response = rs.put("/objects/" + id);
		System.out.println(response.getBody().asPrettyString());
		
		//Validations
		Assert.assertEquals(response.getStatusCode(), 200);
		System.out.println("Status code = " + response.getStatusCode());
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
		System.out.println("Content-Type = " + response.getHeader("Content-Type"));
		Assert.assertEquals(response.getBody().asString().contains(data.toString()), true);
		System.out.println("**********************************");
		
	}
	
	@Test(priority = 5, enabled = true)
	public void test_partiallyUpdateObject() {
		RestAssured.baseURI = "https://api.restful-api.dev";
		RequestSpecification rs = RestAssured.given();
		rs.header("Content-Type", "application/json");
		
		JSONObject obj = new JSONObject();
		obj.put("name", "Apple MacBook Pro 16 - updated");
		
		rs.body(obj.toString());
		Response response = rs.patch("/objects/" + id);
		System.out.println(response.getBody().asPrettyString());
		
		//Validations
		Assert.assertEquals(response.getStatusCode(), 200);
		System.out.println("Status code = " + response.getStatusCode());
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
		System.out.println("Content-Type = " + response.getHeader("Content-Type"));
		JsonPath jsonpath = response.jsonPath();
		String name = jsonpath.getString("name");
		Assert.assertEquals(name.equals("Apple MacBook Pro 16 - updated"), true);
		System.out.println("**********************************");
	}
	
	@Test(priority = 6, enabled = true)
	public void test_deleteObject() {
		Response response = RestAssured.delete("https://api.restful-api.dev/objects/" + id);
		
		//Validation
		Assert.assertEquals(response.getStatusCode(), 200);
		System.out.println("Status code = " + response.getStatusCode());
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
		System.out.println("Content-Type = " + response.getHeader("Content-Type"));
		//Assert.assertEquals(response.getBody().asString().contains("Object with id = " + id + " has been deleted."), true);
		JsonPath jsonpath = response.jsonPath();
		String message = jsonpath.getString("message");
		Assert.assertEquals(message.equals("Object with id = " + id + " has been deleted."), true);
		System.out.println("**********************************");
	}
}
