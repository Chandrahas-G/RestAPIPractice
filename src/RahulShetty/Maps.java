package RahulShetty;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class Maps {

	public static void main(String[] args) throws IOException {
		
		// Add Place
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String postResponse = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				// parsing data directly through JSON File
		.body(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "\\src\\files\\addPlace.json"))))
		.when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
		
		System.out.println("Post Response is "+postResponse);
		
		JsonPath jp = new JsonPath(postResponse); // JsonPath class to extract string from Json file
		String placeID = jp.getString("place_id");
		System.out.println("Place ID: "+placeID);
		
		
		// Update Place
		String newAddress = "70 Summer walk, USA";
		given().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeID+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo ("Address successfully updated"));
		
		
		// Get Place
		String getResponse = given().queryParam("key", "qaclick123").queryParam("place_id", placeID)
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath jp1 = new JsonPath(getResponse);
		String Address = jp1.getString("address");
		Assert.assertEquals(newAddress, Address);
		System.out.println("New Address: "+Address);
	}
}
