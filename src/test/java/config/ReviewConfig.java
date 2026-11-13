package config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "system:properties",
        "classpath:local.properties"
})
public interface ReviewConfig extends Config {

    @Key("review.clubId")
    Integer clubId();

    @Key("review.missingId")
    Integer missingId();

    @Key("review.ownId")
    Integer ownId();

    @Key("review.foreignId")
    Integer foreignId();

    @Key("review.ownDeleteId")
    Integer ownDeleteId();

    @Key("review.foreignDeleteId")
    Integer foreignDeleteId();

    @Key("review.text")
    String text();

    @Key("review.ui.filter")
    String filter();

    @Key("review.ui.clubName")
    String clubName();

    @Key("review.ui.assessment")
    String assessment();

    @Key("review.ui.readPages")
    String readPages();

    @Key("review.error.unauthorized")
    String unauthorizedError();

    @Key("review.error.invalidToken")
    String invalidTokenError();

    @Key("review.error.tokenNotValid")
    String tokenNotValidCode();

    @Key("review.error.assessmentMax")
    String assessmentMaxError();

    @Key("review.error.forbidden")
    String forbiddenError();

    @Key("review.assessment.min")
    int assessmentMin();

    @Key("review.assessment.max")
    int assessmentMax();

    @Key("review.assessment.wrong.min")
    int wrongAssessmentMin();

    @Key("review.assessment.wrong.max")
    int wrongAssessmentMax();

    @Key("review.readPages.min")
    int readPagesMin();

    @Key("review.readPages.max")
    int readPagesMax();
}
