package models.registration;

import java.util.List;

public record WrongCredentialsResponseModel(List<String> username, List<String> password) {
}
