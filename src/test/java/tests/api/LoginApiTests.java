package tests.api;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.login.EmptyCredentialsLoginResponseModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Feature("Login API tests")
@Tags({@Tag("api"), @Tag("login")})
public class LoginApiTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Check successful login response")
    @Description("The server returns 200. Access and refresh tokens are not blank, start with the expected prefix, and differ from each other.")
    public void successfulLoginTests() {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
            api.auth.login(loginData));


        step("Check refresh and access tokens", () -> {
            String actualAccessToken = loginResponse.access();
            String actualRefreshToken = loginResponse.refresh();
            assertThat(actualAccessToken).startsWith(config.auth.expectedTokenPath());
            assertThat(actualRefreshToken).startsWith(config.auth.expectedTokenPath());
            assertThat(actualAccessToken).isNotEqualTo(actualRefreshToken);
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Check authorization attempt with wrong password")
    @Description("The server returns 401, and the detail field contains the wrong-credentials error.")
    public void wrongCredentialsLoginTests() {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.wrongPassword());

        WrongCredentialsLoginResponseModel loginResponse = step("Send login request with wrong credentials and check status (401)", () ->
                api.auth.loginWithWrongCredentials(loginData));

        step("Check error message", () -> {
            String actualDetailError = loginResponse.detail();
            assertThat(actualDetailError).isEqualTo(config.auth.expectedDetailError());
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Check authorization attempt with empty username")
    @Description("The server returns 400, and the username field contains \"This field may not be blank.\"")
    public void emptyUsernameLoginTest () {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.emptyCredentials(), testData.randomRegistrationPassword);

        EmptyCredentialsLoginResponseModel loginResponse = step("Send login request with empty username and check status (400)", () ->
            api.auth.emptyUsernameLogin(loginData));

        step("Check error message", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(loginResponse.username().getFirst()).isEqualTo(expectedError);
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Check authorization attempt with empty password")
    @Description("The server returns 400, and the password field contains \"This field may not be blank.\"")
    public void emptyPasswordLoginTest () {
        LoginBodyModel loginData = new LoginBodyModel(testData.randomRegistrationUsername, config.auth.emptyCredentials());

        EmptyCredentialsLoginResponseModel loginResponse = step("Send login request with empty password and check status (400)", () ->
                api.auth.emptyPasswordLogin(loginData));

        step("Check error message", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(loginResponse.password().getFirst()).isEqualTo(expectedError);
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Check authorization attempt with empty username and password")
    @Description("The server returns 400, and both username and password contain \"This field may not be blank.\"")
    public void emptyCredentialsLoginTest () {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.emptyCredentials(), config.auth.emptyCredentials());

        EmptyCredentialsLoginResponseModel loginResponse = step("Send login request with empty username and password and check status (400)", () ->
                api.auth.emptyCredentialsLogin(loginData));

        step("Check error messages", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(loginResponse.username().getFirst()).isEqualTo(expectedError);
            assertThat(loginResponse.password().getFirst()).isEqualTo(expectedError);
        });
    }

}
