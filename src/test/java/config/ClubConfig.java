package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:local.properties"
})
public interface ClubConfig extends Config {

    @Key("book.publicationYear.min")
    int publicationYearMin();

    @Key("book.publicationYear.max")
    int publicationYearMax();

    @Key("book.publicationYear.wrong")
    String wrongPublicationYear();

    @Key("telegram.chatLink.prefix")
    String telegramChatLinkPrefix();

    @Key("telegram.chatLink.wrong")
    String wrongTelegramChatLink();

    @Key("club.id")
    Integer id();

    @Key("club.randomId.min")
    int randomIdMin();

    @Key("club.randomId.max")
    int randomIdMax();

    @Key("club.randomWrongId.min")
    int randomWrongIdMin();

    @Key("club.randomWrongId.max")
    int randomWrongIdMax();
}
