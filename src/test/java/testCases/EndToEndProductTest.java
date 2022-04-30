package testCases;

import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class EndToEndProductTest {

	SoftAssert softAssert;
	Map<String, String> payloadMap;
	String expectedProductName;
	String expectedProductPrice;
	String expectedProductDescription;
	String firstProductId;
	
	public EndToEndProductTest() {
		softAssert = new SoftAssert();
	}
	
	public Map<String,String> createPayloadMap(){
		
		payloadMap = new HashMap<String, String>();
		payloadMap.put("name", "Prabin's QA Automation Book");
		payloadMap.put("price", "49");
		payloadMap.put("description", "The best Automation book for beginners.");
		payloadMap.put("category_id", "2");
	
		return payloadMap;
		
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
		
		expectedProductName = payloadMap.get("name");
		System.out.println("Expected Product Name: " + expectedProductName);
		
		expectedProductPrice = payloadMap.get("price");
		System.out.println("Expected Product Price: " + expectedProductPrice);
		
		expectedProductDescription = payloadMap.get("description");
		System.out.println("Expected Product Description: " + expectedProductDescription);
		
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
	public void readOneProduct() {
		
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
				
		softAssert.assertAll();
	
	}
	
}
