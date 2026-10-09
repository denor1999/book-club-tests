package api;

import models.clubs.delete.DeleteClubWithErrorResponseModel;
import models.clubs.get.ClubResultsModel;
import models.clubs.ClubUpsertRequestModel;
import models.clubs.get.ClubsListResponseModel;
import models.clubs.ClubUpsertResponseModel;
import models.clubs.post.UnauthorizedPostClubResponseModel;
import models.clubs.post.WrongTelegramChatLinkResponseModel;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static specs.clubs.ClubsSpec.*;

public class ClubsApiClient {
    public ClubsListResponseModel successfulGetClubs() {
        int page = 1;
        int page_size = 1000;

        ClubsListResponseModel response = successfulGetClubs(page, page_size);
        int count = response.count();

        List<ClubResultsModel> allClubs = new ArrayList<>(response.results());

        while(response.next() != null) {
            page++;
            response = successfulGetClubs(page, page_size);
            allClubs.addAll(response.results());
        }

        return new ClubsListResponseModel(count, null, null, allClubs);
    }

    public ClubsListResponseModel successfulGetClubs(int page, int page_size) {
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

    public UnauthorizedPostClubResponseModel unauthorizedPostClub(ClubUpsertRequestModel postBody) {
        return given()
                .spec(clubsRequestSpec)
                .body(postBody)
                .when()
                .post("/clubs/")
                .then()
                .spec(unauthorizedPostClubsPostSpec)
                .extract()
                .as(UnauthorizedPostClubResponseModel.class);
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
                .spec(putClubSpec)
                .extract()
                .as(ClubUpsertResponseModel.class);
    }
}
