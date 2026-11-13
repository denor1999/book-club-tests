package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:local.properties"
})
public interface UpdateUserConfig extends Config {

    @Key("update.username")
    String username();

    @Key("update.firstName")
    String firstName();

    @Key("update.lastName")
    String lastName();

    @Key("update.email")
    String email();
}
