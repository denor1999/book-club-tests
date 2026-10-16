package api;

import io.qameta.allure.Step;
import models.registration.*;
import models.update_user.UnauthorizedUserUpdateWithPutResponseModel;
import models.update_user.UpdateUserPatchBodyModel;
import models.update_user.UpdateUserResponseModel;
import models.update_user.UpdateUserWithPutBodyModel;

import static io.restassured.RestAssured.given;
import static specs.registration.RegistrationSpec.*;
import static specs.update.UpdateSpec.*;

public class UsersApiClient {

    @Step("Send registration request and check status (201)")
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

    @Step("Send registration request for existing user and check status (400)")
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

    @Step("Send registration request with wrong username and check status (400)")
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

    @Step("Send registration request with wrong password and check status (400)")
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

    @Step("Send registration request with wrong credentials and check status (400)")
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

    @Step("Send edit user request with PUT and check status (200)")
    public UpdateUserResponseModel updateUserDataWithPut(UpdateUserWithPutBodyModel updateBody, String access) {
        return given()
                .spec(updateRequestSpec)
                .header("Authorization", "Bearer " + access)
                .body(updateBody)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulUpdateSpec)
                .extract()
                .as(UpdateUserResponseModel.class);
    }

    @Step("Send edit user request with PUT without '/' and check status (301)")
    public void redirectUpdate(UpdateUserWithPutBodyModel updateBody, String access) {
        given()
                .spec(updateRequestSpec)
                .header("Authorization", "Bearer " + access)
                .body(updateBody)
                .basePath("/api/v1")
                .when()
                .put("/users/me")
                .then()
                .spec(redirectUpdateSpec);
    }

    @Step("Send edit user request with PUT without authorization and check status (401)")
    public UnauthorizedUserUpdateWithPutResponseModel unauthorizedUserUpdate(UpdateUserWithPutBodyModel updateBody) {
        return given()
                .spec(updateRequestSpec)
                .body(updateBody)
                .when()
                .put("/users/me/")
                .then()
                .spec(unauthorizedUpdateSpec)
                .extract()
                .as(UnauthorizedUserUpdateWithPutResponseModel.class);
    }

    @Step("Send edit user request with PATCH and check status (200)")
    public UpdateUserResponseModel updateUserDataWithPatch(UpdateUserPatchBodyModel updateBody, String access) {
        return given()
                .spec(updateRequestSpec)
                .header("Authorization", "Bearer " + access)
                .body(updateBody)
                .when()
                .patch("/users/me/")
                .then()
                .spec(successfulUpdateSpec)
                .extract()
                .as(UpdateUserResponseModel.class);
    }
}
