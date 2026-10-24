package specs.reviews;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static specs.BaseSpec.baseRequestSpec;

public class ReviewSpec {

    public static RequestSpecification reviewRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulGetReviewsSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/reviews/successful_get_reviews_json_schema.json"))
            .build();

    public static ResponseSpecification successfulPostReviewSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath("schemas/reviews/successful_post_review_json_shema.json"))
            .build();

    public static ResponseSpecification unauthorizedReviewsSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath("schemas/reviews/unauthorized_post_review_json_schema.json"))
            .build();

    public static ResponseSpecification wrongTokenReviewsPostSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath("schemas/reviews/wrong_access_token_post_review_json_schema.json"))
            .build();

    public static ResponseSpecification successfulEditReviewSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/reviews/successful_edit_review_json_schema.json"))
            .build();

    public static ResponseSpecification wrongAssessmentValueReviewSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath("schemas/reviews/wron_assessment_value_review_json_schema.json"))
            .build();

    public static ResponseSpecification forbiddenReviewSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(403)
            .expectBody(matchesJsonSchemaInClasspath("schemas/reviews/forbidden_review_json_schema.json"))
            .build();
}
