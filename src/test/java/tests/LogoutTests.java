package tests;

import models.login.LoginBodyModel;
import models.login.WrongRefreshTokenLoginBodyModel;
import models.logout.WrongRefreshTokenLogoutResponseModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginRequestSpec;
import static specs.logout.LogoutSpec.*;

public class LogoutTests extends TestBase {

    @Test
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);

        String refreshToken = given()
                .spec(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginRequestSpec)
                .extract().path("refresh");

        String logoutData = format("{\"refresh\": \"%s\"}", refreshToken);

        String logoutResponse = given()
                .spec(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutSpec)
                .extract().asString();

        assertThat(logoutResponse).isEqualTo("{}");
    }

    @Test
    public void unauthorizedLogoutTest() {
        WrongRefreshTokenLoginBodyModel refreshTokenData = new WrongRefreshTokenLoginBodyModel(testData.wrongRefreshToken);

        WrongRefreshTokenLogoutResponseModel logoutResponse = given()
                .spec(logoutRequestSpec)
                .body(refreshTokenData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(wrongRefreshTokenSpec)
                .extract()
                .as(WrongRefreshTokenLogoutResponseModel.class);

        String expectedDetail = "Token is invalid";
        String expectedCode = "token_not_valid";
        assertThat(logoutResponse.detail()).isEqualTo(expectedDetail);
        assertThat(logoutResponse.code()).isEqualTo(expectedCode);
    }
}
