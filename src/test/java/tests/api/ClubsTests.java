package tests.api;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.clubs.*;
import models.clubs.delete.DeleteClubWithErrorResponseModel;
import models.clubs.get.ClubResultsModel;
import models.clubs.get.ClubsListResponseModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("Interaction with clubs API tests")
@Tags({@Tag("api"), @Tag("clubs")})
public class ClubsTests extends TestBase {

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check successful get clubs response")
    @Description("The server returns 200. The body is not null, count is greater than or equal to 0, and results is present.")
    public void successfulGetClubsTest() {
        ClubsListResponseModel getClubsResponse = step("Send get clubs request and check status (200)", () ->
                api.clubs.getClubs());

        step("Check clubs response fields", () -> {
            assertThat(getClubsResponse).isNotNull();
            assertThat(getClubsResponse.count()).isGreaterThanOrEqualTo(0);
            assertThat(getClubsResponse.results()).isNotNull();
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check clubs count matches results size")
    @Description("The server returns 200. The size of results matches count.")
    public void getClubsCountMatchesResultsSizeTest() {
        ClubsListResponseModel getClubsResponse = step("Send get clubs request and check status (200)", () ->
                api.clubs.getClubs());

        step("Check clubs count matches results size", () ->
                assertThat(getClubsResponse.results()).hasSize(getClubsResponse.count()));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check each club has required fields")
    @Description("Each club has id, title, authors, year, description, Telegram link, owner, members, reviews, and created date. id and owner are positive.")
    public void getClubsEachClubHasRequiredFields() {
        ClubsListResponseModel response = step("Send get clubs request and check status (200)", () ->
                api.clubs.getClubs());

        step("Check each club has required fields", () -> {
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
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Check successful post club response")
    @Description("An authorized club creation returns book title, authors, publication year, description, and Telegram link matching the submitted values.")
    public void successfulPostClubsTest() {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

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
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check post club attempt with invalid telegram chat link")
    @Description("Creating a club with an invalid Telegram link returns \"Enter a valid URL.\"")
    public void wrongTelegramChatLinkPostClubsTest() {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ClubUpsertRequestModel requestData = new ClubUpsertRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, config.club.wrongTelegramChatLink());

        WrongTelegramChatLinkResponseModel response = step("Create new book club and check status code", () ->
                api.clubs.wrongTelegramChatLinkPostClub(requestData, loginResponse.access()));

        step("Check error message", () ->
                assertThat(response.telegramChatLink().getFirst()).isEqualTo("Enter a valid URL."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check post club attempt when user unauthorized")
    @Description("Creating a club without authorization returns \"Authentication credentials were not provided.\"")
    public void unauthorizedPostClubsTest() {
        ClubUpsertRequestModel requestData = new ClubUpsertRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        UnauthorizedClubResponseModel response = step("Create new book club", () ->
                api.clubs.unauthorizedPostClub(requestData));

        step("Check error message", () ->
                assertThat(response.detail()).isEqualTo("Authentication credentials were not provided."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Check successful delete club response")
    @Description("After a club is created, an authorized delete returns 204.")
    public void successfulDeleteClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

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
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check delete club attempt when user unauthorized")
    @Description("Deleting a club without authorization returns \"Authentication credentials were not provided.\"")
    public void unauthorizedDeleteClubTest() {
        DeleteClubWithErrorResponseModel deleteResponse = step("Delete new book club", () ->
                api.clubs.unauthorizedDeleteClub(testData.randomBookClubId));

        step("Check error message", () ->
                assertThat(deleteResponse.detail()).isEqualTo("Authentication credentials were not provided."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Check delete attempt for missing club")
    @Description("Deleting a missing club returns \"No Club matches the given query.\"")
    public void deleteMissingClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        DeleteClubWithErrorResponseModel deleteClubResponse = step("Delete new book club", () ->
                api.clubs.deleteMissingClub(testData.randomWrongBookClubId, loginResponse.access()));

        step("Check error message", () ->
                assertThat(deleteClubResponse.detail()).isEqualTo("No Club matches the given query."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Check successful put club request status")
    @Description("An authorized PUT returns 200. Title, authors, year, description, and Telegram link match the submitted values.")
    public void successfulPutClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ClubUpsertRequestModel requestData = new ClubUpsertRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        ClubUpsertResponseModel response = step("Send put request and check status (200)", () ->
                api.clubs.successfulPutClub(requestData, config.club.id(), loginResponse.access()));

        step("Check content of the fields", () -> {
            assertThat(response.bookTitle()).isEqualTo(testData.bookTitle);
            assertThat(response.bookAuthors()).isEqualTo(testData.bookAuthor);
            assertThat(response.publicationYear()).isEqualTo(testData.publicationYear);
            assertThat(response.description()).isEqualTo(testData.description);
            assertThat(response.telegramChatLink()).isEqualTo(testData.telegramChatLink);
        });
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Check put club attempt with empty book title")
    @Description("A PUT without a book title returns 400 and \"This field is required.\" for bookTitle.")
    public void emptyBookTitlePutClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        EmptyBookTitleRequestModel requestData = new EmptyBookTitleRequestModel(testData.bookAuthor,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        EmptyBookTitleResponseModel response = step("Send put request and check status (400)", () ->
                api.clubs.emptyBookTitlePutClub(requestData, config.club.id(), loginResponse.access()));

        step("Check content of the fields", () ->
            assertThat(response.bookTitle().getFirst()).isEqualTo("This field is required."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Check put club attempt with empty book author")
    @Description("A PUT without an author returns 400 and \"This field is required.\" for bookAuthors.")
    public void emptyBookAuthorPutClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        EmptyBookAuthorRequestModel requestData = new EmptyBookAuthorRequestModel(testData.bookTitle,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        EmptyBookAuthorResponseModel response = step("Send put request and check status (400)", () ->
                api.clubs.emptyBookAuthorPutClub(requestData, config.club.id(), loginResponse.access()));

        step("Check content of the fields", () ->
            assertThat(response.bookAuthors().getFirst()).isEqualTo("This field is required."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check put club attempt with wrong publication year")
    @Description("A PUT with a non-numeric publication year returns 400 and \"A valid integer is required.\"")
    public void wrongPublicationYearPutClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        WrongPublicationYearRequestModel requestData = new WrongPublicationYearRequestModel(testData.bookTitle,
                testData.bookAuthor, config.club.wrongPublicationYear(), testData.description, testData.telegramChatLink);

        WrongPublicationYearResponseModel response = step("Send put request and check status (400)", () ->
                api.clubs.wrongPublicationYearPutClub(requestData, config.club.id(), loginResponse.access()));

        step("Check error message", () ->
                assertThat(response.publicationYear().getFirst()).isEqualTo("A valid integer is required."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check put club attempt with invalid telegram chat link")
    @Description("A PUT with an invalid Telegram link returns 400 and \"Enter a valid URL.\"")
    public void wrongTelegramChatLinkPutClubsTest() {
        LoginBodyModel loginData = new LoginBodyModel(config.auth.username(), config.auth.password());

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        ClubUpsertRequestModel requestData = new ClubUpsertRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, config.club.wrongTelegramChatLink());

        WrongTelegramChatLinkResponseModel response = step("Create new book club and check status code (400)", () ->
                api.clubs.wrongTelegramChatLinkPutClub(requestData, config.club.id(), loginResponse.access()));

        step("Check error message", () ->
                assertThat(response.telegramChatLink().getFirst()).isEqualTo("Enter a valid URL."));
    }

    @Test
    @Owner("denor1999")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Check put club attempt when user unauthorized")
    @Description("A PUT without authorization returns 401 and \"Authentication credentials were not provided.\"")
    public void unauthorizedPutClubTest() {
        ClubUpsertRequestModel requestData = new ClubUpsertRequestModel(testData.bookTitle, testData.bookAuthor,
                testData.publicationYear, testData.description, testData.telegramChatLink);

        UnauthorizedClubResponseModel unauthorizedPutResponse = step("Send put request and check status (401)", () ->
                api.clubs.unauthorizedPutClub(requestData, config.club.id()));

        step("Check error message", () ->
                assertThat(unauthorizedPutResponse.detail()).isEqualTo("Authentication credentials were not provided."));
    }
}
