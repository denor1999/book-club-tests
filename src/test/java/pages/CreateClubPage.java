package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CreateClubPage {

    private final SelenideElement bookTitleInput = $("#bookTitle");
    private final SelenideElement bookAuthorsInput = $("#bookAuthors");
    private final SelenideElement publicationYearInput = $("#publicationYear");
    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement telegramChatLinkInput = $("#telegramChatLink");
    private final SelenideElement submitButton = $(".submit-btn");

    @Step("Open page https://book-club.qa.guru/clubs/create")
    public CreateClubPage openPage() {
        open("/clubs/create");

        return this;
    }

    @Step("Set book title {bookTitle}")
    public CreateClubPage setBookTitle(String bookTitle) {
        bookTitleInput.setValue(bookTitle);

        return this;
    }

    @Step("Set book authors {bookAuthors}")
    public CreateClubPage setBookAuthors(String bookAuthors) {
        bookAuthorsInput.setValue(bookAuthors);

        return this;
    }

    @Step("Set publication year {publicationYear}")
    public CreateClubPage setPublicationYear(int publicationYear) {
        publicationYearInput.setValue(String.valueOf(publicationYear));

        return this;
    }

    @Step("Set description {description}")
    public CreateClubPage setDescription(String description) {
        descriptionInput.setValue(description);

        return this;
    }

    @Step("Set telegram chat link {telegramChatLink}")
    public CreateClubPage setTelegramChatLink(String telegramChatLink) {
        telegramChatLinkInput.setValue(telegramChatLink);

        return this;
    }

    @Step("Click submit button")
    public void submitForm() {
        submitButton.scrollTo().click();
    }
}
