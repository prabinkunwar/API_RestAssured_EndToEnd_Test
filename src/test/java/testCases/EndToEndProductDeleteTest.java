package testCases;

import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class EndToEndProductDeleteTest {

	SoftAssert softAssert;
	Map<String, String> createPayloadMap;
	Map<String, String> deletePayloadMap;
	String expectedProductName;
	String expectedProductPrice;
	String expectedProductDescription;
	String firstProductId;
	
	public EndToEndProductDeleteTest() {
		softAssert = new SoftAssert();
	}
	
	public Map<String,String> createPayloadMap(){
		
		createPayloadMap = new HashMap<String, String>();
		
		createPayloadMap.put("name", "Prabin's QA Automation Book");
		createPayloadMap.put("price", "49");
		createPayloadMap.put("description", "The best Automation book for beginners.");
		createPayloadMap.put("category_id", "2");
	
		return createPayloadMap;
		
	}
	
	public Map<String,String> deletePayloadMap(){
		
		deletePayloadMap = new HashMap<String, String>();
		
		deletePayloadMap.put("id", firstProductId);

		return deletePayloadMap;
		
	}
	
	@Test(priority=0)
	public void createNewProduct() {
			
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type", "application/json; charset=UTF-8")
								.auth().preemptive().basic("demo@techfios.com", "abc123")
								.body(createPayloadMap()).
							when()
								.post("/create.php").
							then()
								.extract().response();
		
		int actualResponseStatus = response.getStatusCode();
		System.out.println("Actual Response Status: " + actualResponseStatus);
		softAssert.assertEquals(actualResponseStatus, 201, "Status codes are not matching!");
		
		String actualResponseContentType = response.getHeader("Content-Type");
		System.out.println("Actual Response Content Type: " + actualResponseContentType);
		softAssert.assertEquals(actualResponseContentType, "application/json; charset=UTF-8", "Response Content-Type are not matching!");
		
		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body: " + actualResponseBody);
		
		JsonPath jp = new JsonPath(actualResponseBody);
		String productMessage = jp.get("message");
		softAssert.assertEquals(productMessage, "Product was created.", "Product message's are not matching!");
			
		softAssert.assertAll();
	
	}

	@Test(priority=1)
	public void readAllProducts() {
		
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type", "application/json; charset=UTF-8")
								.auth().preemptive().basic("demo@techfios.com", "abc123").
							when()
								.get("/read.php").
							then()
								.extract().response();
		
		int actualResponseStatus = response.getStatusCode();
		System.out.println("Actual Response Status: " + actualResponseStatus);
		softAssert.assertEquals(actualResponseStatus, 200);
		
		String actualResponseBody = response.getBody().asString();
		
		JsonPath jp = new JsonPath(actualResponseBody);
		firstProductId = jp.get("records[0].id");

		System.out.println("firstProductId :" +firstProductId);
		
		softAssert.assertAll();
	}
	
	@Test(priority=2)
	public void deleteProduct() {
		
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type", "application/json; charset=UTF-8")
								.auth().preemptive().basic("demo@techfios.com", "abc123")
								.body(deletePayloadMap()).
							when()
								.delete("/delete.php").
							then()
								.extract().response();
		
		int actualResponseStatus = response.getStatusCode();
		System.out.println("Actual Response Status: " + actualResponseStatus);
		softAssert.assertEquals(actualResponseStatus, 200, "Status codes are not matching!");
		
		String actualResponseContentType = response.getHeader("Content-Type");
		System.out.println("Actual Response Content Type: " + actualResponseContentType);
		softAssert.assertEquals(actualResponseContentType, "application/json; charset=UTF-8", "Response Content-Type are not matching!");
		
		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body: " + actualResponseBody);
		
		JsonPath jp = new JsonPath(actualResponseBody);
		String actualProductMessage = jp.get("message");
		softAssert.assertEquals(actualProductMessage, "Product was deleted.", "Product message not matching!");
				
		softAssert.assertAll();
	
	}
	
	@Test(priority=3)
	public void readDeletedProduct() {
				
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type", "application/json")
								.auth().preemptive().basic("demo@techfios.com", "abc123")
								.queryParam("id", firstProductId).
							when()
								.get("/read_one.php").
							then()
								.extract().response();
		
		int actualResponseStatus = response.getStatusCode();
		System.out.println("Actual Response Status: " + actualResponseStatus);
		softAssert.assertEquals(actualResponseStatus, 404, "Status codes are not matching!");
		
		String actualResponseContentType = response.getHeader("Content-Type");
		System.out.println("Actual Response Content Type: " + actualResponseContentType);
		softAssert.assertEquals(actualResponseContentType, "application/json", "Response Content-Type are not matching!");
		
		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body: " + actualResponseBody);
		
		JsonPath jp = new JsonPath(actualResponseBody);
		
		String actualProductMessage = jp.get("message");
		softAssert.assertEquals(actualProductMessage, "Product does not exist.", "Product Message not matching!");
		
		softAssert.assertAll();
	
	}
	
}
