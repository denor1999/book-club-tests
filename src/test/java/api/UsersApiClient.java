package api;

import models.registration.*;

import static io.restassured.RestAssured.given;
import static specs.registration.RegistrationSpec.*;

public class UsersApiClient {

    public RegistrationResponseSuccessfulModel register(RegistrationBodyModel registerBody) {
        return given()
                .spec(registrationRequestSpec)
                .body(registerBody)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationRequestSpec)
                .extract()
                .as(RegistrationResponseSuccessfulModel.class);
    }

    public ExistingUserResponseModel existingUserRegister(RegistrationBodyModel registerBody) {
        return given()
                .spec(registrationRequestSpec)
                .body(registerBody)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationRequestSpec)
                .extract()
                .as(ExistingUserResponseModel.class);
    }

    public WrongUsernameRegistrationResponseModel wrongUsernameRegister(RegistrationBodyModel registerBody) {
        return given()
                .spec(registrationRequestSpec)
                .body(registerBody)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongUsernameRegistrationResponseSpec)
                .extract()
                .as(WrongUsernameRegistrationResponseModel.class);
    }

    public WrongPasswordRegistrationResponseModel wrongPasswordRegister(RegistrationBodyModel registerBody) {
        return given()
                .spec(registrationRequestSpec)
                .body(registerBody)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongPasswordRegistrationResponseSpec)
                .extract()
                .as(WrongPasswordRegistrationResponseModel.class);
    }

    public WrongCredentialsResponseModel wrongCredentialsRegister(RegistrationBodyModel registerBody) {
        return given()
                .spec(registrationRequestSpec)
                .body(registerBody)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongCredentialsRegistrationResponseSpec)
                .extract()
                .as(WrongCredentialsResponseModel.class);
    }
}
