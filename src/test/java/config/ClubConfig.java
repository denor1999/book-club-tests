package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "classpath:${env}.properties"
})
public interface ClubConfig extends Config {

    @Key("publication_year_min")
    int publicationYearMin();

    @Key("publication_year_max")
    int publicationYearMax();

    @Key("wrong_publication_year")
    String wrongPublicationYear();

    @Key("telegram_chat_link_prefix")
    String telegramChatLinkPrefix();

    @Key("wrong_telegram_chat_link")
    String wrongTelegramChatLink();

    @Key("club_id")
    Integer clubId();

    @Key("club_random_id_min")
    int randomIdMin();

    @Key("club_random_id_max")
    int randomIdMax();

    @Key("club_random_wrong_id_min")
    int randomWrongIdMin();

    @Key("club_random_wrong_id_max")
    int randomWrongIdMax();
}
