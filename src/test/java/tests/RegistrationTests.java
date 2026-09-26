package tests;

import io.qameta.allure.Owner;
import models.registration.*;
import models.registration.model_examples.EmptyPasswordRegistrationBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.registration.RegistrationSpec.*;

public class RegistrationTests extends TestBase{

    @Test
    @Owner("denor1999")
    @DisplayName("Check successful registration response status")
    public void successfulRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomUsername, testData.randomPassword);

        RegistrationResponseSuccessfulModel registrationResponse = step("Send registration response and check status (200)", () ->
                given()
                        .spec(registrationRequestSpec)
                        .body(registrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(successfulRegistrationRequestSpec)
                        .extract()
                        .as(RegistrationResponseSuccessfulModel.class));

        step("Check response body fields", () -> {
            assertThat(testData.randomUsername).isEqualTo(registrationResponse.username());
            assertThat(registrationResponse.id()).isNotNull();
            assertThat(registrationResponse.firstName()).isBlank();
            assertThat(registrationResponse.lastName()).isBlank();
            assertThat(registrationResponse.email()).isBlank();
        });
    }


    @Test
    @Owner("denor1999")
    @DisplayName("Check registration attempt when user exists")
    public void existingUserRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomUsername, testData.randomPassword);

        RegistrationResponseSuccessfulModel firstRegistrationResponse = step("Send registration response and check status (200)", () ->
                given()
                        .spec(registrationRequestSpec)
                        .body(registrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(successfulRegistrationRequestSpec)
                        .extract()
                        .as(RegistrationResponseSuccessfulModel.class));

        step("Check username in response body", () ->
                assertThat(testData.randomUsername).isEqualTo(firstRegistrationResponse.username()));

        ExistingUserResponseModel secondRegistrationResponse = step("Send re-registration response and check status (400)", () ->
                given()
                        .spec(registrationRequestSpec)
                        .body(registrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(existingUserRegistrationRequestSpec)
                        .extract()
                        .as(ExistingUserResponseModel.class));

        step("Check error message", () -> {
            String actualError = secondRegistrationResponse.username().getFirst();
            assertThat(actualError).isEqualTo(testData.expectedExistingUserError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check registration attempt with wrong username")
    public void wrongUsernameRegistrationTest() {
        RegistrationBodyModel wrongUsernameRegistrationData = new RegistrationBodyModel(
                testData.wrongRegistrationUsername, testData.randomPassword);

        WrongUsernameRegistrationResponseModel wrongUsernameResponse = step("Send registration with wrong username response and check status (400)", () ->
                given()
                        .spec(registrationRequestSpec)
                        .body(wrongUsernameRegistrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(wrongUsernameRegistrationResponseSpec)
                        .extract()
                        .as(WrongUsernameRegistrationResponseModel.class));

        step("Check error message", () -> {
            String exceptedError = "Enter a valid username. This value may contain only letters, numbers, and @/./+/-/_ characters.";
            assertThat(wrongUsernameResponse.username().getFirst()).isEqualTo(exceptedError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check registration attempt with empty username")
    public void emptyUsernameRegistrationTest() {
        EmptyUsernameRegistrationBodyModel emptyUsernameData = new EmptyUsernameRegistrationBodyModel(testData.randomPassword);

        WrongUsernameRegistrationResponseModel emptyUsernameResponse = step("Send registration with empty username response and check status (400)", () ->
                given()
                        .spec(registrationRequestSpec)
                        .body(emptyUsernameData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(wrongUsernameRegistrationResponseSpec)
                        .extract()
                        .as(WrongUsernameRegistrationResponseModel.class));

        step("Check error message", () -> {
            String expectedError = "This field is required.";
            assertThat(emptyUsernameResponse.username().getFirst()).isEqualTo(expectedError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check registration attempt with empty password")
    public void emptyPasswordRegistrationTest() {
        EmptyPasswordRegistrationBodyModel emptyPasswordData = new EmptyPasswordRegistrationBodyModel(testData.randomUsername);

        WrongPasswordRegistrationResponseModel emptyPasswordResponse = step("Send registration with empty password response and check status (400)", () ->
                given()
                        .spec(registrationRequestSpec)
                        .body(emptyPasswordData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(wrongPasswordRegistrationResponseSpec)
                        .extract()
                        .as(WrongPasswordRegistrationResponseModel.class));

        step("Check error message", () -> {
            String expectedError = "This field is required.";
            assertThat(emptyPasswordResponse.password().getFirst()).isEqualTo(expectedError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check registration attempt with empty username and password")
    public void emptyCredentialsRegistrationTest() {
        EmptyCredentialsRegistrationBodyModel emptyUsernameAndPasswordData = new EmptyCredentialsRegistrationBodyModel();

        WrongCredentialsResponseModel emptyUsernameAndPasswordResponse = step("Send registration with empty username and password and check status (400)", () ->
                given()
                        .spec(registrationRequestSpec)
                        .body(emptyUsernameAndPasswordData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(wrongCredentialsRegistrationResponseSpec)
                        .extract()
                        .as(WrongCredentialsResponseModel.class));

        step("Check error messages", () -> {
            String expectedError = "This field is required.";
            assertThat(emptyUsernameAndPasswordResponse.username().getFirst()).isEqualTo(expectedError);
            assertThat(emptyUsernameAndPasswordResponse.password().getFirst()).isEqualTo(expectedError);
        });
    }

}