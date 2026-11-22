package models.reviews;

public record ReviewRequestModel(Integer club,
                                 String review,
                                 Integer assessment,
                                 Integer readPages) {

    public ReviewRequestModel withClub(Integer clubId) {
        return new ReviewRequestModel(clubId, review, assessment, readPages);
    }
}
