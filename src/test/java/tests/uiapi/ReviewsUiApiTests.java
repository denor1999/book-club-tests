package tests.uiapi;

import io.qameta.allure.*;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Selenide.$$;
import static io.qameta.allure.Allure.step;

@Feature("Review tests")
@Story("UI and API")
@Tags({@Tag("ui"), @Tag("api"), @Tag("reviews")})
public class ReviewsUiApiTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API + UI] Successful send review for club")
    @Description("After submitting a review, it is displayed on the club page.")
    public void successfulSendReviewTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomRegistrationUsername,
                testData.randomRegistrationPassword);
        LoginBodyModel loginData = new LoginBodyModel(testData.randomRegistrationUsername,
                testData.randomRegistrationPassword);

        step("Send login request and check status (200)", () ->
                api.auth.loginByApi(api.users.register(registrationData), api.auth.login(loginData)));

        step("Join club and open club page", () ->
                homePage.joinClub("Of Mice and Men"));

        step("Open add review form", () ->
                clubPage.addReview());

        step("Fill review form", () ->
                clubPage.setAssessment(config.review.assessment())
                        .setReadPages(config.review.readPages())
                        .addReviewText(config.review.text()));

        step("Send review", () ->
                clubPage.sendReview());

        step("Check created review is displayed", () ->
                $$(".review-content").shouldHave(itemWithText(config.review.text())));
    }
}
