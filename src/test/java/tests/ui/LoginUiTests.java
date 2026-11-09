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

@Feature("Login form UI tests")
@Tags({@Tag("ui"), @Tag("login")})
public class LoginUiTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[UI] Check successful authorization attempt")
    @Description("After login with valid credentials, the profile link is visible in the header.")
    public void successfulLoginTests() {
        step("Open login page", () ->
                loginPage.openPage());

        step("Enter username and password", () -> {
            loginPage.setUsername(testData.loginUsername)
                    .setPassword(testData.loginPassword);
        });

        step("Click submit button", () ->
                loginPage.submitForm());

        step("Check profile link in header", () ->
                loginPage.checkProfileLink());
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[UI] Check successful authorization attempt after registration")
    @Description("After registration and login with the new credentials, the profile link is visible in the header.")
    public void successfulLoginAfterRegistrationTests() {
        step("Open registration page", () ->
                registrationPage.openPage());

        step("Filling registration form", () ->
                registrationPage.setUsername(testData.randomRegistrationUsername)
                        .setPassword(testData.randomRegistrationPassword)
                        .confirmPassword(testData.randomRegistrationPassword));

        step("Send filled registration form", () ->
                registrationPage.submitForm());

        step("Open login page", () ->
                loginPage.openPage());

        step("Enter username and password", () -> {
            loginPage.setUsername(testData.randomRegistrationUsername)
                    .setPassword(testData.randomRegistrationPassword);
        });

        step("Click submit button", () ->
                loginPage.submitForm());

        step("Check profile link in header", () ->
                loginPage.checkProfileLink());
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[UI] Check authorization attempt with wrong password")
    @Description("A wrong password shows the message \"Ты не пройдешь!\".")
    public void wrongCredentialsLoginTests() {
        step("Open login page", () ->
                loginPage.openPage());

        step("Enter username and password", () -> {
            loginPage.setUsername(testData.loginUsername)
                    .setPassword(testData.wrongLoginPassword);
        });

        step("Click submit button", () ->
                loginPage.submitForm());

        step("Check error message", () ->
                loginPage.checkErrorMessage());
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[UI] Check authorization attempt with empty username")
    @Description("An empty username shows a validation error for the username field.")
    public void emptyUsernameLoginTest() {
        step("Open login page", () ->
                loginPage.openPage());

        step("Enter username and password", () -> {
            loginPage.setPassword(testData.loginPassword);
        });

        step("Click submit button", () ->
                loginPage.submitForm());

        step("Check error message", () ->
                loginPage.checkErrorMessageForEmptyUsername());
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[UI] Check authorization attempt with empty password")
    @Description("An empty password shows a validation error for the password field.")
    public void emptyPasswordLoginTest() {
        step("Open login page", () ->
                loginPage.openPage());

        step("Enter username and password", () -> {
            loginPage.setUsername(testData.loginUsername);
        });

        step("Submit form", () ->
                loginPage.submitForm());

        step("Check error message", () ->
                loginPage.checkErrorMessageForEmptyPassword());
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[UI] Check authorization attempt with empty username and password")
    @Description("Empty username and password show a validation error for both fields.")
    public void emptyCredentialsLoginTest() {
        step("Open login page", () ->
                loginPage.openPage());

        step("Click submit button", () ->
                loginPage.submitForm());

        step("Check error message", () ->
                loginPage.checkErrorMessageForEmptyCredentials());
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
    }
}
