package tests;

import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseSuccessfulModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.registration.RegistrationSpec.*;

public class RegistrationTests extends TestBase{

    @Test
    public void successfulRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomUsername, testData.randomPassword);

        RegistrationResponseSuccessfulModel registrationResponse = given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationRequestSpec)
                .extract()
                .as(RegistrationResponseSuccessfulModel.class);

        assertThat(testData.randomUsername).isEqualTo(registrationResponse.username());
        assertThat(registrationResponse.id()).isNotNull();
        assertThat(registrationResponse.firstName()).isBlank();
        assertThat(registrationResponse.lastName()).isBlank();
        assertThat(registrationResponse.email()).isBlank();
    }


    @Test
    public void existingUserRegistrationTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomUsername, testData.randomPassword);

        RegistrationResponseSuccessfulModel firstRegistrationResponse = given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationRequestSpec)
                .extract()
                .as(RegistrationResponseSuccessfulModel.class);

        assertThat(testData.randomUsername).isEqualTo(firstRegistrationResponse.username());

        ExistingUserResponseModel secondRegistrationResponse = given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationRequestSpec)
                .extract()
                .as(ExistingUserResponseModel.class);

        String actualError = secondRegistrationResponse.username().getFirst();
        assertThat(actualError).isEqualTo(testData.expectedExistingUserError);
    }

    //todo add more negative tests

}
