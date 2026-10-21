package tests.ui;

import helpers.Attach;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import tests.TestBase;

import static io.qameta.allure.Allure.step;

@Feature("Login form UI tests")
public class LoginUiTests extends TestBase {
    LoginPage loginPage = new LoginPage();

    @Test
    @Owner("denor1999")
    @DisplayName("[UI] Check successful authorization attempt")
    public void successfulLoginTests() {
        step("Open login page", () ->
                loginPage.openPage());

        step("Enter username and password", () -> {
            loginPage.setUsername(testData.loginUsername)
                    .setPassword(testData.loginPassword);
        });

        step("Click submit button", () ->
                loginPage.submitForm());

        step("Check successful registration", () ->
                loginPage.checkProfileLink());
    }

    @Test
    @Owner("denor1999")
    @DisplayName("[UI] Check successful authorization attempt after registration")
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

        step("Check successful registration", () ->
                loginPage.checkProfileLink());
    }

    @Test
    @Owner("denor1999")
    @DisplayName("[UI] Check authorization attempt with wrong password")
    public void wrongCredentialsLoginTests() {
        step("Open login page", () ->
                loginPage.openPage());

        step("Enter username and password", () -> {
            loginPage.setUsername(testData.randomRegistrationUsername)
                    .setPassword(testData.wrongLoginPassword);
        });

        step("Click submit button", () ->
                loginPage.submitForm());

        step("Check error message", () ->
                loginPage.checkErrorMessage());
    }

    @Test
    @Owner("denor1999")
    @DisplayName("[UI] Check authorization attempt with empty username")
    public void emptyUsernameLoginTest () {
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
    @DisplayName("[UI] Check authorization attempt with empty password")
    public void emptyPasswordLoginTest () {
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
    @DisplayName("[UI] Check authorization attempt with empty username and password")
    public void emptyCredentialsLoginTest () {
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
