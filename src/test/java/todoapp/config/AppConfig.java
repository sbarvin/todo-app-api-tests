package todoapp.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "classpath:app.properties"
})
public interface AppConfig extends Config {

    @Key("service.api.baseUrl")
    String baseUrl();

    @Key("service.api.username")
    String username();

    @Key("service.api.password")
    String password();

    @Key("service.api.wsUrl")
    String wsUrl();
}
