package tests.api;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.reviews.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("Reviews API tests")
@Tags({@Tag("api"), @Tag("reviews")})
public class ReviewsApiTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] get club reviews")
    @Description("The server returns 200. The size of results matches count.")
    public void getReviewsTest() {
        ReviewsGetResponseModel response = step("Send get reviews request and check status (200)", () ->
                api.review.getReviews(testData.reviewClubId));

        step("Check reviews count matches results size", () ->
                assertThat(response.results()).hasSize(response.count()));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Successful send review")
    @Description("An authorized user creates a review for an existing club. The server returns 201, and id, club, text, assessment, and read pages match the submitted values.")
    public void successfulPostReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(testData.clubId,
                testData.description, testData.randomAssessment, testData.readPages);

        ReviewResponseModel response = step("Send review and check status (201)", () ->
                api.review.successfulPostReview(requestData, loginResponse.access()));

        step("Check created review fields", () -> {
            assertThat(response.id()).isNotNull();
            assertThat(response.club()).isEqualTo(testData.clubId);
            assertThat(response.review()).isEqualTo(testData.description);
            assertThat(response.assessment()).isEqualTo(testData.randomAssessment);
            assertThat(response.readPages()).isEqualTo(testData.readPages);
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Unauthorized user attempt for send review")
    @Description("Without authorization the server returns 401 and \"Authentication credentials were not provided.\"")
    public void unauthorizedPostReviewTest() {
        ReviewRequestModel requestData = new ReviewRequestModel(testData.randomBookClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        UnauthorizedReviewModel response = step("Send review without authorization and check status (401)", () ->
                api.review.unauthorizedPostReview(requestData));

        step("Check error message", () ->
                assertThat(response.detail()).isEqualTo(testData.unauthorizedDetail));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Attempt for send review with wrong access token")
    @Description("With an invalid token the server returns 401: detail is \"Given token not valid for any token type\" and code is \"token_not_valid\".")
    public void wrongTokenPostReviewTest() {
        ReviewRequestModel requestData = new ReviewRequestModel(testData.randomBookClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        WrongTokenPostReviewModel response = step("Send review with wrong access token and check status (401)", () ->
                api.review.wrongAccessTokenPostReview(requestData, testData.wrongAccessToken));

        step("Check error message and code", () -> {
            assertThat(response.detail()).isEqualTo(testData.wrongTokenDetail);
            assertThat(response.code()).isEqualTo(testData.wrongTokenCode);
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Successful edit club review with PUT")
    @Description("An authorized PUT returns 200. Review text, assessment, and read pages match the submitted values, and modified is present.")
    public void successfulPutReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(testData.reviewClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        ReviewResponseModel response = step("Edit review with PUT and check status (200)", () ->
                api.review.successfulPutReview(requestData, loginResponse.access(), testData.ownReviewId));

        step("Check updated review fields", () -> {
            assertThat(response.review()).isEqualTo(testData.description);
            assertThat(response.assessment()).isEqualTo(testData.randomAssessment);
            assertThat(response.readPages()).isEqualTo(testData.readPages);
            assertThat(response.modified()).isNotNull();
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Successful edit club review with PATCH")
    @Description("An authorized PATCH returns 200. Review text, assessment, and read pages match the submitted values, and modified is present.")
    public void successfulPatchReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(testData.reviewClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        ReviewResponseModel response = step("Edit review with PATCH and check status (200)", () ->
                api.review.successfulPatchReview(requestData, loginResponse.access(), testData.ownReviewId));

        step("Check updated review fields", () -> {
            assertThat(response.review()).isEqualTo(testData.description);
            assertThat(response.assessment()).isEqualTo(testData.randomAssessment);
            assertThat(response.readPages()).isEqualTo(testData.readPages);
            assertThat(response.modified()).isNotNull();
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Attempt for edit club review with wrong assessment with PUT")
    @Description("A PUT with an assessment greater than 5 returns 400 and \"Ensure this value is less than or equal to 5.\"")
    public void wrongAssessmentValuePutReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(testData.reviewClubId,
                testData.description, testData.wrongRandomAssessment, testData.readPages);

        WrongAssessmentReviewResponseModel response = step("Edit review with PUT and wrong assessment and check status (400)", () ->
                api.review.wrongAssessmentPutReview(requestData, loginResponse.access(), testData.ownReviewId));

        step("Check error message", () ->
                assertThat(response.assessment().getFirst()).isEqualTo(testData.wrongAssessmentError));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Attempt for edit club review with wrong assessment with PATCH")
    @Description("A PATCH with an assessment greater than 5 returns 400 and \"Ensure this value is less than or equal to 5.\"")
    public void wrongAssessmentValuePatchReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(testData.reviewClubId,
                testData.description, testData.wrongRandomAssessment, testData.readPages);

        WrongAssessmentReviewResponseModel response = step("Edit review with PATCH and wrong assessment and check status (400)", () ->
                api.review.wrongAssessmentPatchReview(requestData, loginResponse.access(), testData.ownReviewId));

        step("Check error message", () ->
                assertThat(response.assessment().getFirst()).isEqualTo(testData.wrongAssessmentError));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Unauthorized user attempt for edit club review with PUT")
    @Description("A PUT without authorization returns 401 and \"Authentication credentials were not provided.\"")
    public void unauthorizedPutReviewTest() {
        ReviewRequestModel requestData = new ReviewRequestModel(testData.reviewClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        UnauthorizedReviewModel response = step("Edit review with PUT without authorization and check status (401)", () ->
                api.review.unauthorizedPutReview(requestData, testData.missingReviewId));

        step("Check error message", () ->
                assertThat(response.detail()).isEqualTo(testData.unauthorizedDetail));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Unauthorized user attempt for edit club review with PATCH")
    @Description("A PATCH without authorization returns 401 and \"Authentication credentials were not provided.\"")
    public void unauthorizedPatchReviewTest() {
        ReviewRequestModel requestData = new ReviewRequestModel(testData.reviewClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        UnauthorizedReviewModel response = step("Edit review with PATCH without authorization and check status (401)", () ->
                api.review.unauthorizedPatchReview(requestData, testData.missingReviewId));

        step("Check error message", () ->
                assertThat(response.detail()).isEqualTo(testData.unauthorizedDetail));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Attempt for change other club reviews with PUT")
    @Description("A PUT of someone else's review returns 403 and \"You do not have permission to perform this action.\"")
    public void someoneElsePutReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(testData.reviewClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        SomeoneElsePutReviewResponseModel response = step("Edit someone else's review with PUT and check status (403)", () ->
                api.review.forbiddenPutReview(requestData, loginResponse.access(), testData.foreignReviewId));

        step("Check error message", () ->
                assertThat(response.detail()).isEqualTo(testData.forbiddenDetail));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Attempt for change other club reviews with PATCH")
    @Description("A PATCH of someone else's review returns 403 and \"You do not have permission to perform this action.\"")
    public void someoneElsePatchReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ReviewRequestModel requestData = new ReviewRequestModel(testData.reviewClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        SomeoneElsePutReviewResponseModel response = step("Edit someone else's review with PATCH and check status (403)", () ->
                api.review.forbiddenPatchReview(requestData, loginResponse.access(), testData.foreignReviewId));

        step("Check error message", () ->
                assertThat(response.detail()).isEqualTo(testData.forbiddenDetail));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Successful delete club review")
    @Description("Deleting one's own review while authorized returns 204.")
    public void successfulDeleteReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        step("Delete review and check status (204)", () ->
                api.review.successfulDeleteReview(testData.ownReviewDeleteId, loginResponse.access()));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Unauthorized user attempt for delete club review")
    @Description("Deleting a review without authorization returns 401.")
    public void unauthorizedDeleteReviewTest() {
        step("Delete review without authorization and check status (401)", () ->
                api.review.unauthorizedDeleteReview(testData.missingReviewId));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Not found club review for delete")
    @Description("Deleting a missing review while authorized returns 404.")
    public void notFoundDeleteReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        step("Delete missing review and check status (404)", () ->
                api.review.notFoundDeleteReview(testData.missingReviewId, loginResponse.access()));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Attempt for delete other club reviews")
    @Description("Deleting someone else's review returns 403.")
    public void forbiddenDeleteReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        step("Delete someone else's review and check status (403)", () ->
                api.review.forbiddenDeleteReview(testData.foreignReviewDeleteId, loginResponse.access()));
    }
}
