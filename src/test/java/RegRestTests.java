import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class RegRestTests {

    @BeforeEach
    public void setup(){

        RestAssured.baseURI = "https://reqres.in"; // El baseURI es el dominio sin el path, en este caso https://reqres.in
        RestAssured.basePath = "/api"; // El basePath es la palabra que sigue despues del host en este caso /api
        RestAssured.filters(new RequestLoggingFilter(),new ResponseLoggingFilter());
    }
    @Test
    public void loginTest(){

        //Esta es una peticion POST y lleva un body de manera obligatoria y el metodo es .post en la URL
                given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .post("login")
                .then()
                .statusCode(HttpStatus.SC_OK)//HttpStatus nos sirve para pasarle el resultado esperado
                .body("token",notNullValue());

       // System.out.println(response);
    }

    @Test
    public void getSingleUserTest(){

        //Esta es una peticion GET y no lleva body ya que extrae informacion y el metodo es tipo .get en la URL
                 given()
                .contentType(ContentType.JSON)
                .get("users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)//HttpStatus nos sirve para pasarle el resultado esperado
                .body("data.id",equalTo(2));

        // System.out.println(response);
    }
}

