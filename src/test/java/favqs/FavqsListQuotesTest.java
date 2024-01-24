package favqs;

import io.restassured.builder.*;
import io.restassured.http.*;
import io.restassured.response.Response;
import io.restassured.specification.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import favqs.helpers.PropertiesReader;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;


public class FavqsListQuotesTest {
	
    private RequestSpecification requestSpec;

    @BeforeEach
    public void createRequestSpec() throws IOException {
        requestSpec =
            new RequestSpecBuilder().
                setBaseUri("https://favqs.com/api").
                build();
    }
      
    @Test
    public void getQuotesContainingFunny() throws IOException {
    	PropertiesReader reader = new PropertiesReader("properties-from-pom.properties");
    	String token = reader.getProperty("token.property");

    	Response response =
    	given().
        	spec(requestSpec).
            auth().
            oauth2(token).
            queryParam("filter", "funny").
        when().
            get("/quotes").
        then().
            assertThat().
            body("quotes.tags[0]", hasItem("funny")).            
            statusCode(200).
        and().
            contentType(ContentType.JSON)
            .extract().response();
    }    
    
    @Test
    public void getQuotesTagFunny() throws IOException {
    	PropertiesReader reader = new PropertiesReader("properties-from-pom.properties");
    	String token = reader.getProperty("token.property");

    	Response response =
    	given().
        	spec(requestSpec).
            auth().
            oauth2(token).
            queryParam("filter", "funny").
            queryParam("type", "tag").
        when().
            get("/quotes").
        then().
            assertThat().
            body("quotes.tag", hasSize(greaterThan(0))).            
            statusCode(200).
        and().
            contentType(ContentType.JSON)
            .extract().response();	
    }
    
    @Test
    public void getQuotesbyMarkTwain() throws IOException {
    	PropertiesReader reader = new PropertiesReader("properties-from-pom.properties");
    	String token = reader.getProperty("token.property");

    	Response response =
    	given().
        	spec(requestSpec).
            auth().
            oauth2(token).
            queryParam("filter", "Mark Twain").
            queryParam("type", "author").
        when().
            get("/quotes").
        then().
            assertThat().
            body("quotes.author", hasItems("Mark Twain")).            
            statusCode(200).
        and().
            contentType(ContentType.JSON)
            .extract().response();
    }    

}
