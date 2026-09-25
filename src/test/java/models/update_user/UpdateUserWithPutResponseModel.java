package models.update_user;

public record UpdateUserWithPutResponseModel(Integer id, String username, String firstName,
                                             String lastName, String email, String remoteAddr) {
}
