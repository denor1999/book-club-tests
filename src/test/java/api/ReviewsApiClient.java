package api;

import io.qameta.allure.Step;
import models.reviews.*;

import static io.restassured.RestAssured.given;
import static specs.reviews.ReviewSpec.*;

public class ReviewsApiClient {

    @Step("Send get reviews request and check status (200)")
    public ReviewsGetResponseModel getReviews(Integer clubId) {
        return given()
                .spec(reviewRequestSpec)
                .when()
                .get("clubs/reviews/?club=" + clubId)
                .then()
                .spec(successfulGetReviewsSpec)
                .extract()
                .as(ReviewsGetResponseModel.class);
    }

    @Step("Send create review request and check status (201)")
    public ReviewResponseModel successfulPostReview(ReviewRequestModel postBody, String accessToken) {
        return given()
                .spec(reviewRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(postBody)
                .when()
                .post("clubs/reviews/")
                .then()
                .spec(successfulPostReviewSpec)
                .extract()
                .as(ReviewResponseModel.class);
    }

    @Step("Send create review request without authorization and check status (401)")
    public UnauthorizedReviewModel unauthorizedPostReview(ReviewRequestModel postBody) {
        return given()
                .spec(reviewRequestSpec)
                .body(postBody)
                .when()
                .post("clubs/reviews/")
                .then()
                .spec(unauthorizedReviewsSpec)
                .extract()
                .as(UnauthorizedReviewModel.class);
    }

    @Step("Send create review request with wrong access token and check status (401)")
    public WrongTokenPostReviewModel wrongAccessTokenPostReview(ReviewRequestModel postBody, String accessToken) {
        return given()
                .spec(reviewRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(postBody)
                .when()
                .post("clubs/reviews/")
                .then()
                .spec(wrongTokenReviewsPostSpec)
                .extract()
                .as(WrongTokenPostReviewModel.class);
    }

    @Step("Send edit review request with PUT and check status (200)")
    public ReviewResponseModel successfulPutReview(ReviewRequestModel postBody, String accessToken, Integer id) {
        String pathFormatted =  String.format("clubs/reviews/%d/", id);

        return given()
                .spec(reviewRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(postBody)
                .when()
                .put(pathFormatted)
                .then()
                .spec(successfulEditReviewSpec)
                .extract()
                .as(ReviewResponseModel.class);
    }

    @Step("Send edit review request with PATCH and check status (200)")
    public ReviewResponseModel successfulPatchReview(ReviewRequestModel postBody, String accessToken, Integer id) {
        String pathFormatted =  String.format("clubs/reviews/%d/", id);

        return given()
                .spec(reviewRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(postBody)
                .when()
                .patch(pathFormatted)
                .then()
                .spec(successfulEditReviewSpec)
                .extract()
                .as(ReviewResponseModel.class);
    }

    @Step("Send edit review request with PUT and wrong assessment and check status (400)")
    public WrongAssessmentReviewResponseModel wrongAssessmentPutReview(ReviewRequestModel postBody, String accessToken, Integer id) {
        String pathFormatted =  String.format("clubs/reviews/%d/", id);

        return given()
                .spec(reviewRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(postBody)
                .when()
                .put(pathFormatted)
                .then()
                .spec(wrongAssessmentValueReviewSpec)
                .extract()
                .as(WrongAssessmentReviewResponseModel.class);
    }

    @Step("Send edit review request with PATCH and wrong assessment and check status (400)")
    public WrongAssessmentReviewResponseModel wrongAssessmentPatchReview(ReviewRequestModel postBody, String accessToken, Integer id) {
        String pathFormatted =  String.format("clubs/reviews/%d/", id);

        return given()
                .spec(reviewRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(postBody)
                .when()
                .patch(pathFormatted)
                .then()
                .spec(wrongAssessmentValueReviewSpec)
                .extract()
                .as(WrongAssessmentReviewResponseModel.class);
    }

    @Step("Send edit review request with PUT without authorization and check status (401)")
    public UnauthorizedReviewModel unauthorizedPutReview(ReviewRequestModel postBody, Integer id) {
        String pathFormatted =  String.format("clubs/reviews/%d/", id);

        return given()
                .spec(reviewRequestSpec)
                .body(postBody)
                .when()
                .put(pathFormatted)
                .then()
                .spec(unauthorizedReviewsSpec)
                .extract()
                .as(UnauthorizedReviewModel.class);
    }

    @Step("Send edit review request with PATCH without authorization and check status (401)")
    public UnauthorizedReviewModel unauthorizedPatchReview(ReviewRequestModel postBody, Integer id) {
        String pathFormatted =  String.format("clubs/reviews/%d/", id);

        return given()
                .spec(reviewRequestSpec)
                .body(postBody)
                .when()
                .patch(pathFormatted)
                .then()
                .spec(unauthorizedReviewsSpec)
                .extract()
                .as(UnauthorizedReviewModel.class);
    }

    @Step("Send edit someone else's review request with PUT and check status (403)")
    public SomeoneElsePutReviewResponseModel forbiddenPutReview(ReviewRequestModel postBody, String accessToken, Integer id) {
        String pathFormatted =  String.format("clubs/reviews/%d/", id);

        return given()
                .spec(reviewRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(postBody)
                .when()
                .put(pathFormatted)
                .then()
                .spec(forbiddenReviewSpec)
                .extract()
                .as(SomeoneElsePutReviewResponseModel.class);
    }

    @Step("Send edit someone else's review request with PATCH and check status (403)")
    public SomeoneElsePutReviewResponseModel forbiddenPatchReview(ReviewRequestModel postBody, String accessToken, Integer id) {
        String pathFormatted =  String.format("clubs/reviews/%d/", id);

        return given()
                .spec(reviewRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(postBody)
                .when()
                .patch(pathFormatted)
                .then()
                .spec(forbiddenReviewSpec)
                .extract()
                .as(SomeoneElsePutReviewResponseModel.class);
    }

    @Step("Send delete review request and check status (204)")
    public void successfulDeleteReview(Integer id, String accessToken) {
        given()
                .spec(reviewRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("clubs/reviews/" + id + "/")
                .then()
                .spec(successfulDeleteReviewSpec);
    }

    @Step("Send delete review request without authorization and check status (401)")
    public void unauthorizedDeleteReview(Integer id) {
        given()
                .spec(reviewRequestSpec)
                .when()
                .delete("clubs/reviews/" + id + "/")
                .then()
                .spec(unauthorizedDeleteReviewSpec);
    }

    @Step("Send delete missing review request and check status (404)")
    public void notFoundDeleteReview(Integer id, String accessToken) {
        given()
                .spec(reviewRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("clubs/reviews/" + id + "/")
                .then()
                .spec(notFoundDeleteReviewSpec);
    }

    @Step("Send delete someone else's review request and check status (403)")
    public void forbiddenDeleteReview(Integer id, String accessToken) {
        given()
                .spec(reviewRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("clubs/reviews/" + id + "/")
                .then()
                .spec(forbiddenDeleteReviewSpec);
    }
}
