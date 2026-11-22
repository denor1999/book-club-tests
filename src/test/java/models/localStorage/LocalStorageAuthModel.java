package models.localStorage;

public record LocalStorageAuthModel(UserLocalStorageModel user,
                                    String accessToken,
                                    String refreshToken,
                                    Boolean isAuthenticated) {
}
