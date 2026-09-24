package tests;

import io.restassured.http.ContentType;
import models.login.*;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.*;

public class LoginTests extends TestBase{

    @Test
    public void successfulLoginTests() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);

        SuccessfulLoginResponseModel loginResponse = given()
                .spec(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginRequestSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);

        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();
        assertThat(actualAccess).startsWith(testData.expectedTokenPath);
        assertThat(actualRefresh).startsWith(testData.expectedTokenPath);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    public void wrongCredentialsLoginTests() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.wrongPassword);

        WrongCredentialsLoginResponseModel loginResponse = given()
                .spec(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginRequestSpec)
                .extract()
                .as(WrongCredentialsLoginResponseModel.class);

        String actualDetailError = loginResponse.detail();
        assertThat(actualDetailError).isEqualTo(testData.expectedDetailError);
    }

    @Test
    public void emptyUsernameLoginTest () {
        EmptyCredentialsLoginBodyModel loginData = new EmptyCredentialsLoginBodyModel(testData.emptyUsername, testData.randomPassword);

        EmptyCredentialsLoginResponseModel loginResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .basePath("/api/v1")
                .body(loginData)
                .post("/auth/token/")
                .then()
                .log().all()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("schemas/login/empty_username_registration_response_schema.json"))
                .extract()
                .as(EmptyCredentialsLoginResponseModel.class);

        String exceptedError = "This field may not be blank.";
        assertThat(loginResponse.username().getFirst()).isEqualTo(exceptedError);
    }

    @Test
    public void emptyPasswordLoginTest () {
        EmptyCredentialsLoginBodyModel loginData = new EmptyCredentialsLoginBodyModel(testData.randomUsername, testData.emptyPassword);

        EmptyCredentialsLoginResponseModel loginResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .basePath("/api/v1")
                .body(loginData)
                .post("/auth/token/")
                .then()
                .log().all()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("schemas/login/empty_password_registration_response_schema.json"))
                .extract()
                .as(EmptyCredentialsLoginResponseModel.class);

        String exceptedError = "This field may not be blank.";
        assertThat(loginResponse.password().getFirst()).isEqualTo(exceptedError);
    }

    @Test
    public void emptyCredentialsLoginTest () {
        EmptyCredentialsLoginBodyModel loginData = new EmptyCredentialsLoginBodyModel(testData.emptyUsername, testData.emptyPassword);

        EmptyCredentialsLoginResponseModel loginResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .basePath("/api/v1")
                .body(loginData)
                .post("/auth/token/")
                .then()
                .log().all()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("schemas/login/empty_credentials_registration_response_schema.json"))
                .extract()
                .as(EmptyCredentialsLoginResponseModel.class);

        String exceptedError = "This field may not be blank.";
        assertThat(loginResponse.username().getFirst()).isEqualTo(exceptedError);
        assertThat(loginResponse.password().getFirst()).isEqualTo(exceptedError);
    }
}
