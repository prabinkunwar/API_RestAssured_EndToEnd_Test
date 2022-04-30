package testCases;

import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class EndToEndProductUpdateTest {

	SoftAssert softAssert;
	Map<String, String> createPayloadMap;
	Map<String,String> updatePayloadMap;
	String expectedProductName;
	String expectedProductPrice;
	String expectedProductDescription;
	String firstProductId;
	
	public EndToEndProductUpdateTest() {
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
	
	public Map<String,String> updatePayloadMap(){
		
		updatePayloadMap = new HashMap<String, String>();
		
		updatePayloadMap.put("id", firstProductId);
		updatePayloadMap.put("name", "Prabin's QA Automation Book 2.0");
		updatePayloadMap.put("price", "59");
		updatePayloadMap.put("description", "The best updated Automation book for Beginners and Intermediate.");
		updatePayloadMap.put("category_id", "2");
	
		return updatePayloadMap;
		
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
	public void updateProduct() {
		
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type", "application/json; charset=UTF-8")
								.auth().preemptive().basic("demo@techfios.com", "abc123")
								.body( updatePayloadMap()).
							when()
								.put("/update.php").
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
		softAssert.assertEquals(actualProductMessage, "Product was updated.", "Product message not matching!");
				
		softAssert.assertAll();
	
	}
	
	@Test(priority=3)
	public void readOneProduct() {
		
		expectedProductName = updatePayloadMap.get("name");
		System.out.println("Expected Product Name: " + expectedProductName);
		
		expectedProductPrice = updatePayloadMap.get("price");
		System.out.println("Expected Product Price: " + expectedProductPrice);
		
		expectedProductDescription = updatePayloadMap.get("description");
		System.out.println("Expected Product Description: " + expectedProductDescription);
		
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
		softAssert.assertEquals(actualResponseStatus, 200, "Status codes are not matching!");
		
		String actualResponseContentType = response.getHeader("Content-Type");
		System.out.println("Actual Response Content Type: " + actualResponseContentType);
		softAssert.assertEquals(actualResponseContentType, "application/json", "Response Content-Type are not matching!");
		
		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body: " + actualResponseBody);
		
		JsonPath jp = new JsonPath(actualResponseBody);
		String actualProductID = jp.get("id");
		softAssert.assertEquals(actualProductID, firstProductId, "Product ID not matching!");
		
		String actualProductName = jp.get("name");
		softAssert.assertEquals(actualProductName, expectedProductName, "Product names are not matching!");
		
		String actualProductPrice = jp.get("price");
		softAssert.assertEquals(actualProductPrice, expectedProductPrice, "Product prices are not matching!");
		
		String actualProductDescription = jp.get("description");
		softAssert.assertEquals(actualProductDescription, expectedProductDescription, "Product description are not matching!");
				
		softAssert.assertAll();
	
	}
	
}
