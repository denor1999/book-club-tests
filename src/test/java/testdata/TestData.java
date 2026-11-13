package testdata;

import config.ProjectConfig;
import net.datafaker.Faker;

public class TestData {

    private final Faker faker = new Faker();
    private final ProjectConfig config = new ProjectConfig();

    public final String randomRegistrationUsername = faker.name().firstName() + "_" + System.currentTimeMillis();
    public final String randomRegistrationPassword = faker.name().lastName();
    public final String wrongRegistrationUsername = faker.name().firstName() + "[]";

    public final String bookTitle = faker.name().title();
    public final String bookAuthor = faker.name().fullName();
    public final int publicationYear = faker.number().numberBetween(
            config.club.publicationYearMin(),
            config.club.publicationYearMax()
    );
    public final String description = faker.name().nameWithMiddle();
    public final String telegramChatLink = config.club.telegramChatLinkPrefix() + faker.name().firstName();

    public final Integer randomWrongBookClubId = faker.number().numberBetween(
            config.club.randomWrongIdMin(),
            config.club.randomWrongIdMax()
    );
    public final Integer randomBookClubId = faker.number().numberBetween(
            config.club.randomIdMin(),
            config.club.randomIdMax()
    );

    public final Integer randomAssessment = faker.number().numberBetween(
            config.review.assessmentMin(),
            config.review.assessmentMax()
    );
    public final Integer wrongRandomAssessment = faker.number().numberBetween(
            config.review.wrongAssessmentMin(),
            config.review.wrongAssessmentMax()
    );
    public final Integer readPages = faker.number().numberBetween(
            config.review.readPagesMin(),
            config.review.readPagesMax()
    );
}
