import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;


public class RegRestTests {

    @Before
    public void setup(){

        RestAssured.baseURI = "https://reqres.in"; // El baseURI es el dominio sin el path, en este caso https://reqres.in
        RestAssured.basePath = "/api"; // El basePath es la palabra que sigue despues del host en este caso /api
    }
    @Test
    public void loginTest(){

        //Esta es una peticion POST y lleva un body de manera obligatoria y el metodo es .post en la URL
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .post("login")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("token",notNullValue());

       // System.out.println(response);
    }

    @Test
    public void getSingleUserTest(){

        //Esta es una peticion GET y no lleva body ya que extrae informacion y el metodo es tipo .get en la URL
        RestAssured
                .given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .get("users/2")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("data.id",equalTo(2));

        // System.out.println(response);
    }
}

