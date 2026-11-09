package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "classpath:${env}.properties"
})
public interface ReviewConfig extends Config {

    @Key("review_club_id")
    Integer clubId();

    @Key("review_missing_id")
    Integer missingReviewId();

    @Key("own_review_id")
    Integer ownReviewId();

    @Key("foreign_review_id")
    Integer foreignReviewId();

    @Key("own_review_delete_id")
    Integer ownReviewDeleteId();

    @Key("foreign_review_delete_id")
    Integer foreignReviewDeleteId();

    @Key("review_text")
    String reviewText();

    @Key("unauthorized_detail")
    String unauthorizedDetail();

    @Key("wrong_token_detail")
    String wrongTokenDetail();

    @Key("wrong_token_code")
    String wrongTokenCode();

    @Key("wrong_assessment_error")
    String wrongAssessmentError();

    @Key("forbidden_detail")
    String forbiddenDetail();
}
