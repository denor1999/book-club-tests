package tests;

import models.clubs.*;
import models.clubs.delete.DeleteClubWithErrorResponseModel;
import models.clubs.get.ClubResultsModel;
import models.clubs.get.ClubsListResponseModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class ClubsTests extends TestBase {

    @Test
    public void successfulGetClubsTest() {
        ClubsListResponseModel getClubsResponse = api.clubs.successfulGetClubs();

        assertThat(getClubsResponse).isNotNull();
        assertThat(getClubsResponse.count()).isGreaterThanOrEqualTo(0);
        assertThat(getClubsResponse.results()).isNotNull();
    }

    @Test
    public void getClubsCountMatchesResultsSizeTest() {
        ClubsListResponseModel getClubsResponse = api.clubs.successfulGetClubs();

        assertThat(getClubsResponse.results()).hasSize(getClubsResponse.count());
    }

    @Test
    public void getClubsEachClubHasRequiredFields() {
        ClubsListResponseModel response = api.clubs.successfulGetClubs();

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
    public void successfulPostGlubsTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ClubUpsertRequestModel requestData = new ClubUpsertRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        ClubUpsertResponseModel response = step("Create new book club", () ->
                api.clubs.successfulPostClub(requestData, loginResponse.access()));

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
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ClubUpsertRequestModel requestData = new ClubUpsertRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, testData.wrongTelegramChatLink);

        WrongTelegramChatLinkResponseModel response = step("Create new book club and check status code", () ->
                api.clubs.wrongTelegramChatLinkPostClub(requestData, loginResponse.access()));

        step("Check error message", () ->
                assertThat(response.telegramChatLink().getFirst()).isEqualTo("Enter a valid URL."));
    }

    @Test
    public void unauthorizedPostGlubsTest() {
        ClubUpsertRequestModel requestData = new ClubUpsertRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        UnauthorizedClubResponseModel response = step("Create new book club", () ->
                api.clubs.unauthorizedPostClub(requestData));

        step("Check error message", () ->
                assertThat(response.detail()).isEqualTo("Authentication credentials were not provided."));
    }

    @Test
    public void successfulDeleteClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ClubUpsertRequestModel requestData = new ClubUpsertRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        ClubUpsertResponseModel clubCreateResponse = step("Create new book club and check status (201)", () ->
                api.clubs.successfulPostClub(requestData, loginResponse.access()));

        step("Delete new book club and check status (204)", () ->
                api.clubs.successfulDeleteClub(clubCreateResponse.id(), loginResponse.access()));
    }

    @Test
    public void unauthorizedDeleteClubTest() {
        DeleteClubWithErrorResponseModel deleteResponse = step("Delete new book club", () ->
                api.clubs.unauthorizedDeleteClub(testData.randomBookClubId));

        step("Check error message", () ->
                assertThat(deleteResponse.detail()).isEqualTo("Authentication credentials were not provided."));
    }

    @Test
    public void deleteMissingClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        DeleteClubWithErrorResponseModel deleteClubResponse = step("Delete new book club", () ->
                api.clubs.deleteMissingClub(testData.randomWrongBookClubId, loginResponse.access()));

        step("Check error message", () ->
                assertThat(deleteClubResponse.detail()).isEqualTo("No Club matches the given query."));
    }

    @Test
    public void successfulPutClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ClubUpsertRequestModel requestData = new ClubUpsertRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        ClubUpsertResponseModel response = step("Send put request and check status (200)", () ->
                api.clubs.successfulPutClub(requestData, testData.clubId, loginResponse.access()));

        step("Check content of the fields", () -> {
            assertThat(response.bookTitle()).isEqualTo(testData.bookTitle);
            assertThat(response.bookAuthors()).isEqualTo(testData.bookAuthor);
            assertThat(response.publicationYear()).isEqualTo(testData.publicationYear);
            assertThat(response.description()).isEqualTo(testData.description);
            assertThat(response.telegramChatLink()).isEqualTo(testData.telegramChatLink);
        });
    }

    @Test
    public void emptyBookTitlePutClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        EmptyBookTitleRequestModel requestData = new EmptyBookTitleRequestModel(testData.bookAuthor,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        EmptyBookTitleResponseModel response = step("Send put request and check status (400)", () ->
                api.clubs.emptyBookTitlePutClub(requestData, testData.clubId, loginResponse.access()));

        step("Check content of the fields", () ->
            assertThat(response.bookTitle().getFirst()).isEqualTo("This field is required."));
    }

    @Test
    public void emptyBookAuthorPutClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        EmptyBookAuthorRequestModel requestData = new EmptyBookAuthorRequestModel(testData.bookTitle,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        EmptyBookAuthorResponseModel response = step("Send put request and check status (400)", () ->
                api.clubs.emptyBookAuthorPutClub(requestData, testData.clubId, loginResponse.access()));

        step("Check content of the fields", () ->
            assertThat(response.bookAuthors().getFirst()).isEqualTo("This field is required."));
    }

    @Test
    public void wrongPublicationYearPutClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        WrongPublicationYearRequestModel requestData = new WrongPublicationYearRequestModel(testData.bookTitle,
                testData.bookAuthor, testData.bookAuthor, testData.description, testData.telegramChatLink);

        WrongPublicationYearResponseModel response = step("Send put request and check status (400)", () ->
                api.clubs.wrongPublicationYearPutClub(requestData, testData.clubId, loginResponse.access()));

        step("Check error message", () ->
                assertThat(response.publicationYear().getFirst()).isEqualTo("A valid integer is required."));
    }

    @Test
    public void wrongTelegramChatLinkPutGlubsTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ClubUpsertRequestModel requestData = new ClubUpsertRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, testData.wrongTelegramChatLink);

        WrongTelegramChatLinkResponseModel response = step("Create new book club and check status code (400)", () ->
                api.clubs.wrongTelegramChatLinkPutClub(requestData, testData.clubId, loginResponse.access()));

        step("Check error message", () ->
                assertThat(response.telegramChatLink().getFirst()).isEqualTo("Enter a valid URL."));
    }

    @Test
    public void unauthorizedPutClubTest() {
        ClubUpsertRequestModel requestData = new ClubUpsertRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        UnauthorizedClubResponseModel unauthorizedPutResponse = step("Send put request and check status (401)", () ->
                api.clubs.unauthorizedPutClub(requestData, testData.clubId));

        step("Check error message", () ->
                assertThat(unauthorizedPutResponse.detail()).isEqualTo("Authentication credentials were not provided."));
    }
}
