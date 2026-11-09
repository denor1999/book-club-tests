package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "classpath:${env}.properties"
})
public interface UpdateUserConfig extends Config {

    @Key("update_username")
    String username();

    @Key("update_first_name")
    String firstName();

    @Key("update_last_name")
    String lastName();

    @Key("update_email")
    String email();
}
