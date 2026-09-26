package specs.update;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import specs.BaseSpec;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UpdateSpec extends BaseSpec {

    public static RequestSpecification updateRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulUpdateSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/update_user/update_data_response_schema.json"))
            .build();

    public static ResponseSpecification unauthorizedUpdateSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath("schemas/update_user/unauthorized_user_update_response_schema.json"))
            .build();

    public static ResponseSpecification redirectUpdateSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(301)
            .build();
}
