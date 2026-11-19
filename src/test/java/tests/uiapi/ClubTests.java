package tests.uiapi;

import io.qameta.allure.*;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;

@Feature("Club tests")
@Story("UI and API")
@Tags({@Tag("ui"), @Tag("api"), @Tag("clubs")})
public class ClubTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API + UI] Check that club owner cannot leave the club")
    @Description("The owner of a created club cannot leave it: an error is shown after clicking leave.")
    public void cantLeaveClubAsOwnerTest_with_login_by_api() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomRegistrationUsername,
                testData.randomRegistrationPassword);
        LoginBodyModel loginData = new LoginBodyModel(testData.randomRegistrationUsername,
                testData.randomRegistrationPassword);

        step("Send login request and check status (200)", () ->
                api.auth.loginByApi(api.users.register(registrationData), api.auth.login(loginData)));

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
