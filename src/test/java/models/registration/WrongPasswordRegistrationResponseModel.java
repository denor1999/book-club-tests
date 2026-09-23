package models.registration;

import java.util.List;

public record WrongPasswordRegistrationResponseModel(List<String> password) {
}
