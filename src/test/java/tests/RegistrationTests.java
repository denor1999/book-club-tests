package tests;

import io.qameta.allure.Owner;
import models.registration.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RegistrationTests extends TestBase{

    @Test
    @Owner("denor1999")
    @DisplayName("Check successful registration response status")
    public void successfulRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomUsername, testData.randomPassword);

        RegistrationResponseSuccessfulModel registrationResponse = step("Send registration response and check status (200)", () ->
                api.users.register(registrationData));

        step("Check response body fields", () -> {
            assertThat(testData.randomUsername).isEqualTo(registrationResponse.username());
            assertThat(registrationResponse.id()).isNotNull();
            assertThat(registrationResponse.firstName()).isBlank();
            assertThat(registrationResponse.lastName()).isBlank();
            assertThat(registrationResponse.email()).isBlank();
        });
    }


    @Test
    @Owner("denor1999")
    @DisplayName("Check registration attempt when user exists")
    public void existingUserRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomUsername, testData.randomPassword);

        RegistrationResponseSuccessfulModel registrationResponse = step("Send registration response and check status (200)", () ->
                api.users.register(registrationData));

        step("Check username in response body", () ->
                assertThat(testData.randomUsername).isEqualTo(registrationResponse.username()));

        ExistingUserResponseModel reRegistrationResponse = step("Send re-registration response and check status (400)", () ->
                api.users.existingUserRegister(registrationData));

        step("Check error message", () -> {
            String actualError = reRegistrationResponse.username().getFirst();
            assertThat(actualError).isEqualTo(testData.expectedExistingUserError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check registration attempt with wrong username")
    public void wrongUsernameRegistrationTest() {
        RegistrationBodyModel wrongUsernameRegistrationData = new RegistrationBodyModel(
                testData.wrongRegistrationUsername, testData.randomPassword);

        WrongUsernameRegistrationResponseModel registrationResponse = step("Send registration with wrong username response and check status (400)", () ->
                api.users.wrongUsernameRegister(wrongUsernameRegistrationData));

        step("Check error message", () -> {
            String exceptedError = "Enter a valid username. This value may contain only letters, numbers, and @/./+/-/_ characters.";
            assertThat(registrationResponse.username().getFirst()).isEqualTo(exceptedError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check registration attempt with empty username")
    public void emptyUsernameRegistrationTest() {
        RegistrationBodyModel emptyUsernameData = new RegistrationBodyModel(testData.emptyUsername, testData.randomPassword);

        WrongUsernameRegistrationResponseModel registrationResponse = step("Send registration with empty username response and check status (400)", () ->
                api.users.wrongUsernameRegister(emptyUsernameData));

        step("Check error message", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(registrationResponse.username().getFirst()).isEqualTo(expectedError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check registration attempt with empty password")
    public void emptyPasswordRegistrationTest() {
        RegistrationBodyModel emptyPasswordData = new RegistrationBodyModel(testData.randomUsername, testData.emptyPassword);

        WrongPasswordRegistrationResponseModel registrationResponse = step("Send registration with empty password response and check status (400)", () ->
                api.users.wrongPasswordRegister(emptyPasswordData));

        step("Check error message", () -> {
            String expectedError = "This field may not be blank.";
            assertThat(registrationResponse.password().getFirst()).isEqualTo(expectedError);
        });
    }

    @Test
    @Owner("denor1999")
    @DisplayName("Check registration attempt with empty username and password")
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