package api;

import io.qameta.allure.Step;
import models.login.EmptyCredentialsLoginResponseModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import models.logout.LogoutBodyModel;
import models.logout.WrongRefreshTokenLogoutResponseModel;

import static io.restassured.RestAssured.given;
import static specs.login.LoginSpec.*;
import static specs.logout.LogoutSpec.*;

public class AuthApiClient {

    @Step("Send login request and check status (200)")
    public SuccessfulLoginResponseModel login(LoginBodyModel loginBody) {
        return given()
                .spec(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);
    }

    @Step("Send login request and chack status (401)")
    public WrongCredentialsLoginResponseModel loginWithWrongCredentials(LoginBodyModel loginBody) {
        return given()
                .spec(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract()
                .as(WrongCredentialsLoginResponseModel.class);
    }

    @Step("Send login request and check status (400)")
    public EmptyCredentialsLoginResponseModel emptyUsernameLogin(LoginBodyModel loginBody) {
        return given()
                .spec(loginRequestSpec)
                .body(loginBody)
                .post("/auth/token/")
                .then()
                .spec(emptyUsernameLoginResponseSpec)
                .extract()
                .as(EmptyCredentialsLoginResponseModel.class);
    }

    @Step("Send login request and check status (400)")
    public EmptyCredentialsLoginResponseModel emptyPasswordLogin(LoginBodyModel loginBody) {
        return given()
                .spec(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyPasswordLoginResponseSpec)
                .extract()
                .as(EmptyCredentialsLoginResponseModel.class);
    }

    @Step("Send login request and check status (400)")
    public EmptyCredentialsLoginResponseModel emptyCredentialsLogin(LoginBodyModel loginBody) {
        return given()
                .spec(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyCredentialsLoginResponseSpec)
                .extract()
                .as(EmptyCredentialsLoginResponseModel.class);
    }

    @Step("Send login request and get refresh token and check status (200)")
    public String loginAndGetRefreshToken(LoginBodyModel loginBody) {
        return given()
                .spec(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().path("refresh");
    }

    @Step("Send logout request and check status (200)")
    public String logout(LogoutBodyModel logoutBody) {
        return given()
                .spec(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutSpec)
                .extract().asString();
    }

    @Step("Send logout request and check status (401)")
    public WrongRefreshTokenLogoutResponseModel logoutWithoutAuthorization(LogoutBodyModel logoutBody) {
        return given()
                .spec(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(wrongRefreshTokenSpec)
                .extract()
                .as(WrongRefreshTokenLogoutResponseModel.class);
    }
}
