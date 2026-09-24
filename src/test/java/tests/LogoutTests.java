package tests;

import io.restassured.http.ContentType;
import models.login.LoginBodyModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginRequestSpec;

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
                .log().all()
                .contentType(ContentType.JSON)
                .body(logoutData)
                .basePath("/api/v1")
                .when()
                .post("/auth/logout/")
                .then()
                .log().all()
                .statusCode(200)
                .extract().asString();

        assertThat(logoutResponse).isEqualTo("{}");
    }

    //todo add more negative tests
}
