package config;

import org.aeonbits.owner.Config;
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:local.properties"
})
public interface AuthConfig extends Config {

    @Key("login.username")
    String username();

    @Key("login.password")
    String password();

    @Key("login.wrongPassword")
    String wrongPassword();

    @Key("login.expectedTokenPath")
    String expectedTokenPath();

    @Key("login.expectedDetailError")
    String expectedDetailError();

    @Key("login.wrongRefreshToken")
    String wrongRefreshToken();

    @Key("login.wrongAccessToken")
    String wrongAccessToken();

    @Key("registration.expectedExistingUserError")
    String expectedExistingUserError();

    @Key("credentials.empty")
    String emptyCredentials();
}
