package tests.uiapi;

import io.qameta.allure.Owner;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseSuccessfulModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static com.codeborne.selenide.Selenide.localStorage;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

public class ClubTests extends TestBase {

    @Test
    @Owner("denor1999")
    @DisplayName("[API + UI] Check that club owner cannot leave the club")
    public void cantLeaveClubAsOwnerTest_with_login_by_api() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomRegistrationUsername,
                testData.randomRegistrationPassword);
        RegistrationResponseSuccessfulModel registerResponse = step("Send registration request and check status (201)", () ->
                api.users.register(registrationData));

        LoginBodyModel loginData = new LoginBodyModel(testData.randomRegistrationUsername,
                testData.randomRegistrationPassword);
        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        String accessToken = loginResponse.access();
        String refreshToken = loginResponse.refresh();

        String localStorageAuthBody = """
                {
                  "user": {
                    "id": %d,
                    "username": "%s",
                    "firstName": "%s",
                    "lastName": "%s",
                    "email": "%s",
                    "remoteAddr": "%s"
                  },
                  "accessToken": "%s",
                  "refreshToken": "%s",
                  "isAuthenticated": true
                }
                """.formatted(
                registerResponse.id(),
                registerResponse.username(),
                registerResponse.firstName(),
                registerResponse.lastName(),
                registerResponse.email(),
                registerResponse.remoteAddr(),
                accessToken,
                refreshToken
        );

        step("Authorize in UI via local storage", () -> {
            open("/favicon.ico");
            localStorage().setItem("book_club_auth", localStorageAuthBody);
            open("/");
        });

        step("Open create club page", () ->
                createClubPage.openPage());

        step("Fill club form", () ->
                createClubPage.setBookTitle(testData.bookTitle)
                        .setBookAuthors(testData.bookAuthor)
                        .setPublicationYear(testData.publicationYear)
                        .setDescription(testData.description)
                        .setTelegramChatLink(testData.telegramChatLink));

        step("Submit club form", () ->
                createClubPage.submitForm());

        step("Open clubs list", () ->
                homePage.openClubsList());

        step("Search created club", () ->
                homePage.searchClub(testData.bookTitle));

        step("Open club from list", () ->
                homePage.openClubFromList(testData.bookTitle));

        step("Try to leave the club", () ->
                clubPage.leaveClub());

        step("Check owner cannot leave the club", () ->
                clubPage.checkLeaveError());
    }
}
