package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage {
    private final SelenideElement usernameInput = $("[data-testid=username-input]");
    private final SelenideElement passwordInput = $("[data-testid=password-input]");
    private final SelenideElement confirmPasswordInput = $("[data-testid=confirm-password-input]");
    private final SelenideElement submitButton = $("[data-testid=signup-button]");

    private final SelenideElement errorMessage = $("[data-testid=error-message]");
    private final ElementsCollection errorMessageEmptyUsername = $("[data-testid=username-input]")
            .$("..").$$("*");
    private final ElementsCollection errorMessageEmptyPassword = $("[data-testid=password-input]")
            .$("..").$$("*");

    @Step("Open registration page")
    public RegistrationPage openPage() {
        open("/signup");

        return this;
    }

    @Step("Set random username")
    public RegistrationPage setUsername(String username) {
        usernameInput.setValue(username);

        return this;
    }

    @Step("Set random password")
    public RegistrationPage setPassword(String password) {
        passwordInput.setValue(password);

        return this;
    }

    @Step("Confirm password")
    public RegistrationPage confirmPassword(String password) {
        confirmPasswordInput.setValue(password);

        return this;
    }

    @Step("Click on submit button")
    public void submitForm() {
        submitButton.click();
    }

    @Step("Check profile link in header after successful registration")
    public void checkProfileLink() {
        $("[data-testid=profile-link]").shouldBe(visible);
    }

    @Step("Check error message for wrong credentials")
    public void checkErrorMessage() {
        errorMessage.shouldBe(visible);
    }

    @Step("Check error message for empty username")
    public void checkErrorForEmptyUsername() {
        errorMessageEmptyUsername.shouldBe();
    }

    @Step("Check error message for empty password")
    public void checkErrorForEmptyPassword() {
        errorMessageEmptyPassword.shouldBe();
    }
}
