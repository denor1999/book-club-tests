package api;

import models.login.LoginBodyModel;
import models.logout.LogoutBodyModel;

import static io.restassured.RestAssured.given;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginRequestSpec;
import static specs.logout.LogoutSpec.*;

    public class AuthApiClient {

    public void login(LoginBodyModel loginBody) {
        given()
                .spec(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginRequestSpec)
                .extract().asString();
    }

    public String loginAndGetRefreshToken(LoginBodyModel loginBody) {
        return given()
                .spec(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginRequestSpec)
                .extract().path("refresh");
    }

    public void logout(LogoutBodyModel logoutBody) {
        given()
                .spec(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutSpec)
                .extract().asString();
    }

    public void logoutWithoutAuthorization(LogoutBodyModel logoutBody) {
        given()
                .spec(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(wrongRefreshTokenSpec)
                .extract().asString();
    }
}
