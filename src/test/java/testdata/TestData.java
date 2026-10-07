package testdata;

import net.datafaker.Faker;

public class TestData {

    private final Faker faker = new Faker();

    public final String username = "denor1999";
    public final String password = "_lyzhnik0_";
    public final String wrongPassword = "qaguru1234";
    public final String expectedTokenPath = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public final String expectedDetailError = "Invalid username or password.";
    public final String expectedExistingUserError = "A user with that username already exists.";
    public final String randomUsername = faker.name().firstName() + "_" + System.currentTimeMillis();
    public final String randomPassword = faker.name().lastName();
    public final String wrongRegistrationUsername = faker.name().firstName() + "[]";
    public final String emptyUsername = " ";
    public final String emptyPassword = " ";
    public final String wrongRefreshToken = "piawenvioawre";

    public final String updateUsername = "denor1999";
    public final String updateFirstName = "Ivan";
    public final String updateLastName = "Ozhgikhin";
    public final String updateEmail = "ozhgixinv@list.ru";

    public final String bookTitle = faker.name().title();
    public final String bookAuthor = faker.name().fullName();
    public final int publicationYear = faker.number().numberBetween(1900, 2023);
    public final String description = faker.name().nameWithMiddle();
    public final String telegramChatLink = "https://book-club.qa.guru";
    public final String wrongTelegramChatLink = "some uri";
}
