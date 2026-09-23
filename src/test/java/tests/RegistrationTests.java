package tests;

import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseSuccessfulModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.registration.RegistrationSpec.*;

public class RegistrationTests extends TestBase{

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        username = faker.name().firstName() + "_" + System.currentTimeMillis();
        password = faker.name().lastName();
    }

    @Test
    public void successfulRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        RegistrationResponseSuccessfulModel registrationResponse = given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationRequestSpec)
                .extract()
                .as(RegistrationResponseSuccessfulModel.class);

        assertThat(username).isEqualTo(registrationResponse.username());
        assertThat(registrationResponse.id()).isNotNull();
        assertThat(registrationResponse.firstName()).isBlank();
        assertThat(registrationResponse.lastName()).isBlank();
        assertThat(registrationResponse.email()).isBlank();
    }


    @Test
    public void existingUserRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        RegistrationResponseSuccessfulModel firstRegistrationResponse = given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationRequestSpec)
                .extract()
                .as(RegistrationResponseSuccessfulModel.class);

        assertThat(username).isEqualTo(firstRegistrationResponse.username());

        ExistingUserResponseModel secondRegistrationResponse = given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationRequestSpec)
                .extract()
                .as(ExistingUserResponseModel.class);

        String expectedError = "A user with that username already exists.";
        String actualError = secondRegistrationResponse.username().getFirst();
        assertThat(actualError).isEqualTo(expectedError);
    }

    //todo add more negative tests

}
