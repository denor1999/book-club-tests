package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "classpath:${env}.properties"
})
public interface UserConfig extends Config {

    @Key("username")
    String username();

    @Key("password")
    String password();

    @Key("wrong_password")
    String wrongPassword();

    @Key("expected_token_path")
    String expectedTokenPath();

    @Key("expected_detail_error")
    String expectedDetailError();

    @Key("expected_existing_user_error")
    String expectedExistingUserError();

    @Key("empty_credentials")
    String emptyCredentials();

    @Key("wrong_refresh_token")
    String wrongRefreshToken();

    @Key("wrong_access_token")
    String wrongAccessToken();
}
