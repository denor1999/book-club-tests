package testdata;

import net.datafaker.Faker;

public class TestData {

    private final Faker faker = new Faker();

    public final String username = "qaguru";
    public final String password = "qaguru123";
    public final String wrongPassword = "qaguru1234";
    public final String expectedTokenPath = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public final String expectedDetailError = "Invalid username or password.";
    public final String expectedExistingUserError = "A user with that username already exists.";
    public final String randomUsername = faker.name().firstName() + "_" + System.currentTimeMillis();
    public final String randomPassword = faker.name().lastName();
    public final String wrongRegistrationUsername = faker.name().firstName() + "[]";
    public final String emptyUsername = " ";
    public final String emptyPassword = " ";
}
