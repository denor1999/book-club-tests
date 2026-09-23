package tests;

import io.restassured.http.ContentType;
import models.registration.*;
import models.registration.model_examples.EmptyPasswordRegistrationBodyModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.registration.RegistrationSpec.*;

public class RegistrationTests extends TestBase{

    @Test
    public void successfulRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomUsername, testData.randomPassword);

        RegistrationResponseSuccessfulModel registrationResponse = given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationRequestSpec)
                .extract()
                .as(RegistrationResponseSuccessfulModel.class);

        assertThat(testData.randomUsername).isEqualTo(registrationResponse.username());
        assertThat(registrationResponse.id()).isNotNull();
        assertThat(registrationResponse.firstName()).isBlank();
        assertThat(registrationResponse.lastName()).isBlank();
        assertThat(registrationResponse.email()).isBlank();
    }


    @Test
    public void existingUserRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomUsername, testData.randomPassword);

        RegistrationResponseSuccessfulModel firstRegistrationResponse = given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationRequestSpec)
                .extract()
                .as(RegistrationResponseSuccessfulModel.class);

        assertThat(testData.randomUsername).isEqualTo(firstRegistrationResponse.username());

        ExistingUserResponseModel secondRegistrationResponse = given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationRequestSpec)
                .extract()
                .as(ExistingUserResponseModel.class);

        String actualError = secondRegistrationResponse.username().getFirst();
        assertThat(actualError).isEqualTo(testData.expectedExistingUserError);
    }

    @Test
    public void wrongUsernameRegistrationTest() {
        RegistrationBodyModel wrongUsernameRegistrationData = new RegistrationBodyModel(
                testData.wrongRegistrationUsername, testData.randomPassword);

        WrongUsernameRegistrationResponseModel wrongUsernameResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(wrongUsernameRegistrationData)
                .basePath("api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("schemas/registration/wrong_username_registration_response_schema.json"))
                .extract()
                .as(WrongUsernameRegistrationResponseModel.class);

        String exceptedError = "Enter a valid username. This value may contain only letters, numbers, and @/./+/-/_ characters.";
        assertThat(wrongUsernameResponse.username().getFirst()).isEqualTo(exceptedError);
    }

    @Test
    public void emptyUsernameRegistrationTest() {
        EmptyUsernameRegistrationBodyModel emptyUsernameData = new EmptyUsernameRegistrationBodyModel(testData.randomPassword);

        WrongUsernameRegistrationResponseModel emptyUsernameResponse =  given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(emptyUsernameData)
                .basePath("api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("schemas/registration/wrong_username_registration_response_schema.json"))
                .extract()
                .as(WrongUsernameRegistrationResponseModel.class);

        String expectedError = "This field is required.";
        assertThat(emptyUsernameResponse.username().getFirst()).isEqualTo(expectedError);

    }

    @Test
    public void emptyPasswordRegistrationTest() {
        EmptyPasswordRegistrationBodyModel emptyPasswordData = new EmptyPasswordRegistrationBodyModel(testData.randomUsername);

        WrongPasswordRegistrationResponseModel emptyPasswordResponse =  given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(emptyPasswordData)
                .basePath("api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("schemas/registration/wrong_password_registration_response_schema.json"))
                .extract()
                .as(WrongPasswordRegistrationResponseModel.class);

        String expectedError = "This field is required.";
        assertThat(emptyPasswordResponse.password().getFirst()).isEqualTo(expectedError);

    }

    @Test
    public void emptyUsernameAndPasswordRegistrationTest() {
        EmptyCredentialsRegistrationBodyModel emptyUsernameAndPasswordData = new EmptyCredentialsRegistrationBodyModel();

        WrongUsernameAndPasswordRegistrationResponseModel emptyUsernameAndPasswordResponse =  given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(emptyUsernameAndPasswordData)
                .basePath("api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("schemas/registration/wrong_username_and_password_registration_response_schema.json"))
                .extract()
                .as(WrongUsernameAndPasswordRegistrationResponseModel.class);

        String expectedError = "This field is required.";
        assertThat(emptyUsernameAndPasswordResponse.username().getFirst()).isEqualTo(expectedError);
        assertThat(emptyUsernameAndPasswordResponse.password().getFirst()).isEqualTo(expectedError);

    }

}