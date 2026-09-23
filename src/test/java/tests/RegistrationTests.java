package tests;

import io.restassured.http.ContentType;
import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseSuccessfulModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RegistrationTests extends TestBase{

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        username = faker.name().firstName() + "_" + System.currentTimeMillis();
        password = faker.name().lastName();
    }

    @Test
    public void successfulRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        RegistrationResponseSuccessfulModel registrationResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/registration/successful_registration_response_schema.json"))
                .extract()
                .as(RegistrationResponseSuccessfulModel.class);

        assertThat(username).isEqualTo(registrationResponse.username());
        assertThat(registrationResponse.id()).isNotNull();
        assertThat(registrationResponse.firstName()).isBlank();
        assertThat(registrationResponse.lastName()).isBlank();
        assertThat(registrationResponse.email()).isBlank();
    }

    @Test
    public void existingUserRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        RegistrationResponseSuccessfulModel firstRegistrationResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/registration/successful_registration_response_schema.json"))
                .extract()
                .as(RegistrationResponseSuccessfulModel.class);

        assertThat(username).isEqualTo(firstRegistrationResponse.username());

        ExistingUserResponseModel secondRegistrationResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(registrationData)
                .when()
                .basePath("/api/v1")
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("schemas/registration/existing_user_registration_response_schema.json"))
                .extract()
                .as(ExistingUserResponseModel.class);

        String expectedError = "A user with that username already exists.";
        String actualError = secondRegistrationResponse.username().getFirst();
        assertThat(actualError).isEqualTo(expectedError);
    }

}
