package tests;

import api.ApiClient;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import pages.RegistrationPage;
import testdata.TestData;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    protected final TestData testData = new TestData();
    protected final ApiClient api = new ApiClient();
    protected final RegistrationPage registrationPage = new RegistrationPage();

    @BeforeAll
    public static void setup() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("browser_version");
        Configuration.browserSize = System.getProperty("browser_size", "1920x1080");
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        Configuration.pageLoadStrategy = "eager";
        Configuration.baseUrl = "https://book-club.qa.guru";
        RestAssured.baseURI = "https://book-club.qa.guru";

        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (Configuration.browser.equals("chrome")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments(List.of("--disable-dev-shm-usage", "--no-sandbox"));
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        } else if (Configuration.browser.equals("firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setCapability("webSocketUrl", false);
            capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
        } else {
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments(List.of("--disable-dev-shm-usage", "--no-sandbox"));
            capabilities.setCapability(EdgeOptions.CAPABILITY, edgeOptions);
        }

        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;
        Configuration.remote = System.getProperty("remote_url");
    }

    @AfterEach
    void tearsDown() {
        closeWebDriver();
    }
}
