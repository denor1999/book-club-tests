package tests;

import io.qameta.allure.Owner;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.update_user.UnauthorizedUserUpdateWithPutResponseModel;
import models.update_user.UpdateUserPatchBodyModel;
import models.update_user.UpdateUserResponseModel;
import models.update_user.UpdateUserWithPutBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginRequestSpec;
import static specs.update.UpdateSpec.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateUserTest extends TestBase{

    @Test
    @Owner("denor1999")
    @DisplayName("Check successful put request status")
    public void successfulUpdateWithPutFields() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);

        SuccessfulLoginResponseModel loginResponse = step("Send login request", () ->
                given()
                        .spec(loginRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(successfulLoginRequestSpec)
                        .extract()
                        .as(SuccessfulLoginResponseModel.class));

        UpdateUserWithPutBodyModel updateData = new UpdateUserWithPutBodyModel(testData.updateUsername, testData.updateFirstName, testData.updateLastName, testData.updateEmail);

        UpdateUserResponseModel putResponse = step("Trying to change value of the fields", () ->
                given()
                        .spec(updateRequestSpec)
                        .header("Authorization", "Bearer " + loginResponse.access())
                        .body(updateData)
                        .when()
                        .put("/users/me/")
                        .then()
                        .spec(successfulUpdateSpec)
                        .extract()
                        .as(UpdateUserResponseModel.class));

        step("Check values of the fields", () -> {
            assertThat(testData.updateUsername).isEqualTo(putResponse.username());
            assertThat(testData.updateFirstName).isEqualTo(putResponse.firstName());
            assertThat(testData.updateLastName).isEqualTo(putResponse.lastName());
            assertThat(testData.updateEmail).isEqualTo(putResponse.email());
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check put request attempt when user unauthorized")
    public void unauthorizedUpdateFields() {
        UpdateUserWithPutBodyModel updateData = new UpdateUserWithPutBodyModel(testData.updateUsername, testData.updateFirstName, testData.updateLastName, testData.updateEmail);

        UnauthorizedUserUpdateWithPutResponseModel putResponse = step("Trying to change values of the fields without authorization", () ->
                given()
                    .spec(updateRequestSpec)
                    .body(updateData)
                    .when()
                    .put("/users/me/")
                    .then()
                    .spec(unauthorizedUpdateSpec)
                    .extract()
                    .as(UnauthorizedUserUpdateWithPutResponseModel.class));

        String expectedError = "Authentication credentials were not provided.";
        assertThat(putResponse.detail()).isEqualTo(expectedError);
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check request with URI without '/' ")
    public void redirectUpdateFields() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);

        SuccessfulLoginResponseModel loginResponse = step("Send login request", () ->
                given()
                    .spec(loginRequestSpec)
                    .body(loginData)
                    .when()
                    .post("/auth/token/")
                    .then()
                    .spec(successfulLoginRequestSpec)
                    .extract()
                    .as(SuccessfulLoginResponseModel.class));

        UpdateUserWithPutBodyModel updateData = new UpdateUserWithPutBodyModel(testData.updateUsername, testData.updateFirstName, testData.updateLastName, testData.updateEmail);

        step("Trying to change values of the fields using URI without '/' ", () ->
        given()
                .spec(updateRequestSpec)
                .header("Authorization", "Bearer " + loginResponse.access())
                .body(updateData)
                .basePath("/api/v1")
                .when()
                .put("/users/me")
                .then()
                .spec(redirectUpdateSpec));

    }

    @ParameterizedTest(name = "PATCH {0}")
    @MethodSource("patchCases")
    @Owner("denor1999")
    @DisplayName("Check successful patch request status")
    void successfulUpdateFieldWithPatch(
            String caseName,
            UpdateUserPatchBodyModel body,
            Function<UpdateUserResponseModel, Object> extractor,
            Object expected
    ) {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);

        SuccessfulLoginResponseModel loginResponse = step("Send login request", () ->
                given()
                    .spec(loginRequestSpec)
                    .body(loginData)
                    .when()
                    .post("/auth/token/")
                    .then()
                    .spec(successfulLoginRequestSpec)
                    .extract()
                    .as(SuccessfulLoginResponseModel.class));

        UpdateUserResponseModel patchResponse = step("Trying to change {0}", () ->
                given()
                    .spec(updateRequestSpec)
                    .header("Authorization", "Bearer " + loginResponse.access())
                    .body(body)
                    .when()
                    .patch("/users/me/")
                    .then()
                    .spec(successfulUpdateSpec)
                    .extract()
                    .as(UpdateUserResponseModel.class));

        step("Check {0} value of field", () ->
            assertThat(extractor.apply(patchResponse)).isEqualTo(expected));
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
