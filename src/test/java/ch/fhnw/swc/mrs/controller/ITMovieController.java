package ch.fhnw.swc.mrs.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ch.fhnw.swc.mrs.Application;
import ch.fhnw.swc.mrs.util.StatusCodes;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.*;

@Tag("integration")
class ITMovieController {
	
	private String baseUrl = "http://localhost:";
	
	@BeforeAll
	static void startApplication() throws Exception {
		Application.main(null);
	}

	@BeforeEach
	void setPort() throws Exception {
		baseUrl = baseUrl + Application.getPort();
	}

	@DisplayName("Get a movie by its id.")
	@Test
	void testGetMovieById() {
		given().
			contentType("application/json").
		when().
			get(baseUrl + "/movies/{id}", UUID.fromString("00000000-0000-0000-0000-000000000004")).
		then().
			statusCode(200).
			body("title", equalTo("Eragon"));
	}
	
	@DisplayName("Delete movie")
	@Test
	void testDeleteMovie() {
		String json = get(baseUrl + "/movies").asString();
		int elementsBefore = new JsonPath(json).getInt("size()");
		
		given().
		when().
			delete(baseUrl + "/movies/00000000-0000-0000-0000-000000000005").
		then().
			statusCode(StatusCodes.NO_CONTENT);

		json = get(baseUrl + "/movies").asString();
		int elementsAfter = new JsonPath(json).getInt("size()");
		assertEquals(elementsBefore, elementsAfter+1);
	}
		
	@DisplayName("Create movie")
	@Test
	void testCreateMovie() {
		String data = "?title=Metropolis&releaseDate=1922-09-07&priceCategory=Regular&ageRating=12";
		String json = get(baseUrl + "/movies").asString();

		int elementsBefore = new JsonPath(json).getInt("size()");
		
		given().
		when().
			post(baseUrl + "/movies" + data).
		then().
			statusCode(StatusCodes.CREATED).
			body("title", equalTo("Metropolis")).
		extract().
			response();
		
		json = get(baseUrl + "/movies").asString();
		int elementsAfter = new JsonPath(json).getInt("size()");
		assertEquals(elementsBefore, elementsAfter - 1);
	}
	
	@DisplayName("Update movie")
	@Test
	void testUpdateMovie() {
		String body = "{\r\n" 
		        + "        \"id\": \"00000000-0000-0000-0000-000000000006\",\r\n" 
		        + "        \"rented\": false,\r\n" 
		        + "        \"title\": \"Live Free or Die Hard\",\r\n" 
		        + "        \"releaseDate\": \"2017-06-27\",\r\n" 
		        + "        \"priceCategory\": \"Children\",\r\n" 
		        + "        \"ageRating\": 6\r\n" 
		        + "    }";
		String json = get(baseUrl + "/movies").asString();
		int elementsBefore = new JsonPath(json).getInt("size()");
		
		given().
			body(body).
		when().
			put(baseUrl + "/movies/00000000-0000-0000-0000-000000000006").
		then().
			statusCode(StatusCodes.OK).
			body("priceCategory", equalTo("Children")).
			body("ageRating", equalTo(6));

		json = get(baseUrl + "/movies").asString();
		int elementsAfter = new JsonPath(json).getInt("size()");
		assertEquals(elementsBefore, elementsAfter);
	}
		
	@AfterAll
	static void stopSpark() throws Exception {
		Application.stop();
		Thread.sleep(1000);
	}

}
