package favqs;

import io.restassured.builder.*;
import io.restassured.http.*;
import io.restassured.response.Response;
import io.restassured.specification.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import favqs.helpers.PropertiesReader;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.HashMap;

public class FavqsFavQuotesTest {
	
    private RequestSpecification requestSpec;

    @BeforeEach
    public void createRequestSpec() {

        requestSpec =
            new RequestSpecBuilder().
                setBaseUri("https://favqs.com/api").
                build();
    }
	
    String queryStringSession = """
				{ 
				  "user": {
				    "login": "joaodcosta",
				    "password": "9409c8b596da61c"
				  }
				}
            """;

    String queryStringFav = """
				{ 
    				"id": 17605,
		            "author": "Brian Eno",
		            "body": "Sometimes you recognize that there is a category of human experience that has not been identified but everyone knows about it. That is when I find a term to describe it.",
                    "user_details": {
                        "favorite": "true",
						"User-Token": "i4dwXAUNfSUEaTP90K5RWOWLap5cvc9mtjfpvA+c0uufP5GYnpYZUO4k5pTEpwsJHVHNhY5lbLn0cDRpiwsOKA==",                        
                        "login": "joaodcosta",
                        "password": "9409c8b596da61c"
                    }   
				}
            """;    
    @Test
    public void createSession() throws IOException {
    	
        HashMap<String, Object> createSessionQuery = new HashMap<>();
        createSessionQuery.put("query", queryStringSession);        

    	PropertiesReader reader = new PropertiesReader("properties-from-pom.properties");
    	String token = reader.getProperty("token.property");
    	
    	Response response =
    	given().
    		spec(requestSpec).    	
            auth().
            oauth2(token).
            contentType(ContentType.JSON).
            body(createSessionQuery).            
        when().
            post("/session").
        then().
	        contentType(ContentType.JSON)
	        .extract().response();
		  	System.out.println ("Here comes the response" + response.asString ());
    }

    @Test
    public void favQuote() throws IOException {
    	
        HashMap<String, Object> favQuoteQuery = new HashMap<>();
        favQuoteQuery.put("query", queryStringFav);     
        
    	PropertiesReader reader = new PropertiesReader("properties-from-pom.properties");
    	String token = reader.getProperty("token.property");        

    	Response response =
    	given().
			spec(requestSpec).      	
            auth().
            oauth2(token).
            header("User-Token", "i4dwXAUNfSUEaTP90K5RWOWLap5cvc9mtjfpvA+c0uufP5GYnpYZUO4k5pTEpwsJHVHNhY5lbLn0cDRpiwsOKA==").
            contentType(ContentType.JSON).
            body(favQuoteQuery).            	
        when().
            put("/quotes/17605/fav").
        then().
	        contentType(ContentType.JSON)
	        .extract().response();
		  	System.out.println ("Here comes the response" + response.asString ());
    }    
    

}
