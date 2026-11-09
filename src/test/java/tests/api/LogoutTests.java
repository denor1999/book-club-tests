package tests.api;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.login.LoginBodyModel;
import models.login.WrongRefreshTokenLoginBodyModel;
import models.logout.LogoutBodyModel;
import models.logout.WrongRefreshTokenLogoutResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Feature("Logout API tests")
@Tags({@Tag("api"), @Tag("logout")})
public class LogoutTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Check successful logout response")
    @Description("The server returns 200 and an empty response body {}.")
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

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
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Check logout attempt with invalid refresh token")
    @Description("An invalid refresh token is rejected: detail is \"Token is invalid\" and code is \"token_not_valid\".")
    public void invalidRefreshTokenLogoutTest() {
        WrongRefreshTokenLoginBodyModel refreshTokenData = new WrongRefreshTokenLoginBodyModel(testData.wrongRefreshToken);
        LogoutBodyModel logoutData = new LogoutBodyModel(refreshTokenData.refresh());

        WrongRefreshTokenLogoutResponseModel logoutResponse = step("Send logout request with invalid refresh token", () ->
            api.auth.logoutWithoutAuthorization(logoutData));

        step("Check error messages", () -> {
            String expectedDetail = "Token is invalid";
            String expectedCode = "token_not_valid";
            assertThat(logoutResponse.detail()).isEqualTo(expectedDetail);
            assertThat(logoutResponse.code()).isEqualTo(expectedCode);
        });
    }
}
