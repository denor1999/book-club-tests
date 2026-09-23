package tests;

import io.restassured.http.ContentType;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginTests extends TestBase{

    @Test
    public void successfulLoginTests() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);

        SuccessfulLoginResponseModel loginResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(loginData)
                .basePath("/api/v1")
                .when()
                .post("/auth/token/")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/login/successful_login_response_schema.json"))
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
                .log().all()
                .contentType(ContentType.JSON)
                .body(loginData)
                .basePath("/api/v1")
                .when()
                .post("/auth/token/")
                .then()
                .log().all()
                .statusCode(401)
                .body(matchesJsonSchemaInClasspath("schemas/login/wrong_credentials_login_response_schema.json"))
                .extract()
                .as(WrongCredentialsLoginResponseModel.class);

        String actualDetailError = loginResponse.detail();
        assertThat(actualDetailError).isEqualTo(testData.expectedDetailError);
    }

}
