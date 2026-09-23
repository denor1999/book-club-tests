package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import testdata.TestData;

public class TestBase {

    protected final TestData testData = new TestData();

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://book-club.qa.guru";
    }
}
