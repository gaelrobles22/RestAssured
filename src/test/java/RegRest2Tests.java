import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RegRest2Tests extends BaseTest{

    @Test
    public void getSingleUserTest(){
        given()
                .get("users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("data.id",equalTo(2));
    }
}
