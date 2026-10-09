package models.clubs;

public record EmptyBookAuthorRequestModel(String bookTitle,
                                          Integer publicationYear,
                                          String description,
                                          String telegramChatLink) {
}
