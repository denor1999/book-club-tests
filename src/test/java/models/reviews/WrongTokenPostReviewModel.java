package models.reviews;

import java.util.List;

public record WrongTokenPostReviewModel(String detail,
                                        String code,
                                        List<WrongTokenMessagesReviewModel> messages) {
}
