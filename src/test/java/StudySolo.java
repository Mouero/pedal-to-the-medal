import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import petstore.model.User;

import static io.qameta.allure.Allure.step;


@Feature("User")
public class StudySolo {
    private static final String URL = "http://localhost";
    private static final int PORT = 8080;
    private static final String BASE_PATH = "/api/v3";
    private final RestTemplate restTemplate = new RestTemplate();
    private String uriGet;
    private HttpHeaders headers = new HttpHeaders();
    private User userRequest;

    @SneakyThrows
    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {

        UriComponentsBuilder
                baseUri =
                step("Создание базового URL", () ->
                        UriComponentsBuilder.fromHttpUrl(URL).port(PORT).path(BASE_PATH));
        String uriPost =
                step("Создание URI для запроса Post/user", () ->
                        baseUri.build() + "/user");
        uriGet =
                step("Создание URI для запроса Get/user/Mouero", () ->
                        baseUri.build() + "/user/Mouero");

        headers = new HttpHeaders();
        step("Создание хедеров", () -> {
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        });
        userRequest = new User();
        step("Заполнение модели User данными", () ->
                userRequest
                        .id(926L)
                        .username("Mouero")
                        .firstName("Anton")
                        .lastName("Dubovyy")
                        .email("123@yandex.ru")
                        .password("12093487565")
                        .phone("8 999 452-46-71")
                        .userStatus(2));
        String jsonRequestBody =
                step("Модель User в json", () ->
                        new ObjectMapper()
                                .writer()
                                .withDefaultPrettyPrinter()
                                .writeValueAsString(userRequest));
        step("Вызов запроса User для запроса Post", () ->
                restTemplate.exchange(uriPost, HttpMethod.POST, new HttpEntity<>(jsonRequestBody, headers), String.class));
    }

    @Story("Get /user")
    @Test(description = "Метод GET /user должен вернуть модель User")
    public void postGetUserByUserNameTest() {

        User getUserByUserName =
                step("Вызов запроса GET /user/username для получения созданного пользователя", () ->
                        restTemplate.exchange(uriGet, HttpMethod.GET, new HttpEntity<>(headers), User.class).getBody());
        SoftAssert softAssert = new SoftAssert();

        step("Сравнение ожидаемого и фактического результата", () -> {
                step("Сравнение по Id", () ->
                        softAssert.assertEquals(userRequest.getId(), getUserByUserName.getId(),
                                "Поле id не совпадает"));
        step("Сравнение по Username", () ->
                softAssert.assertEquals(userRequest.getUsername(), getUserByUserName.getUsername(),
                        "Поле username не совпадает"));
        step("Сравнение по FirstName", () ->
                softAssert.assertEquals(userRequest.getFirstName(), getUserByUserName.getFirstName(),
                        "Поле FirstName не совпадает"));
        step("Сравнение по LastName", () ->
                softAssert.assertEquals(userRequest.getLastName(), getUserByUserName.getLastName(),
                        "Поле LastName не совпадает"));
        step("Сравнение по Email", () ->
                softAssert.assertEquals(userRequest.getEmail(), getUserByUserName.getEmail(),
                        "Поле Email не совпадает"));
        step("Сравнение по Password", () ->
                softAssert.assertEquals(userRequest.getPassword(), getUserByUserName.getPassword(),
                        "Поле Password не совпадает"));
        step("Сравнение по Phone", () ->
                softAssert.assertEquals(userRequest.getPhone(), getUserByUserName.getPhone(),
                        "Поле Phone не совпадает"));
        step("Сравнение по UserStatus", () ->
                softAssert.assertEquals(userRequest.getUserStatus(), getUserByUserName.getUserStatus(),
                        "Поле UserStatus не совпадает"));
        });
    }

}
