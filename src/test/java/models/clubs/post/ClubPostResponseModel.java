package models.clubs.post;

import models.clubs.ClubReviewsEmptyModel;

import java.util.List;

public record ClubPostResponseModel(Integer id,
                                    String bookTitle,
                                    String bookAuthors,
                                    Integer publicationYear,
                                    String description,
                                    String telegramChatLink,
                                    Integer owner,
                                    List<Integer> members,
                                    List<ClubReviewsEmptyModel> reviews,
                                    String created,
                                    String modified) {
}
