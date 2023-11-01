import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import petstore.model.*;

import java.net.URI;
import java.util.List;

import static io.qameta.allure.Allure.step;


@Feature("User")
public class StudySolo {
    private static final String URL = "http://localhost:";
    private static final int PORT = 8080;
    private static final String BASE_PATH = "/api/v3";
    private final RestTemplate restTemplate = new RestTemplate();
    private String uriGet;
    private HttpHeaders headers = new HttpHeaders();
    private User userRequest;
    private Pet petRequest;
    private Order orderRequest;

    @SneakyThrows
    @BeforeMethod(alwaysRun = true)
    public void beforeTestForPostGetUserByUserNameTest() {

        String
                baseUri =
                step("Создание базового URL", () ->
                        URL + PORT + BASE_PATH);
        String uriPost =
                step("Создание URI для запроса Post/user", () ->
                        baseUri + "/user");
        uriGet =
                step("Создание URI для запроса Get/user/Mouero", () ->
                        baseUri + "/user/Mouero");

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
            step("Сравнение по id", () ->
                    softAssert.assertEquals(userRequest.getId(), getUserByUserName.getId(),
                            "Поле id не совпадает"));
            step("Сравнение по username", () ->
                    softAssert.assertEquals(userRequest.getUsername(), getUserByUserName.getUsername(),
                            "Поле username не совпадает"));
            step("Сравнение по firstName", () ->
                    softAssert.assertEquals(userRequest.getFirstName(), getUserByUserName.getFirstName(),
                            "Поле firstName не совпадает"));
            step("Сравнение по lastName", () ->
                    softAssert.assertEquals(userRequest.getLastName(), getUserByUserName.getLastName(),
                            "Поле lastName не совпадает"));
            step("Сравнение по email", () ->
                    softAssert.assertEquals(userRequest.getEmail(), getUserByUserName.getEmail(),
                            "Поле email не совпадает"));
            step("Сравнение по password", () ->
                    softAssert.assertEquals(userRequest.getPassword(), getUserByUserName.getPassword(),
                            "Поле password не совпадает"));
            step("Сравнение по phone", () ->
                    softAssert.assertEquals(userRequest.getPhone(), getUserByUserName.getPhone(),
                            "Поле phone не совпадает"));
            step("Сравнение по userStatus", () ->
                    softAssert.assertEquals(userRequest.getUserStatus(), getUserByUserName.getUserStatus(),
                            "Поле userStatus не совпадает"));
            softAssert.assertAll();
        });
    }


    @SneakyThrows
    @BeforeMethod(alwaysRun = false)
    public void beforeTestForPostPetGetFindPetById() {
        String baseUri =
                step("Создание базового URI", () ->
                        URL + PORT + BASE_PATH);
        String uriPost =
                step("Создание Post запроса", () ->
                        baseUri + "/pet");
        uriGet =
                step("Создание Get запроса", () ->
                        baseUri + "/pet/22");
        headers = new HttpHeaders();
        step("Создание хедеров", () -> {
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        });
        petRequest = new Pet();
        step("Создание питомца", () ->
                petRequest
                        .id(22L)
                        .name("Volt")
                        .category(new Category().id(2L).name("doggie"))
                        .photoUrls(List.of("url1", "url2"))
                        .tags(List.of(new Tag().id(0L).name("Crossbreed"), new Tag().id(1L).name("Boy")))
                        .status(Pet.StatusEnum.SOLD));
        String jsonRequestBody =
                step("Модель Pet В json", () ->
                        new ObjectMapper()
                                .writer()
                                .withDefaultPrettyPrinter()
                                .writeValueAsString(petRequest));
        step("Вызов Post запроса", () ->
                restTemplate.exchange(uriPost, HttpMethod.POST, new HttpEntity<>(jsonRequestBody, headers), String.class));
    }



    @Story("Get /pet")
    @Test(description = "Метод GET /pet должен вернуть модель Pet")
    public void GetPetByPetIdTest() {
        Pet getPetByPetId =
                step("Вызов запроса GET /user/username для получения созданного пользователя", () ->
                        restTemplate.exchange(uriGet, HttpMethod.GET, new HttpEntity<>(headers), Pet.class).getBody());

        SoftAssert softAssert = new SoftAssert();
        step("Сравнение ожидаемого и фактического результата", () -> {
            step("Сравнение по id", () ->
                    softAssert.assertEquals(getPetByPetId.getId(), petRequest.getId(),
                            "Поле id не совпадает"));
            step("Сравнение по name", () ->
                    softAssert.assertEquals(getPetByPetId.getName(), petRequest.getName(),
                            "Поле name не совпадает"));
            step("Сравнение по category", () ->
                    softAssert.assertEquals(getPetByPetId.getCategory(), petRequest.getCategory(),
                            "Поле category не совпадает"));
            step("Сравнение по photoUrls", () ->
                    softAssert.assertEquals(getPetByPetId.getPhotoUrls(), petRequest.getPhotoUrls(),
                            "Поле category не совпадает"));
            step("Сравнение по tags", () ->
                    softAssert.assertEquals(getPetByPetId.getTags(), petRequest.getTags(),
                            "Поле tags не совпадает"));
            step("Сравнение по status", () ->
                    softAssert.assertEquals(getPetByPetId.getStatus(), petRequest.getStatus(),
                            "Поле status не совпадает"));
            softAssert.assertAll();
        });
    }

    @Story("Get /pet")
    @Test(description = "Метод GET /pet должен вернуть статус код 404 Not found")
    public void GetPetByPetIdNegativeTest() {
        HttpClientErrorException getPetByPetId =
            step("Вызов запроса GET /pet/{petId} для получения созданного питомца", () ->
                   Assert.expectThrows(
                            HttpClientErrorException.class,
                            () -> restTemplate.exchange(uriGet, HttpMethod.GET, new HttpEntity<>(headers), Pet.class))
            );
        Assert.assertEquals(getPetByPetId.getStatusCode(), HttpStatus.NOT_FOUND);
    }




}

