package tests.ui;

import helpers.Attach;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;

@Feature("Registration form UI tests")
@Tags({@Tag("ui"), @Tag("registration")})
public class RegistrationUiTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[UI] Check successful registration attempt")
    @Description("After successful registration with valid data, the profile link is visible in the header.")
    public void successfulFillingRegistrationFormTest() {
        step("Open registration page", () ->
                registrationPage.openPage());

        step("Filling registration form", () ->
                registrationPage.setUsername(testData.randomRegistrationUsername)
                        .setPassword(testData.randomRegistrationPassword)
                        .confirmPassword(testData.randomRegistrationPassword));

        step("Send filled registration form", () ->
                registrationPage.submitForm());

        step("Check successful registration", () ->
                registrationPage.checkProfileLink());
    }
    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[UI] Check registration attempt when user exists")
    @Description("Registering an existing user shows an error message.")
    public void existingUserFillingRegistrationFormTest() {
        step("Open registration page", () ->
                registrationPage.openPage());

        step("Fill registration form for existing user", () ->
                registrationPage.setUsername(config.auth.username())
                        .setPassword(config.auth.password())
                        .confirmPassword(config.auth.password()));

        step("Send filled registration form", () ->
                registrationPage.submitForm());

        step("Check error message", () ->
                registrationPage.checkErrorMessage());
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[UI] Check registration attempt with wrong username")
    @Description("Registering with an invalid username shows an error message.")
    public void wrongUsernameFillingRegistrationFormTest() {
        step("Open registration page", () ->
                registrationPage.openPage());

        step("Fill registration form", () ->
                registrationPage.setUsername(testData.wrongRegistrationUsername)
                        .setPassword(testData.randomRegistrationPassword)
                        .confirmPassword(testData.randomRegistrationPassword));

        step("Send filled registration form", () ->
                registrationPage.submitForm());

        step("Check error message", () ->
                registrationPage.checkErrorMessage());
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[UI] Check registration attempt with empty username")
    @Description("An empty username shows a validation error for the username field.")
    public void emptyUsernameFillingRegistrationFormTest() {
        step("Open registration page", () ->
                registrationPage.openPage());

        step("Fill registration form", () ->
                registrationPage.setPassword(testData.randomRegistrationPassword)
                        .confirmPassword(testData.randomRegistrationPassword));

        step("Send filled registration form", () ->
                registrationPage.submitForm());

        step("Check error message", () ->
                registrationPage.checkErrorForEmptyUsername());
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[UI] Check registration attempt with empty password")
    @Description("An empty password shows a validation error for the password field.")
    public void emptyPasswordFillingRegistrationFormTest() {
        step("Open registration page", () ->
                registrationPage.openPage());

        step("Fill registration form", () ->
                registrationPage.setUsername(testData.randomRegistrationUsername));

        step("Send filled registration form", () ->
                registrationPage.submitForm());

        step("Check error message", () ->
                registrationPage.checkErrorForEmptyPassword());

    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[UI] Check registration attempt with empty username and password")
    @Description("Empty username and password show a validation error for both fields.")
    public void emptyCredentialsFillingRegistrationTest() {
        step("Open registration page", () ->
                registrationPage.openPage());

        step("Click submit button", () ->
                registrationPage.submitForm());

        step("Check error messages", () -> {
            registrationPage.checkErrorForEmptyUsername();
        });
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
    }
}
