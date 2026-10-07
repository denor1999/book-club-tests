package models.clubs.post;

public record ClubPostRequestModel(String bookTitle,
                                   String bookAuthors,
                                   Integer publicationYear,
                                   String description,
                                   String telegramChatLink) {
}
