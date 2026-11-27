package tests.ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

@Feature("Registration tests")
@Story("UI")
@Tags({@Tag("ui"), @Tag("registration")})
public class RegistrationUiTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @AllureId("54154")
    @DisplayName("[UI] Check successful registration attempt")
    @Description("After successful registration with valid data, the profile link is visible in the header.")
    public void successfulFillingRegistrationFormTest() {
        registrationPage.openPage()
                .setUsername(testData.randomRegistrationUsername)
                .setPassword(testData.randomRegistrationPassword)
                .confirmPassword(testData.randomRegistrationPassword)
                .submitForm();

        registrationPage.checkProfileLink();
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @AllureId("54155")
    @DisplayName("[UI] Check registration attempt when user exists")
    @Description("Registering an existing user shows an error message.")
    public void existingUserFillingRegistrationFormTest() {
        registrationPage.openPage()
                .setUsername(config.auth.username())
                .setPassword(config.auth.password())
                .confirmPassword(config.auth.password())
                .submitForm();

        registrationPage.checkErrorMessage();
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @AllureId("54153")
    @DisplayName("[UI] Check registration attempt with wrong username")
    @Description("Registering with an invalid username shows an error message.")
    public void wrongUsernameFillingRegistrationFormTest() {
        registrationPage.openPage()
                .setUsername(testData.wrongRegistrationUsername)
                .setPassword(testData.randomRegistrationPassword)
                .confirmPassword(testData.randomRegistrationPassword)
                .submitForm();

        registrationPage.checkErrorMessage();
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @AllureId("54152")
    @DisplayName("[UI] Check registration attempt with empty username")
    @Description("An empty username shows a validation error for the username field.")
    public void emptyUsernameFillingRegistrationFormTest() {
        registrationPage.openPage()
                .setPassword(testData.randomRegistrationPassword)
                .confirmPassword(testData.randomRegistrationPassword)
                .submitForm();

        registrationPage.checkErrorForEmptyUsername();
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @AllureId("54151")
    @DisplayName("[UI] Check registration attempt with empty password")
    @Description("An empty password shows a validation error for the password field.")
    public void emptyPasswordFillingRegistrationFormTest() {
        registrationPage.openPage()
                .setUsername(testData.randomRegistrationUsername)
                .submitForm();

        registrationPage.checkErrorForEmptyPassword();
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @AllureId("54156")
    @DisplayName("[UI] Check registration attempt with empty username and password")
    @Description("Empty username and password show a validation error for both fields.")
    public void emptyCredentialsFillingRegistrationTest() {
        registrationPage.openPage()
                .submitForm();

        registrationPage.checkErrorForEmptyUsername();
    }
}
