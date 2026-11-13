package tests.api;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
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

@Feature("Update user tests")
@Story("API")
@Tags({@Tag("api"), @Tag("user")})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateUserTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check successful put request status")
    @Description("An authorized PUT updates username, firstName, lastName, and email. The response values match the submitted ones.")
    public void successfulUpdateWithPutFields() {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

        SuccessfulLoginResponseModel loginResponse = step("Send login request", () ->
                api.auth.login(loginData));

        UpdateUserWithPutBodyModel updateData = new UpdateUserWithPutBodyModel(config.updateUser.username(), config.updateUser.firstName(), config.updateUser.lastName(), config.updateUser.email());

        UpdateUserResponseModel putResponse = step("Trying to change value of the fields", () ->
                api.users.updateUserDataWithPut(updateData, loginResponse.access()));

        step("Check values of the fields", () -> {
            assertThat(putResponse.username()).isEqualTo(config.updateUser.username());
            assertThat(putResponse.firstName()).isEqualTo(config.updateUser.firstName());
            assertThat(putResponse.lastName()).isEqualTo(config.updateUser.lastName());
            assertThat(putResponse.email()).isEqualTo(config.updateUser.email());
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check put request attempt when user unauthorized")
    @Description("Without authorization the server returns 401 and \"Authentication credentials were not provided.\"")
    public void unauthorizedUpdateFields() {
        UpdateUserWithPutBodyModel updateData = new UpdateUserWithPutBodyModel(config.updateUser.username(), config.updateUser.firstName(), config.updateUser.lastName(), config.updateUser.email());

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
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

        SuccessfulLoginResponseModel loginResponse = step("Send login request", () ->
                api.auth.login(loginData));

        UpdateUserWithPutBodyModel updateData = new UpdateUserWithPutBodyModel(config.updateUser.username(), config.updateUser.firstName(), config.updateUser.lastName(), config.updateUser.email());

        step("Trying to change values of the fields using URI without '/' ", () ->
                api.users.redirectUpdate(updateData, loginResponse.access()));

    }

    @ParameterizedTest(name = "PATCH {0}")
    @MethodSource("patchCases")
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check successful patch request status")
    @Description("An authorized PATCH updates one field (username, firstName, lastName, or email). The response value of that field matches the submitted one.")
    void successfulUpdateFieldWithPatchTest(
            String caseName,
            UpdateUserPatchBodyModel body,
            Function<UpdateUserResponseModel, Object> extractor,
            Object expected
    ) {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

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
                        UpdateUserPatchBodyModel.withUsername(config.updateUser.username()),
                        (Function<UpdateUserResponseModel, Object>) UpdateUserResponseModel::username,
                        config.updateUser.username()),
                Arguments.of("firstName",
                        UpdateUserPatchBodyModel.withFirstName(config.updateUser.firstName()),
                        (Function<UpdateUserResponseModel, Object>) UpdateUserResponseModel::firstName,
                        config.updateUser.firstName()),
                Arguments.of("lastName",
                        UpdateUserPatchBodyModel.withLastName(config.updateUser.lastName()),
                        (Function<UpdateUserResponseModel, Object>) UpdateUserResponseModel::lastName,
                        config.updateUser.lastName()),
                Arguments.of("email",
                        UpdateUserPatchBodyModel.withEmail(config.updateUser.email()),
                        (Function<UpdateUserResponseModel, Object>) UpdateUserResponseModel::email,
                        config.updateUser.email())
        );
    }
}
