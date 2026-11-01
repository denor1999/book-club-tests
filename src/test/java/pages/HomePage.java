package pages;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class HomePage {

    private final ElementsCollection filterOption = $$(".filter-option");
    private final ElementsCollection clubCard = $$(".club-card");

    @Step("Select filter option {option}")
    public HomePage selectOption(String option) {
        filterOption.findBy(text(option)).click();

        return this;
    }

    @Step("Open club page {clubName}")
    public void openClubPage(String clubName) {
        clubCard.findBy(text(clubName)).
                $(".open-btn").click();
    }
}
