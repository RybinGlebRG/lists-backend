package ru.rerumu.lists.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.aspects.Loggable;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.postgresql.PostgreSQLContainer;
import ru.rerumu.lists.controller.series.views.out.SeriesListView;
import ru.rerumu.lists.controller.series.views.out.SeriesView;
import ru.rerumu.lists.crosscut.Profiles;


@ActiveProfiles(Profiles.TEST)
public class ITBase {

    protected static PostgreSQLContainer postgres;

    static {
        postgres = new PostgreSQLContainer("postgres:16-alpine");
        postgres.start();
    }

    @Autowired
    private ObjectMapper objectMapper;

    protected static void cleanSQL() {
        JdbcDatabaseDelegate databaseDelegate = new JdbcDatabaseDelegate(postgres, "");
        ScriptUtils.runInitScript(databaseDelegate, "ru/rerumu/lists/integration/clean.sql");
    }

    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    protected SeriesListView getSeriesList() throws Exception{
        String body = RestAssuredMockMvc
                .given()
                    .header("Accept-Type", "application/json")
                    .attribute("authUserId", 0L)
                    .log().all()
                .when()
                    .get("/api/v1/users/0/series")
                .then()
                    .log().all()
                    .statusCode(200)
                .extract().body().asString();

        return objectMapper.readValue(body, SeriesListView.class);
    }

    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    protected SeriesView getSeriesByTitle(@NonNull String title) throws Exception{
        SeriesListView seriesListView = getSeriesList();

        return seriesListView.items().stream()
                .filter(item -> item.title().equals(title))
                .findAny()
                .orElse(null);
    }

}
