package models.clubs;

public record WrongPublicationYearRequestModel(String bookTitle,
                                               String bookAuthors,
                                               String publicationYear,
                                               String description,
                                               String telegramChatLink) {
}
