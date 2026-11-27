package tests.uiapi;

import io.qameta.allure.*;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

@Feature("Club tests")
@Story("UI and API")
@Tags({@Tag("ui"), @Tag("api"), @Tag("clubs")})
public class ClubTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @AllureId("55479")
    @DisplayName("[API + UI] Check that club owner cannot leave the club")
    @Description("The owner of a created club cannot leave it: an error is shown after clicking leave.")
    public void cantLeaveClubAsOwnerTest_with_login_by_api() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.randomRegistrationUsername,
                testData.randomRegistrationPassword);
        LoginBodyModel loginData = new LoginBodyModel(testData.randomRegistrationUsername,
                testData.randomRegistrationPassword);

        api.auth.loginByApi(api.users.register(registrationData), api.auth.login(loginData));

        createClubPage.openPage()
                .setBookTitle(testData.bookTitle)
                .setBookAuthors(testData.bookAuthor)
                .setPublicationYear(testData.publicationYear)
                .setDescription(testData.description)
                .setTelegramChatLink(testData.telegramChatLink)
                .submitForm();

        homePage.openClubsList()
                .searchClub(testData.bookTitle)
                .openClubFromList(testData.bookTitle);

        clubPage.leaveClub();

        clubPage.checkLeaveError();
    }
}
