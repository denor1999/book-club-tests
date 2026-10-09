package models.clubs;

public record ClubUpsertRequestModel(String bookTitle,
                                     String bookAuthors,
                                     Integer publicationYear,
                                     String description,
                                     String telegramChatLink) {
}
