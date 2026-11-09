package testdata;

import config.ClubConfig;
import config.ReviewConfig;
import config.UpdateUserConfig;
import config.UserConfig;
import net.datafaker.Faker;
import org.aeonbits.owner.ConfigFactory;

public class TestData {

    private static final UserConfig userConfig;
    private static final UpdateUserConfig updateUserConfig;
    private static final ClubConfig clubConfig;
    private static final ReviewConfig reviewConfig;
    private final Faker faker = new Faker();

    static {
        if (System.getProperty("env") == null) {
            System.setProperty("env", "local");
        }
        userConfig = ConfigFactory.create(UserConfig.class);
        updateUserConfig = ConfigFactory.create(UpdateUserConfig.class);
        clubConfig = ConfigFactory.create(ClubConfig.class);
        reviewConfig = ConfigFactory.create(ReviewConfig.class);
    }

    public final String loginUsername = userConfig.username();
    public final String loginPassword = userConfig.password();
    public final String wrongLoginPassword = userConfig.wrongPassword();
    public final String expectedTokenPath = userConfig.expectedTokenPath();
    public final String expectedDetailError = userConfig.expectedDetailError();
    public final String expectedExistingUserError = userConfig.expectedExistingUserError();
    public final String randomRegistrationUsername = faker.name().firstName() + "_" + System.currentTimeMillis();
    public final String randomRegistrationPassword = faker.name().lastName();
    public final String wrongRegistrationUsername = faker.name().firstName() + "[]";
    public final String emptyUsername = userConfig.emptyCredentials();
    public final String emptyPassword = userConfig.emptyCredentials();
    public final String wrongRefreshToken = userConfig.wrongRefreshToken();
    public final String wrongAccessToken = userConfig.wrongAccessToken();

    public final String updateUsername = updateUserConfig.username();
    public final String updateFirstName = updateUserConfig.firstName();
    public final String updateLastName = updateUserConfig.lastName();
    public final String updateEmail = updateUserConfig.email();

    public final String bookTitle = faker.name().title();
    public final String bookAuthor = faker.name().fullName();
    public final int publicationYear = faker.number().numberBetween(
            clubConfig.publicationYearMin(),
            clubConfig.publicationYearMax()
    );
    public final String wrongPublicationYear = clubConfig.wrongPublicationYear();
    public final String description = faker.name().nameWithMiddle();
    public final String telegramChatLink = clubConfig.telegramChatLinkPrefix() + faker.name().firstName();
    public final String wrongTelegramChatLink = clubConfig.wrongTelegramChatLink();

    public final Integer randomWrongBookClubId = faker.number().numberBetween(
            clubConfig.randomWrongIdMin(),
            clubConfig.randomWrongIdMax()
    );
    public final Integer randomBookClubId = faker.number().numberBetween(
            clubConfig.randomIdMin(),
            clubConfig.randomIdMax()
    );

    public final Integer clubId = clubConfig.clubId();
    public final Integer reviewClubId = reviewConfig.clubId();
    public final Integer missingReviewId = reviewConfig.missingReviewId();
    public final Integer ownReviewId = reviewConfig.ownReviewId();
    public final Integer foreignReviewId = reviewConfig.foreignReviewId();
    public final Integer ownReviewDeleteId = reviewConfig.ownReviewDeleteId();
    public final Integer foreignReviewDeleteId = reviewConfig.foreignReviewDeleteId();
    public final String reviewText = reviewConfig.reviewText();
    public final String unauthorizedDetail = reviewConfig.unauthorizedDetail();
    public final String wrongTokenDetail = reviewConfig.wrongTokenDetail();
    public final String wrongTokenCode = reviewConfig.wrongTokenCode();
    public final String wrongAssessmentError = reviewConfig.wrongAssessmentError();
    public final String forbiddenDetail = reviewConfig.forbiddenDetail();

    public final Integer randomAssessment = faker.number().numberBetween(1, 5);
    public final Integer wrongRandomAssessment = faker.number().numberBetween(6, 60);
    public final Integer readPages = faker.number().numberBetween(0, 100000);
}
