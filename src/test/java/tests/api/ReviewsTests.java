package tests.api;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.reviews.*;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewsTests extends TestBase {

    @Test
    public void getReviewsTest() {
        ReviewsGetResponseModel response = api.review.getReviews();

        assertThat(response.results()).hasSize(response.count());
    }

    @Test
    public void successfulPostReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        ReviewRequestModel requestData = new ReviewRequestModel(testData.randomBookClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        ReviewResponseModel response = api.review.successfulPostReview(requestData, loginResponse.access());

        assertThat(response.id()).isNotNull();
    }

    @Test
    public void unauthorizedPostReviewTest() {
        ReviewRequestModel requestData = new ReviewRequestModel(testData.randomBookClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        UnauthorizedReviewModel response = api.review.unauthorizedPostReview(requestData);

        assertThat(response.detail()).isEqualTo("Authentication credentials were not provided.");
    }

    @Test
    public void wrongTokenPostReviewTest() {
        ReviewRequestModel requestData = new ReviewRequestModel(testData.randomBookClubId,
                testData.description, testData.randomAssessment, testData.readPages);

        WrongTokenPostReviewModel response = api.review.wrongAccessTokenPostReview(requestData, testData.wrongAccessToken);

        assertThat(response.detail()).isEqualTo("Given token not valid for any token type");
        assertThat(response.code()).isEqualTo("token_not_valid");
    }

    @Test
    public void successfulPutReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.randomAssessment, testData.readPages);

        ReviewResponseModel response = api.review.successfulPutReview(requestData,
                loginResponse.access(), 1883);

        assertThat(response.modified()).isNotNull();
    }

    @Test
    public void successfulPatchReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.randomAssessment, testData.readPages);

        ReviewResponseModel response = api.review.successfulPatchReview(requestData,
                loginResponse.access(), 1883);

        assertThat(response.modified()).isNotNull();
    }

    @Test
    public void wrongAssessmentValuePutReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.wrongRandomAssessment, testData.readPages);

        WrongAssessmentReviewResponseModel response = api.review.wrongAssessmentPutReview(requestData,
                loginResponse.access(), 1883);

        assertThat(response.assessment().getFirst()).isEqualTo("Ensure this value is less than or equal to 5.");
    }

    @Test
    public void wrongAssessmentValuePatchReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.wrongRandomAssessment, testData.readPages);

        WrongAssessmentReviewResponseModel response = api.review.wrongAssessmentPatchReview(requestData,
                loginResponse.access(), 1883);

        assertThat(response.assessment().getFirst()).isEqualTo("Ensure this value is less than or equal to 5.");
    }

    @Test
    public void unauthorizedPutReviewTest() {
        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.randomAssessment, testData.readPages);

        UnauthorizedReviewModel response = api.review.unauthorizedPutReview(requestData, 1883);

        assertThat(response.detail()).isEqualTo("Authentication credentials were not provided.");
    }

    @Test
    public void unauthorizedPatchReviewTest() {
        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.randomAssessment, testData.readPages);

        UnauthorizedReviewModel response = api.review.unauthorizedPatchReview(requestData, 1883);

        assertThat(response.detail()).isEqualTo("Authentication credentials were not provided.");
    }

    @Test
    public void someoneElsePutReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.wrongRandomAssessment, testData.readPages);

        SomeoneElsePutReviewResponseModel response = api.review.forbiddenPutReview(requestData,
                loginResponse.access(), 1391);

        assertThat(response.detail()).isEqualTo("You do not have permission to perform this action.");
    }

    @Test
    public void someoneElsePatchReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        ReviewRequestModel requestData = new ReviewRequestModel(1,
                testData.description, testData.wrongRandomAssessment, testData.readPages);

        SomeoneElsePutReviewResponseModel response = api.review.forbiddenPatchReview(requestData,
                loginResponse.access(), 1391);

        assertThat(response.detail()).isEqualTo("You do not have permission to perform this action.");
    }


}
