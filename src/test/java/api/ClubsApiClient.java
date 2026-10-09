package api;

import models.clubs.*;
import models.clubs.delete.DeleteClubWithErrorResponseModel;
import models.clubs.get.ClubResultsModel;
import models.clubs.get.ClubsListResponseModel;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static specs.clubs.ClubsSpec.*;

public class ClubsApiClient {
    public ClubsListResponseModel getClubs() {
        int page = 1;
        int page_size = 1000;

        ClubsListResponseModel response = getClubs(page, page_size);
        int count = response.count();

        List<ClubResultsModel> allClubs = new ArrayList<>(response.results());

        while(response.next() != null) {
            page++;
            response = getClubs(page, page_size);
            allClubs.addAll(response.results());
        }

        return new ClubsListResponseModel(count, null, null, allClubs);
    }

    public ClubsListResponseModel getClubs(int page, int page_size) {
        String pathFormatted =  String.format("/clubs/?page=%d&page_size=%d", page, page_size);

        return given()
                .spec(clubsRequestSpec)
                .when()
                .get(pathFormatted)
                .then()
                .spec(successfulClubsGetResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    public ClubUpsertResponseModel successfulPostClub(ClubUpsertRequestModel postBody, String accessToken) {
        return given()
                .spec(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(postBody)
                .when()
                .post("/clubs/")
                .then()
                .spec(successfulPostClubsPostSpec)
                .extract()
                .as(ClubUpsertResponseModel.class);
    }

    public WrongTelegramChatLinkResponseModel wrongTelegramChatLinkPostClub(ClubUpsertRequestModel postBody, String accessToken) {
        return given()
                .spec(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(postBody)
                .when()
                .post("/clubs/")
                .then()
                .spec(wrongTelegramChatLinkPostClubsPostSpec)
                .extract()
                .as(WrongTelegramChatLinkResponseModel.class);
    }

    public UnauthorizedClubResponseModel unauthorizedPostClub(ClubUpsertRequestModel postBody) {
        return given()
                .spec(clubsRequestSpec)
                .body(postBody)
                .when()
                .post("/clubs/")
                .then()
                .spec(unauthorizedClubsSpec)
                .extract()
                .as(UnauthorizedClubResponseModel.class);
    }

    public void successfulDeleteClub(Integer id, String accessToken) {
        given()
                .spec(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("/clubs/" + id + "/")
                .then()
                .spec(successfulDeleteClubSpec)
                .extract().asString();
    }

    public DeleteClubWithErrorResponseModel unauthorizedDeleteClub(Integer id) {
        return given()
                .spec(clubsRequestSpec)
                .when()
                .delete("/clubs/" + id + "/")
                .then()
                .spec(unauthorizedDeleteClubSpec)
                .extract()
                .as(DeleteClubWithErrorResponseModel.class);
    }

    public DeleteClubWithErrorResponseModel deleteMissingClub(Integer id, String accessToken) {
        return given()
                .spec(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("/clubs/" + id + "/")
                .then()
                .spec(deleteMissingClubSpec)
                .extract()
                .as(DeleteClubWithErrorResponseModel.class);
    }

    public ClubUpsertResponseModel successfulPutClub(ClubUpsertRequestModel putBody, Integer id, String accessToken) {
        return given()
                .spec(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(putBody)
                .when()
                .put("/clubs/" + id + "/")
                .then()
                .spec(successfulPutClubSpec)
                .extract()
                .as(ClubUpsertResponseModel.class);
    }

    public WrongTelegramChatLinkResponseModel wrongTelegramChatLinkPutClub(ClubUpsertRequestModel postBody, Integer id, String accessToken) {
        return given()
                .spec(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(postBody)
                .when()
                .put("/clubs/" + id + "/")
                .then()
                .spec(wrongTelegramChatLinkPostClubsPostSpec)
                .extract()
                .as(WrongTelegramChatLinkResponseModel.class);
    }

    public UnauthorizedClubResponseModel unauthorizedPutClub(ClubUpsertRequestModel putBody, Integer id) {
        return given()
                .spec(clubsRequestSpec)
                .body(putBody)
                .when()
                .put("/clubs/" + id + "/")
                .then()
                .spec(unauthorizedClubsSpec)
                .extract()
                .as(UnauthorizedClubResponseModel.class);
    }

    public EmptyBookTitleResponseModel emptyBookTitlePutClub(EmptyBookTitleRequestModel putBody, Integer id, String accessToken) {
        return given()
                .spec(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(putBody)
                .when()
                .put("/clubs/" + id + "/")
                .then()
                .spec(emptyBookTitlePutClubSpec)
                .extract()
                .as(EmptyBookTitleResponseModel.class);
    }

    public EmptyBookAuthorResponseModel emptyBookAuthorPutClub(EmptyBookAuthorRequestModel putBody, Integer id, String accessToken) {
        return given()
                .spec(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(putBody)
                .when()
                .put("/clubs/" + id + "/")
                .then()
                .spec(emptyBookAuthorPutClubSpec)
                .extract()
                .as(EmptyBookAuthorResponseModel.class);
    }

    public WrongPublicationYearResponseModel wrongPublicationYearPutClub(WrongPublicationYearRequestModel putBody, Integer id, String accessToken) {
        return given()
                .spec(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(putBody)
                .when()
                .put("/clubs/" + id + "/")
                .then()
                .spec(wrongPublicationYearPutClubSpec)
                .extract()
                .as(WrongPublicationYearResponseModel.class);
    }

}
