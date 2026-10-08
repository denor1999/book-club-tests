package api;

import models.clubs.delete.DeleteClubWithErrorResponseModel;
import models.clubs.post.ClubPostRequestModel;
import models.clubs.get.ClubsListResponseModel;
import models.clubs.post.ClubPostResponseModel;
import models.clubs.post.UnauthorizedPostClubResponseModel;
import models.clubs.post.WrongTelegramChatLinkResponseModel;

import static io.restassured.RestAssured.given;
import static specs.clubs.ClubsSpec.*;

public class ClubsApiClient {

    public ClubsListResponseModel getClubs() {
        return given()
                .spec(clubsRequestSpec)
                .when()
                .get("/clubs/")
                .then()
                .spec(successfulClubsGetResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    public ClubPostResponseModel postClub(ClubPostRequestModel postBody, String accessToken) {
        return given()
                .spec(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(postBody)
                .when()
                .post("/clubs/")
                .then()
                .spec(successfulPostClubsPostSpec)
                .extract()
                .as(ClubPostResponseModel.class);
    }

    public WrongTelegramChatLinkResponseModel wrongTelegramChatLinkPostClub(ClubPostRequestModel postBody, String accessToken) {
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

    public UnauthorizedPostClubResponseModel unauthorizedPostClub(ClubPostRequestModel postBody) {
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
}
