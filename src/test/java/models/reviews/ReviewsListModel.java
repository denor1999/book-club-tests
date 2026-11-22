package models.reviews;

public record ReviewsListModel(Integer id,
                               Integer club,
                               UserReviewModel user,
                               String review,
                               Integer assessment,
                               Integer readPages,
                               String created,
                               String modified) {
}
