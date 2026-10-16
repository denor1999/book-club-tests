package tests;

import api.ApiClient;
import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import testdata.TestData;

import static com.codeborne.selenide.Selenide.webdriver;

public class TestBase {

    protected final TestData testData = new TestData();
    protected final ApiClient api = new ApiClient();

    @BeforeAll
    public static void setup() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "eager";
        Configuration.baseUrl = "https://book-club.qa.guru";
        RestAssured.baseURI = "https://book-club.qa.guru";
    }

    @AfterEach
    void tearsDown() {
        webdriver().driver().close();
    }
}
