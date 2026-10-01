package tests;

import api.AuthApiClient;
import io.qameta.allure.Owner;
import models.login.LoginBodyModel;
import models.login.WrongRefreshTokenLoginBodyModel;
import models.logout.LogoutBodyModel;
import models.logout.WrongRefreshTokenLogoutResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class LogoutTests extends TestBase {

    @Test
    @Owner("denor1999")
    @DisplayName("Check successful logout response")
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);

        String refreshToken = step("Get refresh token with authorization", () ->
            api.auth.loginAndGetRefreshToken(loginData));

        LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);

        String logoutResponse = step("Send logout request with refresh token and check response status(200)", () ->
            api.auth.logout(logoutData));

        step("Check logout response", () ->
            assertThat(logoutResponse).isEqualTo("{}"));
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check attempt logout without authorization")
    public void unauthorizedLogoutTest() {
        WrongRefreshTokenLoginBodyModel refreshTokenData = new WrongRefreshTokenLoginBodyModel(testData.wrongRefreshToken);
        LogoutBodyModel logoutData = new LogoutBodyModel(refreshTokenData.refresh());

        WrongRefreshTokenLogoutResponseModel logoutResponse = step("Trying to get response without authorization", () ->
            api.auth.logoutWithoutAuthorization(logoutData));

        step("Check error messages", () -> {
            String expectedDetail = "Token is invalid";
            String expectedCode = "token_not_valid";
            assertThat(logoutResponse.detail()).isEqualTo(expectedDetail);
            assertThat(logoutResponse.code()).isEqualTo(expectedCode);
        });
    }
}
