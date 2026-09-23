package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
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

}
