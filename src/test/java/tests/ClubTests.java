package tests;

import io.qameta.allure.Owner;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationResponseSuccessfulModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class ClubTests extends TestBase{

    @Test
    @Owner("denor1999")
    @DisplayName("Check that club owner cannot leave the club")
    public void cantLeaveClubAsOwnerTest_with_login_by_api() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomRegistrationUsername,
                testData.randomRegistrationPassword);
        RegistrationResponseSuccessfulModel registerResponse = api.users.register(registrationData);

        LoginBodyModel loginData = new LoginBodyModel(testData.randomRegistrationUsername,
                testData.randomRegistrationPassword);
        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

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

        open("/favicon.ico");
        localStorage().setItem("book_club_auth", localStorageAuthBody);
        open("/");

        open("/clubs/create");
        $("#bookTitle").setValue(testData.bookTitle);
        $("#bookAuthors").setValue(testData.bookAuthor);
        $("#publicationYear").setValue(String.valueOf(testData.publicationYear));
        $("#description").setValue(testData.description);
        $("#telegramChatLink").setValue(testData.telegramChatLink);
        $(".submit-btn").scrollTo().click();

        $("[data-testid=clubs-link]").click();
        $(".search-input").setValue(testData.bookTitle).pressEnter();
        $(".clubs-list").$(byText(testData.bookTitle))
                .parent().parent().$(".open-btn").click();
        $(".leave-btn").click();

        $(".error").shouldBe(visible);
    }
}
