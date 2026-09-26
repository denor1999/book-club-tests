package tests;

import api.AuthApiClient;
import io.qameta.allure.Owner;
import models.login.LoginBodyModel;
import models.login.WrongRefreshTokenLoginBodyModel;
import models.logout.LogoutBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;

public class LogoutTests extends TestBase {

    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    @Owner("denor1999")
    @DisplayName("Check successful logout response")
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);

        String refreshToken = step("Get refresh token with authorization", () ->
            authApiClient.loginAndGetRefreshToken(loginData));

        step("Send logout request with refresh token and check response status(200)", () -> {
            LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
            authApiClient.logout(logoutData);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check attempt logout without authorization")
    public void unauthorizedLogoutTest() {
        WrongRefreshTokenLoginBodyModel refreshTokenData = new WrongRefreshTokenLoginBodyModel(testData.wrongRefreshToken);

        step("Trying to get response without authorization", () -> {
            LogoutBodyModel logoutData = new LogoutBodyModel(refreshTokenData.refresh());
            authApiClient.logoutWithoutAuthorization(logoutData);
        });
    }
}
