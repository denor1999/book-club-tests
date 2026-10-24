package api;

import models.reviews.*;

import static io.restassured.RestAssured.given;
import static specs.reviews.ReviewSpec.*;

public class ReviewsApiClient {

    public ReviewsGetResponseModel getReviews() {
        return given()
                .spec(reviewRequestSpec)
                .when()
                .get("clubs/reviews/?club=1")
                .then()
                .spec(successfulGetReviewsSpec)
                .extract()
                .as(ReviewsGetResponseModel.class);
    }

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
}
