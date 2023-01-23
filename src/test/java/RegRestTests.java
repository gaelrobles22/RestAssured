import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static io.restassured.path.json.JsonPath.from;


public class RegRestTests {


    /*@Test
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


    //Esta es la mejor forma de manejar las respuestas de APIS por medio de objetos
    @Test
    public void registerUserTest(){

        CreateUserRequest user = new CreateUserRequest();
        user.setEmail("eve.holt@reqres.in");
        user.setPassword("pistol");

        CreateUserResponse userResponse = given()
                .when()
                .body(user)
                .post("register")
                .then()
                .spec(defaultResponseSpecification())
                .contentType(equalTo("application/json; charset=utf-8"))
                .extract()
                .body()
                .as(CreateUserResponse.class);
        assertThat(userResponse.getId(),equalTo(4));
        assertThat(userResponse.getToken(),equalTo("QpwL5tke4Pnpja7X4"));
    }


    //Con este metodo creamos las especificaciones default para el proyecto, tipo base y se pueden crear varias
    private RequestSpecification defaultRequestSpecification(){

        List<Filter> filters = new ArrayList<>();
        filters.add(new RequestLoggingFilter());
        filters.add(new ResponseLoggingFilter());

        return new RequestSpecBuilder().setBaseUri("https://reqres.in")
                .setBasePath("/api")
                .addFilters(filters)
                .setContentType(ContentType.JSON)
                .build();
    }

    private RequestSpecification prodtRequestSpecification(){

        return new RequestSpecBuilder().setBaseUri("https://prod.reqres.in")//es un ejemplo para tomar esta especificacion
                .setBasePath("/api")
                .setContentType(ContentType.JSON)
                .build();
    }

    private ResponseSpecification defaultResponseSpecification(){
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .expectContentType(ContentType.JSON)
                .build();
    }

     */

}

