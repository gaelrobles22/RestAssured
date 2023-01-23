import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {

    private static final Logger logger = LogManager.getLogger(RegRestTests.class);

    @BeforeAll
    public static void setup(){

        logger.info("Iniciando la configuracion");
        RestAssured.requestSpecification =  defaultRequestSpecification();
        logger.info("Configuracion exitosa.");
    }

    //Con este metodo creamos las especificaciones default para el proyecto, tipo base y se pueden crear varias
    private static RequestSpecification defaultRequestSpecification(){

        List<Filter> filters = new ArrayList<>();
        filters.add(new RequestLoggingFilter());
        filters.add(new ResponseLoggingFilter());
        filters.add(new AllureRestAssured());

        return new RequestSpecBuilder().setBaseUri("https://reqres.in")
                .setBasePath("/api")
                .addFilters(filters)
                .setContentType(ContentType.JSON)
                .build();
    }

    private RequestSpecification prodRequestSpecification(){

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
}
