package tests;

import io.restassured.http.ContentType;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.update_user.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginRequestSpec;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

        UpdateUserResponseModel putResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + loginResponse.access())
                .body(updateData)
                .basePath("/api/v1")
                .when()
                .put("/users/me/")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/update_user/update_data_response_schema.json"))
                .extract()
                .as(UpdateUserResponseModel.class);

        assertThat(testData.updateUsername).isEqualTo(putResponse.username());
        assertThat(testData.updateFirstName).isEqualTo(putResponse.firstName());
        assertThat(testData.updateLastName).isEqualTo(putResponse.lastName());
        assertThat(testData.updateEmail).isEqualTo(putResponse.email());
    }

    @Test
    public void unauthorizedUpdateFields() {
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
    public void redirectUpdateFields() {
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

    @ParameterizedTest(name = "PATCH {0}")
    @MethodSource("patchCases")
    void successfulUpdateFieldWithPatch(
            String caseName,
            UpdateUserPatchBodyModel body,
            Function<UpdateUserResponseModel, Object> extractor,
            Object expected
    ) {
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

        UpdateUserResponseModel patchResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + loginResponse.access())
                .body(body)
                .basePath("/api/v1")
                .when()
                .patch("/users/me/")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/update_user/update_data_response_schema.json"))
                .extract()
                .as(UpdateUserResponseModel.class);

        assertThat(extractor.apply(patchResponse)).isEqualTo(expected);
    }

    Stream<Arguments> patchCases() {
        return Stream.of(
                Arguments.of("username",
                        UpdateUserPatchBodyModel.withUsername(testData.updateUsername),
                        (Function<UpdateUserResponseModel, Object>) UpdateUserResponseModel::username,
                        testData.updateUsername),
                Arguments.of("firstName",
                        UpdateUserPatchBodyModel.withFirstName(testData.updateFirstName),
                        (Function<UpdateUserResponseModel, Object>) UpdateUserResponseModel::firstName,
                        testData.updateFirstName),
                Arguments.of("lastName",
                        UpdateUserPatchBodyModel.withLastName(testData.updateLastName),
                        (Function<UpdateUserResponseModel, Object>) UpdateUserResponseModel::lastName,
                        testData.updateLastName),
                Arguments.of("email",
                        UpdateUserPatchBodyModel.withEmail(testData.updateEmail),
                        (Function<UpdateUserResponseModel, Object>) UpdateUserResponseModel::email,
                        testData.updateEmail)
        );
    }
}
