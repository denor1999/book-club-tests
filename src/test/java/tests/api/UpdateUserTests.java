package tests.api;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.update_user.UnauthorizedUserUpdateWithPutResponseModel;
import models.update_user.UpdateUserPatchBodyModel;
import models.update_user.UpdateUserResponseModel;
import models.update_user.UpdateUserWithPutBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tests.TestBase;

import java.util.function.Function;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Feature("Update user data API tests")
@Tags({@Tag("api"), @Tag("user")})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateUserTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check successful put request status")
    @Description("An authorized PUT updates username, firstName, lastName, and email. The response values match the submitted ones.")
    public void successfulUpdateWithPutFields() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request", () ->
                api.auth.login(loginData));

        UpdateUserWithPutBodyModel updateData = new UpdateUserWithPutBodyModel(testData.updateUsername, testData.updateFirstName, testData.updateLastName, testData.updateEmail);

        UpdateUserResponseModel putResponse = step("Trying to change value of the fields", () ->
                api.users.updateUserDataWithPut(updateData, loginResponse.access()));

        step("Check values of the fields", () -> {
            assertThat(testData.updateUsername).isEqualTo(putResponse.username());
            assertThat(testData.updateFirstName).isEqualTo(putResponse.firstName());
            assertThat(testData.updateLastName).isEqualTo(putResponse.lastName());
            assertThat(testData.updateEmail).isEqualTo(putResponse.email());
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check put request attempt when user unauthorized")
    @Description("Without authorization the server returns 401 and \"Authentication credentials were not provided.\"")
    public void unauthorizedUpdateFields() {
        UpdateUserWithPutBodyModel updateData = new UpdateUserWithPutBodyModel(testData.updateUsername, testData.updateFirstName, testData.updateLastName, testData.updateEmail);

        UnauthorizedUserUpdateWithPutResponseModel putResponse = step("Trying to change values of the fields without authorization", () ->
                api.users.unauthorizedUserUpdate(updateData));

        step("Check error message", () -> {
            String expectedError = "Authentication credentials were not provided.";
            assertThat(putResponse.detail()).isEqualTo(expectedError);
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Send PUT request with URI without '/' ")
    @Description("A PUT to a URI without a trailing slash succeeds for an authorized user.")
    public void redirectUpdateFields() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request", () ->
                api.auth.login(loginData));

        UpdateUserWithPutBodyModel updateData = new UpdateUserWithPutBodyModel(testData.updateUsername, testData.updateFirstName, testData.updateLastName, testData.updateEmail);

        step("Trying to change values of the fields using URI without '/' ", () ->
                api.users.redirectUpdate(updateData, loginResponse.access()));

    }

    @ParameterizedTest(name = "PATCH {0}")
    @MethodSource("patchCases")
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check successful patch request status")
    @Description("An authorized PATCH updates one field (username, firstName, lastName, or email). The response value of that field matches the submitted one.")
    void successfulUpdateFieldWithPatch(
            String caseName,
            UpdateUserPatchBodyModel body,
            Function<UpdateUserResponseModel, Object> extractor,
            Object expected
    ) {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request", () ->
                api.auth.login(loginData));

        UpdateUserResponseModel patchResponse = step("Trying to change {0}", () ->
                api.users.updateUserDataWithPatch(body, loginResponse.access()));

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
