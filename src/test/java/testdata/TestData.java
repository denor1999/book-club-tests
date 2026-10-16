package testdata;

import net.datafaker.Faker;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestData {

    private static final Properties PROPERTIES = loadProperties();
    private final Faker faker = new Faker();

    public final String loginUsername = get("login.username");
    public final String loginPassword = get("login.password");
    public final String wrongLoginPassword = get("login.wrongPassword");
    public final String expectedTokenPath = get("login.expectedTokenPath");
    public final String expectedDetailError = get("login.expectedDetailError");
    public final String expectedExistingUserError = get("registration.expectedExistingUserError");
    public final String randomRegistrationUsername = faker.name().firstName() + "_" + System.currentTimeMillis();
    public final String randomRegistrationPassword = faker.name().lastName();
    public final String wrongRegistrationUsername = faker.name().firstName() + "[]";
    public final String emptyUsername = get("credentials.empty");
    public final String emptyPassword = get("credentials.empty");
    public final String wrongRefreshToken = get("login.wrongRefreshToken");

    public final String updateUsername = get("update.username");
    public final String updateFirstName = get("update.firstName");
    public final String updateLastName = get("update.lastName");
    public final String updateEmail = get("update.email");

    public final String bookTitle = faker.name().title();
    public final String bookAuthor = faker.name().fullName();
    public final int publicationYear = faker.number().numberBetween(
            getInt("book.publicationYear.min"),
            getInt("book.publicationYear.max")
    );
    public final String description = faker.name().nameWithMiddle();
    public final String telegramChatLink = get("telegram.chatLink.prefix") + faker.name().firstName();
    public final String wrongTelegramChatLink = get("telegram.chatLink.wrong");

    public final Integer randomWrongBookClubId = faker.number().numberBetween(
            getInt("club.randomWrongId.min"),
            getInt("club.randomWrongId.max")
    );
    public final Integer randomBookClubId = faker.number().numberBetween(
            getInt("club.randomId.min"),
            getInt("club.randomId.max")
    );

    public final Integer clubId = getInt("club.id");

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = TestData.class.getClassLoader().getResourceAsStream("test-data.properties")) {
            if (input == null) {
                throw new IllegalStateException("test-data.properties not found on classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load test-data.properties", e);
        }
        return properties;
    }

    private static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("Missing property: " + key);
        }
        return value;
    }

    private static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
