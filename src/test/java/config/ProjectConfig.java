package config;

import org.aeonbits.owner.ConfigFactory;

public class ProjectConfig {

    public final AuthConfig auth = ConfigFactory.create(AuthConfig.class, System.getProperties());
    public final UpdateUserConfig updateUser = ConfigFactory.create(UpdateUserConfig.class, System.getProperties());
    public final ClubConfig club = ConfigFactory.create(ClubConfig.class, System.getProperties());
    public final ReviewConfig review = ConfigFactory.create(ReviewConfig.class, System.getProperties());
}
