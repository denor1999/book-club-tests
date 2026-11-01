package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class HomePage {

    private final ElementsCollection filterOption = $$(".filter-option");
    private final ElementsCollection clubCard = $$(".club-card");
    private final SelenideElement clubsLink = $("[data-testid=clubs-link]");
    private final SelenideElement searchInput = $(".search-input");
    private final SelenideElement clubsList = $(".clubs-list");

    @Step("Select filter option {option}")
    public HomePage selectOption(String option) {
        filterOption.findBy(text(option)).click();

        return this;
    }

    @Step("Open clubs list")
    public HomePage openClubsList() {
        clubsLink.click();

        return this;
    }

    @Step("Search club {clubName}")
    public HomePage searchClub(String clubName) {
        searchInput.setValue(clubName).pressEnter();

        return this;
    }

    @Step("Open club page {clubName}")
    public void openClubPage(String clubName) {
        clubCard.findBy(text(clubName)).
                $(".open-btn").click();
    }

    @Step("Open club from list {clubName}")
    public void openClubFromList(String clubName) {
        clubsList.$(byText(clubName))
                .parent().parent().$(".open-btn").click();
    }
}
