package RahulShetty;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.List;

import POJO.Location;
import POJO.addPlace;
import io.restassured.RestAssured;

public class SerializationPOJO {
	
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
		
		//ArrayList<String> t = new ArrayList<String>();
		//t.add("shoe park");
		//t.add("shop");
		List<String> t = Arrays.asList("bed","food");
		a.setTypes(t);
		a.setWebsite("edo okati.com");
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String postResponse = given().log().all().queryParam("key", "qaclick123")
				.header("Content-Type", "application/json").body(a).when().post("maps/api/place/add/json").then()
				.assertThat().statusCode(200).extract().response().asString();

		System.out.println(postResponse);
	}
}