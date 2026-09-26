package tests;

import io.qameta.allure.Owner;
import models.login.LoginBodyModel;
import models.login.WrongRefreshTokenLoginBodyModel;
import models.logout.WrongRefreshTokenLogoutResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.successfulLoginRequestSpec;
import static specs.logout.LogoutSpec.*;

public class LogoutTests extends TestBase {

    @Test
    @Owner("denor1999")
    @DisplayName("Check successful logout response")
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);

        String refreshToken = step("Get refresh token with authorization", () ->
            given()
                    .spec(logoutRequestSpec)
                    .body(loginData)
                    .when()
                    .post("/auth/token/")
                    .then()
                    .spec(successfulLoginRequestSpec)
                    .extract().path("refresh"));

        String logoutData = format("{\"refresh\": \"%s\"}", refreshToken);

        String logoutResponse = step("Send request logout with refresh token and check response status(200)", () ->
                given()
                        .spec(logoutRequestSpec)
                        .body(logoutData)
                        .when()
                        .post("/auth/logout/")
                        .then()
                        .spec(successfulLogoutSpec)
                        .extract().asString());

        step("Check logout response", () ->
            assertThat(logoutResponse).isEqualTo("{}"));
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check attempt logout without authorization")
    public void unauthorizedLogoutTest() {
        WrongRefreshTokenLoginBodyModel refreshTokenData = new WrongRefreshTokenLoginBodyModel(testData.wrongRefreshToken);

        WrongRefreshTokenLogoutResponseModel logoutResponse = step("Trying to get response without authorization", () ->
                given()
                        .spec(logoutRequestSpec)
                        .body(refreshTokenData)
                        .when()
                        .post("/auth/logout/")
                        .then()
                        .spec(wrongRefreshTokenSpec)
                        .extract()
                        .as(WrongRefreshTokenLogoutResponseModel.class));

        step("Check error messages", () -> {
            String expectedDetail = "Token is invalid";
            String expectedCode = "token_not_valid";
            assertThat(logoutResponse.detail()).isEqualTo(expectedDetail);
            assertThat(logoutResponse.code()).isEqualTo(expectedCode);
        });
    }
}
