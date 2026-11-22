package models.clubs;

public record EmptyBookTitleRequestModel(String bookAuthors,
                                         Integer publicationYear,
                                         String description,
                                         String telegramChatLink) {
}
