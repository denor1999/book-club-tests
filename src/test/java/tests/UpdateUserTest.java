package tests;

import io.restassured.http.ContentType;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.update_user.UnauthorizedUserUpdateWithPutResponseModel;
import models.update_user.UpdateUserWithPutBodyModel;
import models.update_user.UpdateUserWithPutResponseModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginRequestSpec;

public class UpdateUserTest extends TestBase{

    @Test
    public void successfulUpdateWithPutFields() {
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

        UpdateUserWithPutBodyModel updateData = new UpdateUserWithPutBodyModel(testData.updateUsername, testData.updateFirstName, testData.updateLastName, testData.updateEmail);

        UpdateUserWithPutResponseModel putResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + loginResponse.access())
                .body(updateData)
                .basePath("/api/v1")
                .when()
                .put("/users/me/")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/update_user/update_data_with_put_response_schema.json"))
                .extract()
                .as(UpdateUserWithPutResponseModel.class);

        assertThat(testData.updateUsername).isEqualTo(putResponse.username());
        assertThat(testData.updateFirstName).isEqualTo(putResponse.firstName());
        assertThat(testData.updateLastName).isEqualTo(putResponse.lastName());
        assertThat(testData.updateEmail).isEqualTo(putResponse.email());
    }

    @Test
    public void unauthorizedUpdateWithPutFields() {
        UpdateUserWithPutBodyModel updateData = new UpdateUserWithPutBodyModel(testData.updateUsername, testData.updateFirstName, testData.updateLastName, testData.updateEmail);

        UnauthorizedUserUpdateWithPutResponseModel putResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(updateData)
                .basePath("/api/v1")
                .when()
                .put("/users/me/")
                .then()
                .statusCode(401)
                .body(matchesJsonSchemaInClasspath("schemas/update_user/unauthorized_user_update_with_put_response_schema.json"))
                .extract()
                .as(UnauthorizedUserUpdateWithPutResponseModel.class);

        String expectedError = "Authentication credentials were not provided.";
        assertThat(putResponse.detail()).isEqualTo(expectedError);
    }

    @Test
    public void redirectUpdateWithPutFields() {
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

        UpdateUserWithPutBodyModel updateData = new UpdateUserWithPutBodyModel(testData.updateUsername, testData.updateFirstName, testData.updateLastName, testData.updateEmail);

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + loginResponse.access())
                .body(updateData)
                .basePath("/api/v1")
                .when()
                .put("/users/me")
                .then()
                .statusCode(301);

    }
}
