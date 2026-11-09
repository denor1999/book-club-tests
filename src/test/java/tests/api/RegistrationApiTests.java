package tests.api;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.registration.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Feature("Registration form API tests")
@Tags({@Tag("api"), @Tag("registration")})
public class RegistrationApiTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Check successful registration response status")
    @Description("The server returns 201. Username matches the submitted value, id is present, and firstName, lastName, and email are blank.")
    public void successfulRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomRegistrationUsername, testData.randomRegistrationPassword);

        RegistrationResponseSuccessfulModel registrationResponse = step("Send registration response and check status (201)", () ->
                api.users.register(registrationData));

        step("Check response body fields", () -> {
            assertThat(registrationResponse.username()).isEqualTo(testData.randomRegistrationUsername);
            assertThat(registrationResponse.id()).isNotNull();
            assertThat(registrationResponse.firstName()).isBlank();
            assertThat(registrationResponse.lastName()).isBlank();
            assertThat(registrationResponse.email()).isBlank();
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Check registration attempt when user exists")
    @Description("Re-registration returns 400, and the username field contains the existing-user error.")
    public void existingUserRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomRegistrationUsername, testData.randomRegistrationPassword);

        RegistrationResponseSuccessfulModel registrationResponse = step("Send registration response and check status (201)", () ->
                api.users.register(registrationData));

        step("Check username in response body", () ->
                assertThat(registrationResponse.username()).isEqualTo(testData.randomRegistrationUsername));

        ExistingUserResponseModel reRegistrationResponse = step("Send re-registration response and check status (400)", () ->
                api.users.existingUserRegister(registrationData));

        step("Check error message", () -> {
            String actualError = reRegistrationResponse.username().getFirst();
            assertThat(actualError).isEqualTo(testData.expectedExistingUserError);
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Check registration attempt with wrong username")
    @Description("The server returns 400 and a message that username may contain only letters, numbers, and @/./+/-/_ characters.")
    public void wrongUsernameRegistrationTest() {
        RegistrationBodyModel wrongUsernameRegistrationData = new RegistrationBodyModel(
                testData.wrongRegistrationUsername, testData.randomRegistrationPassword);

        WrongUsernameRegistrationResponseModel registrationResponse = step("Send registration with wrong username response and check status (400)", () ->
                api.users.wrongUsernameRegister(wrongUsernameRegistrationData));

        step("Check error message", () -> {
            String exceptedError = "Enter a valid username. This value may contain only letters, numbers, and @/./+/-/_ characters.";
            assertThat(registrationResponse.username().getFirst()).isEqualTo(exceptedError);
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Check registration attempt with empty username")
    @Description("The server returns 400, and the username field contains \"This field may not be blank.\"")
    public void emptyUsernameRegistrationTest() {
        RegistrationBodyModel emptyUsernameData = new RegistrationBodyModel(testData.emptyUsername, testData.randomRegistrationPassword);

        WrongUsernameRegistrationResponseModel registrationResponse = step("Send registration with empty username response and check status (400)", () ->
                api.users.wrongUsernameRegister(emptyUsernameData));

        step("Check error message", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(registrationResponse.username().getFirst()).isEqualTo(expectedError);
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Check registration attempt with empty password")
    @Description("The server returns 400, and the password field contains \"This field may not be blank.\"")
    public void emptyPasswordRegistrationTest() {
        RegistrationBodyModel emptyPasswordData = new RegistrationBodyModel(testData.randomRegistrationUsername, testData.emptyPassword);

        WrongPasswordRegistrationResponseModel registrationResponse = step("Send registration with empty password response and check status (400)", () ->
                api.users.wrongPasswordRegister(emptyPasswordData));

        step("Check error message", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(registrationResponse.password().getFirst()).isEqualTo(expectedError);
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Check registration attempt with empty username and password")
    @Description("The server returns 400, and both username and password contain \"This field may not be blank.\"")
    public void emptyCredentialsRegistrationTest() {
        RegistrationBodyModel emptyUsernameAndPasswordData = new RegistrationBodyModel(testData.emptyUsername, testData.emptyPassword);

        WrongCredentialsResponseModel registrationResponse = step("Send registration with empty username and password and check status (400)", () ->
                api.users.wrongCredentialsRegister(emptyUsernameAndPasswordData));

        step("Check error messages", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(registrationResponse.username().getFirst()).isEqualTo(expectedError);
            assertThat(registrationResponse.password().getFirst()).isEqualTo(expectedError);
        });
    }
}