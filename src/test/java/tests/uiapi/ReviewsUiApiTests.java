package tests.uiapi;

import helpers.Attach;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import org.junit.jupiter.api.*;
import tests.TestBase;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

@Feature("Review tests")
@Story("UI and API")
@Tags({@Tag("ui"), @Tag("api"), @Tag("reviews")})
public class ReviewsUiApiTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API + UI] Successful send review for club")
    @Description("After submitting a review, it is displayed on the club page with the text \"A very nice book!\".")
    @Flaky
    public void successfulSendReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

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
                homePage.selectOption(config.review.filter()));

        step("Open club page", () ->
                homePage.openClubPage(config.review.clubName()));

        step("Open add review form", () ->
                clubPage.addReview());

        step("Fill review form", () ->
                clubPage.setAssessment(config.review.assessment())
                        .setReadPages(config.review.readPages())
                        .addReviewText(config.review.text()));

        step("Send review", () ->
                clubPage.sendReview());

        step("Check created review is displayed", () ->
                $$(".review-content").shouldHave(texts(config.review.text())));
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
    }
}
