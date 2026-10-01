package tests;

import io.qameta.allure.Owner;
import models.login.EmptyCredentialsLoginResponseModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginTests extends TestBase{

    @Test
    @Owner("denor1999")
    @DisplayName("Check successful login response")
    public void successfulLoginTests() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
            api.auth.login(loginData));


        step("Check refresh and access tokens", () -> {
            String actualAccessToken = loginResponse.access();
            String actualRefreshToken = loginResponse.refresh();
            assertThat(actualAccessToken).startsWith(testData.expectedTokenPath);
            assertThat(actualRefreshToken).startsWith(testData.expectedTokenPath);
            assertThat(actualAccessToken).isNotEqualTo(actualRefreshToken);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check authorization attempt with wrong password")
    public void wrongCredentialsLoginTests() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.wrongPassword);

        WrongCredentialsLoginResponseModel loginResponse = step("Send login request with wrong credentials and check status (401)", () ->
                api.auth.loginWithWrongCredentials(loginData));

        step("Check error message", () -> {
            String actualDetailError = loginResponse.detail();
            assertThat(actualDetailError).isEqualTo(testData.expectedDetailError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check authorization attempt with empty username")
    public void emptyUsernameLoginTest () {
        LoginBodyModel loginData = new LoginBodyModel(testData.emptyUsername, testData.randomPassword);

        EmptyCredentialsLoginResponseModel loginResponse = step("Send login request with empty username and check status (400)", () ->
            api.auth.emptyUsernameLogin(loginData));

        step("Check error message", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(loginResponse.username().getFirst()).isEqualTo(expectedError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check authorization attempt with empty password")
    public void emptyPasswordLoginTest () {
        LoginBodyModel loginData = new LoginBodyModel(testData.randomUsername, testData.emptyPassword);

        EmptyCredentialsLoginResponseModel loginResponse = step("Send login request with empty password and check status (400)", () ->
                api.auth.emptyPasswordLogin(loginData));

        step("Check error message", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(loginResponse.password().getFirst()).isEqualTo(expectedError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check authorization attempt with empty username and password")
    public void emptyCredentialsLoginTest () {
        LoginBodyModel loginData = new LoginBodyModel(testData.emptyUsername, testData.emptyPassword);

        EmptyCredentialsLoginResponseModel loginResponse = step("Send login request with empty username and password and check status (400)", () ->
                api.auth.emptyCredentialsLogin(loginData));

        step("Check error messages", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(loginResponse.username().getFirst()).isEqualTo(expectedError);
            assertThat(loginResponse.password().getFirst()).isEqualTo(expectedError);
        });
    }

}
