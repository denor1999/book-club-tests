package models.clubs.get;

import java.util.List;

public record ClubsListResponseModel(Integer count,
                                     String next,
                                     String previous,
                                     List<ClubResultsModel> results) {
}
