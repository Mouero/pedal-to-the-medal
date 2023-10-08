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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import petstore.model.Category;
import petstore.model.Pet;
import petstore.model.Tag;

import java.util.List;

import static io.qameta.allure.Allure.step;

@Feature("Pet")
public class MostClearestTestWhenYouNeverSeenBeforeTest {
    private static final String URL = "http://localhost";
    private static final int PORT = 8080;
    private static final String BASE_PATH = "/api/v3";
    private final RestTemplate restTemplate = new RestTemplate();
    private UriComponentsBuilder baseUri;
    private String uriPost;
    private String uriGet;
    private HttpHeaders headers;
    private HttpEntity<String> requestEntity;
    private Pet petRequest;

    @SneakyThrows
    @BeforeTest(alwaysRun = true)
    public void beforeTest() {
        baseUri =
                step("Создание базового URL", () ->
                        UriComponentsBuilder.fromHttpUrl(URL).port(PORT).path(BASE_PATH));
        uriPost =
                step("Создание URI для POST /pet", () ->
                        baseUri.build() + "/pet");

        uriGet =
                step("Создание URI для GET /pet/{petId}", () ->
                        baseUri.build() + "/pet/0");

        headers = new HttpHeaders();
        step("Добавление хедеров", () -> {
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        });

        petRequest = new Pet();
        step("Заполнение модели Pet данными", () ->
                petRequest
                        .id(0L)
                        .name("Night")
                        .category(new Category().id(0L).name("Dogs"))
                        .photoUrls(List.of("url1", "url2"))
                        .tags(List.of(new Tag().id(0L).name("Sheltie"), new Tag().id(1L).name("Boy")))
                        .status(Pet.StatusEnum.SOLD));

        String jsonRequestBody =
                step("Сериализация заполненной модели Pet в json", () ->
                        new ObjectMapper()
                                .writer()
                                .withDefaultPrettyPrinter()
                                .writeValueAsString(petRequest));

        requestEntity = new HttpEntity<>(jsonRequestBody, headers);
    }

    @Story("GET /pet")
    @Test(description = "Метод GET /pet должен вернуть модель Pet")
    public void getPetIdShouldReturnPetTest() {

        step("Вызов запроса POST /pet для создания питомца", () ->
                restTemplate.exchange(uriPost, HttpMethod.POST, requestEntity, String.class));

        Pet getPetIdResponse =
                step("Вызов запроса GET /pet/{petId} для получения созданного питомца", () ->
                        restTemplate.exchange(uriGet, HttpMethod.GET, new HttpEntity<>(headers), Pet.class).getBody());

        SoftAssert softAssert = new SoftAssert();

        step("Сравнение ожидаемого и фактического результата", () -> {
            step("Сравнение поля id", () ->
                    softAssert.assertEquals(petRequest.getId(), getPetIdResponse.getId(),
                            "поле id не совпадает с ожидаемым"));
            step("Сравнение поля name", () ->
                    softAssert.assertEquals(petRequest.getName(), getPetIdResponse.getName(),
                            "поле name не совпадает с ожидаемым"));
            step("Сравнение поля category", () ->
                    softAssert.assertEquals(petRequest.getCategory(), getPetIdResponse.getCategory(),
                            "поле category не совпадает с ожидаемым"));
            step("Сравнение поля tags", () ->
                    softAssert.assertEquals(petRequest.getTags(), getPetIdResponse.getTags(),
                            "поле tags не совпадает с ожидаемым"));
            step("Сравнение поля status", () ->
                    softAssert.assertEquals(petRequest.getStatus(), getPetIdResponse.getStatus(),
                            "поле status не совпадает с ожидаемым"));
            step("Сравнение поля photoUrls", () ->
                    softAssert.assertEquals(petRequest.getPhotoUrls(), getPetIdResponse.getPhotoUrls(),
                            "поле photoUrls не совпадает с ожидаемым"));
            softAssert.assertAll();
        });
    }
}
