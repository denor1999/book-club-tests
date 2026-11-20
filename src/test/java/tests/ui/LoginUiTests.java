package tests.ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

@Feature("Login tests")
@Story("UI")
@Tags({@Tag("ui"), @Tag("login")})
public class LoginUiTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[UI] Check successful authorization attempt")
    @Description("After login with valid credentials, the profile link is visible in the header.")
    public void successfulLoginTests() {
        loginPage.openPage()
                .setUsername(config.auth.username())
                .setPassword(config.auth.password())
                .submitForm();

        loginPage.checkProfileLink();
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[UI] Check successful authorization attempt after registration")
    @Description("After registration and login with the new credentials, the profile link is visible in the header.")
    public void successfulLoginAfterRegistrationTests() {
        registrationPage.openPage()
                .setUsername(testData.randomRegistrationUsername)
                .setPassword(testData.randomRegistrationPassword)
                .confirmPassword(testData.randomRegistrationPassword)
                .submitForm();

        loginPage.openPage()
                .setUsername(testData.randomRegistrationUsername)
                .setPassword(testData.randomRegistrationPassword)
                .submitForm();

        loginPage.checkProfileLink();
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[UI] Check authorization attempt with wrong password")
    @Description("A wrong password shows the message \"Ты не пройдешь!\".")
    public void wrongCredentialsLoginTests() {
        loginPage.openPage()
                .setUsername(config.auth.username())
                .setPassword(config.auth.wrongPassword())
                .submitForm();

        loginPage.checkErrorMessage();
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[UI] Check authorization attempt with empty username")
    @Description("An empty username shows a validation error for the username field.")
    public void emptyUsernameLoginTest() {
        loginPage.openPage()
                .setPassword(config.auth.password())
                .submitForm();

        loginPage.checkErrorMessageForEmptyUsername();
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[UI] Check authorization attempt with empty password")
    @Description("An empty password shows a validation error for the password field.")
    public void emptyPasswordLoginTest() {
        loginPage.openPage()
                .setUsername(config.auth.username())
                .submitForm();

        loginPage.checkErrorMessageForEmptyPassword();
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[UI] Check authorization attempt with empty username and password")
    @Description("Empty username and password show a validation error for both fields.")
    public void emptyCredentialsLoginTest() {
        loginPage.openPage()
                .submitForm();

        loginPage.checkErrorMessageForEmptyCredentials();
    }
}
