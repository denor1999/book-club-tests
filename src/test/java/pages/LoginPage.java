package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {
    private final SelenideElement usernameInput = $("[data-testid=username-input]");
    private final SelenideElement passwordInput = $("[data-testid=password-input]");
    private final SelenideElement submitButton = $("[data-testid=submit-button]");
    private final SelenideElement profileLink = $("[data-testid=profile-link]");
    private final SelenideElement errorMessage = $("[data-testid=error-message]");
    private final ElementsCollection emptyUsernameInputMessage = $("[data-testid=username-input]")
            .$("..").$$("*");
    private final ElementsCollection emptyPasswordInputMessage = $("[data-testid=password-input]")
            .$("..").$$("*");

    @Step("Open page https://book-club.qa.guru/signin")
    public LoginPage openPage() {
        open("/signin");

        return this;
    }

    @Step("Set username {username}")
    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);

        return this;
    }

    @Step("Set password {password}")
    public LoginPage setPassword(String username) {
        passwordInput.setValue(username);

        return this;
    }

    @Step("Click submit button")
    public void submitForm() {
        submitButton.click();
    }

    @Step("Check profile link in header")
    public void checkProfileLink() {
        profileLink.shouldBe(visible);
    }

    @Step("Check error message text")
    public void checkErrorMessage() {
        errorMessage.shouldHave(text("Ты не пройдешь!"));
    }

    @Step("Check error message text for empty username")
    public void checkErrorMessageForEmptyUsername() {
        emptyUsernameInputMessage.shouldBe();
    }

    @Step("Check error message text for empty credentials")
    public void checkErrorMessageForEmptyCredentials() {
        emptyUsernameInputMessage.shouldBe();
    }

    @Step("Check error message text for empty password")
    public void checkErrorMessageForEmptyPassword() {
        emptyPasswordInputMessage.shouldBe();
    }
}
