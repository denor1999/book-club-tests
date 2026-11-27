package tests.uiapi;

import io.qameta.allure.*;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

@Feature("Review tests")
@Story("UI and API")
@Tags({@Tag("ui"), @Tag("api"), @Tag("reviews")})
public class ReviewsUiApiTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @AllureId("55482")
    @DisplayName("[API + UI] Successful send review for club")
    @Description("After submitting a review, it is displayed on the club page.")
    public void successfulSendReviewTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomRegistrationUsername,
                testData.randomRegistrationPassword);
        LoginBodyModel loginData = new LoginBodyModel(testData.randomRegistrationUsername,
                testData.randomRegistrationPassword);

        api.auth.loginByApi(api.users.register(registrationData), api.auth.login(loginData));

        homePage.joinClub("Of Mice and Men");

        clubPage.addReview()
                .setAssessment(config.review.assessment())
                .setReadPages(config.review.readPages())
                .addReviewText(config.review.text())
                .sendReview();

        clubPage.checkReviewContent(config.review.text());
    }
}
