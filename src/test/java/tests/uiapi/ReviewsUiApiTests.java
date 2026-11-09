package tests.uiapi;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

@Feature("Reviews UI and API tests")
@Tags({@Tag("ui"), @Tag("api"), @Tag("reviews")})
public class ReviewsUiApiTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API + UI] Successful send review for club")
    @Description("After submitting a review, it is displayed on the club page with the text \"A very nice book!\".")
    @Flaky
    public void successfulSendReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        String localStorage = """
                {
                  "accessToken": "%s",
                  "refreshToken": "%s",
                  "isAuthenticated": true
                }
                """.formatted(
                loginResponse.access(),
                loginResponse.refresh()
        );

        step("Authorize in UI via local storage", () -> {
            open("/favicon.ico");
            localStorage().setItem("book_club_auth", localStorage);
            open("/");
        });

        step("Select 'Мои клубы' filter", () ->
                homePage.selectOption("Мои клубы"));

        step("Open club page", () ->
                homePage.openClubPage("Chief Metrics Coordinator"));

        step("Open add review form", () ->
                clubPage.addReview());

        step("Fill review form", () ->
                clubPage.setAssessment("5")
                        .setReadPages("25")
                        .addReviewText(testData.reviewText));

        step("Send review", () ->
                clubPage.sendReview());

        step("Check created review is displayed", () ->
                $$(".review-content").shouldHave(texts(testData.reviewText)));
    }
}
