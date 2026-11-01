package tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.reviews.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("Reviews API tests")
public class ReviewsApiTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] get club reviews")
    public void getReviewsTest() {
        ReviewsGetResponseModel response = step("Send get reviews request and check status (200)", () ->
                api.review.getReviews());

        step("Check reviews count matches results size", () ->
                assertThat(response.results()).hasSize(response.count()));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Successful send review")
    public void successfulPostReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(testData.randomBookClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        ReviewResponseModel response = step("Send review and check status (201)", () ->
                api.review.successfulPostReview(requestData, loginResponse.access()));

        step("Check review id is present", () ->
                assertThat(response.id()).isNotNull());
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Unauthorized user attempt for send review")
    public void unauthorizedPostReviewTest() {
        ReviewRequestModel requestData = new ReviewRequestModel(testData.randomBookClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        UnauthorizedReviewModel response = step("Send review without authorization and check status (401)", () ->
                api.review.unauthorizedPostReview(requestData));

        step("Check error message", () ->
                assertThat(response.detail()).isEqualTo("Authentication credentials were not provided."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Attempt for send review with wrong access token")
    public void wrongTokenPostReviewTest() {
        ReviewRequestModel requestData = new ReviewRequestModel(testData.randomBookClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        WrongTokenPostReviewModel response = step("Send review with wrong access token and check status (401)", () ->
                api.review.wrongAccessTokenPostReview(requestData, testData.wrongAccessToken));

        step("Check error message and code", () -> {
            assertThat(response.detail()).isEqualTo("Given token not valid for any token type");
            assertThat(response.code()).isEqualTo("token_not_valid");
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Successful edit club review with PUT")
    public void successfulPutReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.randomAssessment, testData.readPages);

        ReviewResponseModel response = step("Edit review with PUT and check status (200)", () ->
                api.review.successfulPutReview(requestData, loginResponse.access(), 1883));

        step("Check modified field is present", () ->
                assertThat(response.modified()).isNotNull());
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Successful edit club review with PATCH")
    public void successfulPatchReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.randomAssessment, testData.readPages);

        ReviewResponseModel response = step("Edit review with PATCH and check status (200)", () ->
                api.review.successfulPatchReview(requestData, loginResponse.access(), 1883));

        step("Check modified field is present", () ->
                assertThat(response.modified()).isNotNull());
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Attempt for edit club review with wrong assessment with PUT")
    public void wrongAssessmentValuePutReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.wrongRandomAssessment, testData.readPages);

        WrongAssessmentReviewResponseModel response = step("Edit review with PUT and wrong assessment and check status (400)", () ->
                api.review.wrongAssessmentPutReview(requestData, loginResponse.access(), 1883));

        step("Check error message", () ->
                assertThat(response.assessment().getFirst()).isEqualTo("Ensure this value is less than or equal to 5."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Attempt for edit club review with wrong assessment with PATCH")
    public void wrongAssessmentValuePatchReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.wrongRandomAssessment, testData.readPages);

        WrongAssessmentReviewResponseModel response = step("Edit review with PATCH and wrong assessment and check status (400)", () ->
                api.review.wrongAssessmentPatchReview(requestData, loginResponse.access(), 1883));

        step("Check error message", () ->
                assertThat(response.assessment().getFirst()).isEqualTo("Ensure this value is less than or equal to 5."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Unauthorized user attempt for edit club review with PUT")
    public void unauthorizedPutReviewTest() {
        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.randomAssessment, testData.readPages);

        UnauthorizedReviewModel response = step("Edit review with PUT without authorization and check status (401)", () ->
                api.review.unauthorizedPutReview(requestData, 1883));

        step("Check error message", () ->
                assertThat(response.detail()).isEqualTo("Authentication credentials were not provided."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Unauthorized user attempt for edit club review with PATCH")
    public void unauthorizedPatchReviewTest() {
        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.randomAssessment, testData.readPages);

        UnauthorizedReviewModel response = step("Edit review with PATCH without authorization and check status (401)", () ->
                api.review.unauthorizedPatchReview(requestData, 1883));

        step("Check error message", () ->
                assertThat(response.detail()).isEqualTo("Authentication credentials were not provided."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Attempt for change other club reviews with PUT")
    public void someoneElsePutReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.wrongRandomAssessment, testData.readPages);

        SomeoneElsePutReviewResponseModel response = step("Edit someone else's review with PUT and check status (403)", () ->
                api.review.forbiddenPutReview(requestData, loginResponse.access(), 1391));

        step("Check error message", () ->
                assertThat(response.detail()).isEqualTo("You do not have permission to perform this action."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Attempt for change other club reviews with PATCH")
    public void someoneElsePatchReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.wrongRandomAssessment, testData.readPages);

        SomeoneElsePutReviewResponseModel response = step("Edit someone else's review with PATCH and check status (403)", () ->
                api.review.forbiddenPatchReview(requestData, loginResponse.access(), 1391));

        step("Check error message", () ->
                assertThat(response.detail()).isEqualTo("You do not have permission to perform this action."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Successful delete club review")
    public void successfulDeleteReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        step("Delete review and check status (204)", () ->
                api.review.successfulDeleteReview(1982, loginResponse.access()));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Unauthorized user attempt for delete club review")
    public void unauthorizedDeleteReviewTest() {
        step("Delete review without authorization and check status (401)", () ->
                api.review.unauthorizedDeleteReview(testData.randomBookClubId));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Not found club review for delete")
    public void notFoundDeleteReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        step("Delete missing review and check status (404)", () ->
                api.review.notFoundDeleteReview(1982, loginResponse.access()));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Attempt for delete other club reviews")
    public void forbiddenDeleteReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        step("Delete someone else's review and check status (403)", () ->
                api.review.forbiddenDeleteReview(1888, loginResponse.access()));
    }
}
