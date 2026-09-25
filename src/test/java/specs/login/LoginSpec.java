package specs.login;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;

public class LoginSpec {

    public static RequestSpecification loginRequestSpec = with()
            .log().all()
            .contentType(ContentType.JSON)
            .basePath("/api/v1");

    public static ResponseSpecification successfulLoginRequestSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/login/successful_login_response_schema.json"))
            .expectBody("access", notNullValue())
            .expectBody("refresh", notNullValue())
            .build();

    public static ResponseSpecification wrongCredentialsLoginRequestSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath("schemas/login/wrong_credentials_login_response_schema.json"))
            .expectBody("detail", notNullValue())
            .build();

    public static ResponseSpecification emptyUsernameLoginRequestSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath("schemas/login/empty_username_login_response_schema.json"))
            .expectBody("username", notNullValue())
            .build();

    public static ResponseSpecification emptyPasswordLoginRequestSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath("schemas/login/empty_password_login_response_schema.json"))
            .expectBody("password", notNullValue())
            .build();

    public static ResponseSpecification emptyCredentialsLoginRequestSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath("schemas/login/empty_credentials_login_response_schema.json"))
            .expectBody("username", notNullValue())
            .expectBody("password", notNullValue())
            .build();
}
