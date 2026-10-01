package api;

import models.registration.*;
import models.update_user.UnauthorizedUserUpdateWithPutResponseModel;
import models.update_user.UpdateUserPatchBodyModel;
import models.update_user.UpdateUserResponseModel;
import models.update_user.UpdateUserWithPutBodyModel;

import static io.restassured.RestAssured.given;
import static specs.registration.RegistrationSpec.*;
import static specs.update.UpdateSpec.*;

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
