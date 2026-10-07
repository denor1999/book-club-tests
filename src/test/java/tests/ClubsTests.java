package tests;

import models.clubs.get.ClubResultsModel;
import models.clubs.get.ClubsListResponseModel;
import models.clubs.post.ClubPostRequestModel;
import models.clubs.post.ClubPostResponseModel;
import models.clubs.post.UnauthorizedPostClubResponseModel;
import models.clubs.post.WrongTelegramChatLinkResponseModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class ClubsTests extends TestBase{

    @Test
    public void successfulGetClubsTest() {
        ClubsListResponseModel getClubsResponse = api.clubs.getClubs();

        assertThat(getClubsResponse).isNotNull();
        assertThat(getClubsResponse.count()).isGreaterThanOrEqualTo(0);
        assertThat(getClubsResponse.results()).isNotNull();
    }

    @Test
    @Disabled
    public void getClubsCountMatchesResultsSizeTest() {
        ClubsListResponseModel getClubsResponse = api.clubs.getClubs();

        assertThat(getClubsResponse.results()).hasSize(getClubsResponse.count());
    }

    @Test
    public void getClubsEachClubHasRequiredFields() {
        ClubsListResponseModel response = api.clubs.getClubs();

        for (ClubResultsModel club : response.results()) {
            assertThat(club.id()).isNotNull().isPositive();
            assertThat(club.bookTitle()).isNotNull();
            assertThat(club.bookAuthors()).isNotNull();
            assertThat(club.publicationYear()).isNotNull();
            assertThat(club.description()).isNotNull();
            assertThat(club.telegramChatLink()).isNotNull();
            assertThat(club.owner()).isNotNull().isPositive();
            assertThat(club.members()).isNotNull();
            assertThat(club.reviews()).isNotNull();
            assertThat(club.created()).isNotNull();
        }
    }

    @Test
    @Disabled
    public void unauthorizedGetClubsTest() {
        ClubsListResponseModel response = api.clubs.unauthorizedGetClubs();
    }

    @Test
    public void successfulPostGlubsTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ClubPostRequestModel requestData = new ClubPostRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        ClubPostResponseModel response = step("Create new book club", () ->
                api.clubs.postClub(requestData, loginResponse.access()));

        step("Check content of the fields", () -> {
            assertThat(response.bookTitle()).isEqualTo(testData.bookTitle);
            assertThat(response.bookAuthors()).isEqualTo(testData.bookAuthor);
            assertThat(response.publicationYear()).isEqualTo(testData.publicationYear);
            assertThat(response.description()).isEqualTo(testData.description);
            assertThat(response.telegramChatLink()).isEqualTo(testData.telegramChatLink);
        });
    }

    @Test
    public void wrongTelegramChatLinkPostGlubsTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ClubPostRequestModel requestData = new ClubPostRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, testData.wrongTelegramChatLink);

        WrongTelegramChatLinkResponseModel response = step("Create new book club and check status code", () ->
                api.clubs.wrongTelegramChatLinkPostClub(requestData, loginResponse.access()));

        step("Check error message", () ->
                assertThat(response.telegramChatLink().getFirst()).isEqualTo("Enter a valid URL."));
    }

    @Test
    public void unauthorizedPostGlubsTest() {
        ClubPostRequestModel requestData = new ClubPostRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        UnauthorizedPostClubResponseModel response = step("Create new book club", () ->
                api.clubs.unauthorizedPostClub(requestData));

        step("Check error message", () ->
                assertThat(response.detail()).isEqualTo("Authentication credentials were not provided."));
    }

    //todo more tests for CRUD
}
