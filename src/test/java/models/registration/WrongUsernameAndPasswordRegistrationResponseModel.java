package models.registration;

import java.util.List;

public record WrongUsernameAndPasswordRegistrationResponseModel(List<String> username, List<String> password) {
}
