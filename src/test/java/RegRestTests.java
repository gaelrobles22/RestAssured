import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static io.restassured.path.json.JsonPath.from;


public class RegRestTests {

    @BeforeEach
    public void setup(){

        RestAssured.baseURI = "https://reqres.in"; // El baseURI es el dominio sin el path, en este caso https://reqres.in
        RestAssured.basePath = "/api"; // El basePath es la palabra que sigue despues del host en este caso /api
        RestAssured.filters(new RequestLoggingFilter(),new ResponseLoggingFilter());

        RestAssured.requestSpecification =  new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
    }
    @Test
    public void loginTest(){

        //Esta es una peticion POST y lleva un body de manera obligatoria y el metodo es .post en la URL
                given()
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
                .get("users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)//HttpStatus nos sirve para pasarle el resultado esperado
                .body("data.id",equalTo(2));

    }

    @Test
    public void deleteUserTest(){


                 given()
                .delete("users/2")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);//HttpStatus nos sirve para pasarle el resultado esperado

    }

    @Test
    public void patchUserTest(){

        String nameUpdated = given()
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .patch("users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)//HttpStatus nos sirve para pasarle el resultado esperado
                .extract()//Hasemos una assercion para revisar que nos devuelva el dato name
                .jsonPath().getString("name");

        assertThat(nameUpdated,equalTo("morpheus"));

    }

    @Test
    public void putUserTest(){

        String jobUpdated = given()
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .put("users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)//HttpStatus nos sirve para pasarle el resultado esperado
                .extract()//Hasemos una assercion para revisar que nos devuelva el dato name
                .jsonPath().getString("job");

        assertThat(jobUpdated,equalTo("zion resident"));
    }

    @Test
    public void getAllUsersTest(){
        Response response = given()
                .get("users?page=2");

        Headers headers = response.getHeaders();
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        String contenType = response.getContentType();

        assertThat(statusCode,equalTo(HttpStatus.SC_OK));
        System.out.println("body" + body);
        System.out.println("content type" + contenType);
        System.out.println("Headers" + headers.toString());

        System.out.println("********");
        System.out.println(headers.get("Content-type"));
        System.out.println(headers.get("Transfer-Encoding"));

    }

    @Test
    public void getAllUsersTest2(){
        String response = given()
                .when()
                .get("users?page=2")
                .then()
                .extract()
                .body()
                .asString();
        int page = from(response).get("page");
        int totalPages = from(response).get("total_pages");

        int idFirstUser = from(response).get("data[0].id");

        System.out.println("page: " + page );
        System.out.println("total pages: " + totalPages);
        System.out.println("id first user: "+ idFirstUser);

        //Creamos una Lista donde obtenga los usuarios que tengan id mayor a 10
        List<Map> usersWithIdGreaterThan10 = from(response).get("data.findAll { user -> user.id > 10 }");
        String email = usersWithIdGreaterThan10.get(0).get("email").toString();


        List<Map> user = from(response).get("data.findAll { user -> user.id > 10 && user.last_name == 'Howell'}");
        String id = user.get(0).get("id").toString();
    }

    @Test
    public void createUserTest() {
        String response = given()
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}")
                .post("users")
                .then()
                .extract()
                .body()
                .asString();
        User user = from(response).getObject("",User.class);
        System.out.println(user.getId());
        System.out.println(user.getJob());
    }
}

