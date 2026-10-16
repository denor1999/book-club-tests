package tests.ui;

import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.Owner;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;

public class RegistrationUiTests extends TestBase {

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @Test
    @Owner("denor1999")
    @DisplayName("[UI] Check successful registration response status")
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
    @DisplayName("[UI] Check registration attempt when user exists")
    public void existingUserFillingRegistrationFormTest() {
        step("Open registration page", () ->
                registrationPage.openPage());

        step("Fill registration form for existing user", () ->
                registrationPage.setUsername(testData.loginUsername)
                        .setPassword(testData.loginPassword)
                        .confirmPassword(testData.loginPassword));

        step("Send filled registration form", () ->
                registrationPage.submitForm());

        step("Check error message", () ->
                registrationPage.checkErrorMessage());
    }

    @Test
    @Owner("denor1999")
    @DisplayName("[UI] Check registration attempt with wrong username")
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
    @DisplayName("[UI] Check registration attempt with empty username")
    public void emptyUsernameFillingRegistrationFormTest() {
        step("Open registration page", () ->
                registrationPage.openPage());

        step("Fill registration form", () ->
                registrationPage.setPassword(testData.randomRegistrationPassword)
                        .confirmPassword(testData.randomRegistrationPassword)
                        .submitForm());

        step("Send filled registration form", () ->
                registrationPage.submitForm());

        step("Check error message", () ->
                registrationPage.checkErrorForEmptyUsername());
    }

    @Test
    @Owner("denor1999")
    @DisplayName("[UI] Check registration attempt with empty password")
    public void emptyPasswordFillingRegistrationFormTest() {
        step("Open registration page", () ->
                registrationPage.openPage());

        step("Fill registration form", () ->
                registrationPage.setUsername(testData.randomRegistrationUsername)
                        .submitForm());

        step("Send filled registration form", () ->
                registrationPage.submitForm());

        step("Check error message", () ->
                registrationPage.checkErrorForEmptyPassword());

    }

    @Test
    @Owner("denor1999")
    @DisplayName("[UI] Check registration attempt with empty username and password")
    public void emptyCredentialsFillingRegistrationTest() {
        step("Open registration page", () ->
                registrationPage.openPage());

        step("Click submit button", () ->
                registrationPage.submitForm());

        step("Check error message", () ->
                registrationPage.checkErrorForEmptyUsername());
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
    }
}
