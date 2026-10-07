package models.clubs.get;

import models.clubs.ClubReviewsModel;

import java.util.List;

public record ClubResultsModel(Integer id,
                               String bookTitle,
                               String bookAuthors,
                               Integer publicationYear,
                               String description,
                               String telegramChatLink,
                               Integer owner,
                               List<Integer> members,
                               List<ClubReviewsModel> reviews,
                               String created,
                               String modified) {
}
