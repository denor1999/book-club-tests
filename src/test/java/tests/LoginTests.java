package tests;

import api.AuthApiClient;
import io.qameta.allure.Owner;
import models.login.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.*;

public class LoginTests extends TestBase{

    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    @Owner("denor1999")
    @DisplayName("Check successful login response")
    public void successfulLoginTests() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);

        step("Send login request and check status (200)", () ->
            authApiClient.login(loginData));
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check authorization attempt with wrong password")
    public void wrongCredentialsLoginTests() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.wrongPassword);

        WrongCredentialsLoginResponseModel loginResponse = step("Send login request with wrong credentials and check status (401)", () ->
                given()
                        .spec(loginRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(wrongCredentialsLoginRequestSpec)
                        .extract()
                        .as(WrongCredentialsLoginResponseModel.class));

        step("Check error message", () -> {
            String actualDetailError = loginResponse.detail();
            assertThat(actualDetailError).isEqualTo(testData.expectedDetailError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check authorization attempt with empty username")
    public void emptyUsernameLoginTest () {
        EmptyCredentialsLoginBodyModel loginData = new EmptyCredentialsLoginBodyModel(testData.emptyUsername, testData.randomPassword);

        EmptyCredentialsLoginResponseModel loginResponse = step("Send login request with empty username and check status (400)", () ->
                given()
                        .spec(loginRequestSpec)
                        .body(loginData)
                        .post("/auth/token/")
                        .then()
                        .spec(emptyUsernameLoginRequestSpec)
                        .extract()
                        .as(EmptyCredentialsLoginResponseModel.class));

        step("Check error message", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(loginResponse.username().getFirst()).isEqualTo(expectedError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check authorization attempt with empty password")
    public void emptyPasswordLoginTest () {
        EmptyCredentialsLoginBodyModel loginData = new EmptyCredentialsLoginBodyModel(testData.randomUsername, testData.emptyPassword);

        EmptyCredentialsLoginResponseModel loginResponse = step("Send login request with empty password and check status (400)", () ->
                given()
                        .spec(loginRequestSpec)
                        .body(loginData)
                        .post("/auth/token/")
                        .then()
                        .spec(emptyPasswordLoginRequestSpec)
                        .extract()
                        .as(EmptyCredentialsLoginResponseModel.class));

        step("Check error message", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(loginResponse.password().getFirst()).isEqualTo(expectedError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check authorization attempt with empty username and password")
    public void emptyCredentialsLoginTest () {
        EmptyCredentialsLoginBodyModel loginData = new EmptyCredentialsLoginBodyModel(testData.emptyUsername, testData.emptyPassword);

        EmptyCredentialsLoginResponseModel loginResponse = step("Send login request with empty username and password and check status (400)", () ->
                given()
                        .spec(loginRequestSpec)
                        .body(loginData)
                        .post("/auth/token/")
                        .then()
                        .spec(emptyCredentialsLoginRequestSpec)
                        .extract()
                        .as(EmptyCredentialsLoginResponseModel.class));

        step("Check error messages", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(loginResponse.username().getFirst()).isEqualTo(expectedError);
            assertThat(loginResponse.password().getFirst()).isEqualTo(expectedError);
        });
    }

}
