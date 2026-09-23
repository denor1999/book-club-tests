package models.registration;

public record RegistrationResponseSuccessfulModel(Integer id, String username, String firstName,
                                                  String lastName, String email, String remoteAddr) {
}
