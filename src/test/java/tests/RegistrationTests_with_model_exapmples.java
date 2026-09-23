package tests;

import io.restassured.http.ContentType;
import models.registration.model_examples.lombok.RegistrationBodyLombokModel;
import models.registration.model_examples.lombok.RegistrationResponseLombokModel;
import models.registration.model_examples.pojo.RegistrationBodyPojoModel;
import models.registration.model_examples.pojo.RegistrationResponsePojoModel;
import models.registration.model_examples.records.ExistingUser400ResponseRecordsModel;
import models.registration.model_examples.records.RegistrationBodyRecordsModel;
import models.registration.model_examples.records.RegistrationResponseRecordsModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import testdata.TestData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTests_with_model_exapmples {

    TestData testData = new TestData();
    String username = testData.randomUsername;
    String password = testData.randomPassword;

    @Test
    @Disabled
    public void successfulRegistrationTests_with_pojo() {
        RegistrationBodyPojoModel data = new RegistrationBodyPojoModel();
        data.setUsername(username);
        data.setPassword(password);

        RegistrationResponsePojoModel registrationResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(RegistrationResponsePojoModel.class);

        assertEquals(username, registrationResponse.getUsername());
    }

    @Test
    @Disabled
    public void successfulRegistrationTests_with_lombok() {
        RegistrationBodyLombokModel data = new RegistrationBodyLombokModel();
        data.setUsername(username);
        data.setPassword(password);

        RegistrationResponseLombokModel registrationResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(RegistrationResponseLombokModel.class);

        assertEquals(username, registrationResponse.getUsername());
    }

    @Test
    @Disabled
    public void successfulRegistrationTests_with_records() {
        RegistrationBodyRecordsModel data = new RegistrationBodyRecordsModel(username, password);

        RegistrationResponseRecordsModel registrationResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(RegistrationResponseRecordsModel.class);

        assertEquals(username, registrationResponse.username());
    }

    @Test
    @Disabled
    public void existingUser400RegistrationTests() {
        RegistrationBodyRecordsModel data = new RegistrationBodyRecordsModel(username, password);

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(username))
                .body("id", notNullValue());

        ExistingUser400ResponseRecordsModel response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(ExistingUser400ResponseRecordsModel.class);

        assertEquals(testData.expectedExistingUserError, response.username().get(0));
    }

    @Test
    @Disabled
    public void unsupportedMediaType301RegistrationTests() {
        RegistrationBodyPojoModel data = new RegistrationBodyPojoModel();
        data.setUsername(username);
        data.setPassword(password);

        given()
                .log().all()
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register")
                .then()
                .log().all()
                .statusCode(301);
    }
}
