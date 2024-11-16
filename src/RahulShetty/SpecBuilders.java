package RahulShetty;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.List;

import POJO.Location;
import POJO.addPlace;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class SpecBuilders {

	public static void main(String[] args) {

		addPlace a = new addPlace();
		a.setAccuracy(50);
		a.setAddress("ikkade");
		a.setLanguage("Telugu");

		Location l = new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		a.setLocation(l);
		a.setName("Ma ille");
		a.setPhone_number("123456789");

		List<String> t = Arrays.asList("bed", "food");
		a.setTypes(t);
		a.setWebsite("edo okati.com");

		// adding request & response spec builders
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();
		ResponseSpecification res = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON)
				.build();

		// calling spec builders
		// Add Place
		RequestSpecification response = given().log().all().spec(req).body(a);
		String postResponse = response.when().post("maps/api/place/add/json").then().spec(res).extract().response()
				.asString();

		System.out.println(postResponse);

		JsonPath jp = new JsonPath(postResponse); // JsonPath class to extract string from Json file
		String placeID = jp.getString("place_id");
		System.out.println("Place ID: " + placeID);

		// Get Place
		given().spec(req).queryParam("place_id", placeID).when().get("maps/api/place/get/json")
				.then().log().all().spec(res).extract().response().asString();

	}
}
