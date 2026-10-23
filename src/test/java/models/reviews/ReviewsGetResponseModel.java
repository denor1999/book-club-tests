package models.reviews;

import java.util.List;

public record ReviewsGetResponseModel(Integer count,
                                      String next,
                                      String previous,
                                      List<ReviewsListModel> results) {
}
