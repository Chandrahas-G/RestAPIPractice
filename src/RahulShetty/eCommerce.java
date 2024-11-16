package RahulShetty;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import POJO.Orders;
import POJO.eComLogin;
import POJO.eComOrders;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class eCommerce {

	public static void main(String[] args) {

		System.out.println("Login");
		eComLogin L = new eComLogin();
		L.setUserEmail("chandrahas@gmail.com");
		L.setUserPassword("Micky@021");

		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();

		RequestSpecification login = given().spec(req).body(L);
		eComLogin resp = login.when().post("/api/ecom/auth/login").then().extract().response().as(eComLogin.class);

		System.out.println(resp.getMessage());
		String UserID = resp.getUserId();
		String Token = resp.getToken();

		System.out.println("\nCreate Product");
		RequestSpecification req1 = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", Token).build();

		RequestSpecification createProduct = given().spec(req1).param("productName", "HP Laptop")
				.param("productAddedBy", UserID).param("productCategory", "Electronics")
				.param("productSubCategory", "Laptops").param("productPrice", "1500").param("productDescription", "HP")
				.param("productFor", "All")
				.multiPart("productImage", new File("C:\\Users\\chand\\Postman\\files\\laptop.jpg"));
		String resp1 = createProduct.when().post("/api/ecom/product/add-product").then().extract().response()
				.asString();

		String productId = new JsonPath(resp1).getString("productId");
		System.out.println(new JsonPath(resp1).getString("message"));

		System.out.println("\nPlace Order");
		Orders o = new Orders();
		o.setCountry("India");
		o.setProductOrderedId(productId);
		List<Orders> O = new ArrayList<Orders>();
		O.add(o);
		eComOrders ord = new eComOrders();
		ord.setOrders(O);

		RequestSpecification placeOrder = given().spec(req1).contentType(ContentType.JSON).body(ord);
		String orders = placeOrder.when().post("/api/ecom/order/create-order").then().extract().asString();

		Object orderId = new JsonPath(orders).getList("orders").get(0);
		System.out.println(new JsonPath(orders).getString("message"));

		System.out.println("\nGet Order Details");
		RequestSpecification GetOrderDetails = given().spec(req1).queryParam("id", orderId);
		String god =GetOrderDetails.when().get("/api/ecom/order/get-orders-details").then().extract().asString();
		System.out.println(new JsonPath(god).getString("message"));

		System.out.println("\nDelete Order");
		RequestSpecification DeleteOrder = given().spec(req1);
		String DelResp = DeleteOrder.when().delete("/api/ecom/order/delete-order/" + orderId + "").then().extract()
				.asString();
		System.out.println(new JsonPath(DelResp).getString("message"));

		System.out.println("\nDelete Product");
		RequestSpecification DeleteProd = given().spec(req1).pathParam("productId", productId);
		String DelProdResp = DeleteProd.when().delete("/api/ecom/product/delete-product/{productId}").then().extract()
				.asString();
		System.out.println(new JsonPath(DelProdResp).getString("message"));
	}
}
