import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import petstore.api.PetApi;
import petstore.model.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import static io.qameta.allure.Allure.step;

public class Study {
    private String url = "http://localhost";
    private int port = 8080;
    private String basePath = "/api/v3";

    @SneakyThrows
    @Test
    public void getPetIdShouldReturnPetTest() {
        //setup
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder baseUri = UriComponentsBuilder.fromHttpUrl(url).port(port).path(basePath);

        String uriPost = baseUri.build() + "/pet";
        String uriGet = baseUri.build() + "/pet/0";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Pet petRequest = new Pet();
        petRequest
                .id(0L)
                .name("Night")
                .category(new Category().id(0L).name("Dogs"))
                .photoUrls(List.of("url1", "url2"))
                .tags(List.of(new Tag().id(0L).name("Sheltie"), new Tag().id(1L).name("Boy")))
                .status(Pet.StatusEnum.SOLD);


        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonRequestBody = ow.writeValueAsString(petRequest);

        restTemplate.exchange(uriPost, HttpMethod.POST, new HttpEntity<>(jsonRequestBody, headers), String.class);

        Pet getPetIdResponse = restTemplate.exchange(uriGet, HttpMethod.GET, new HttpEntity<>(headers), Pet.class).getBody();

        Assert.assertEquals(petRequest, getPetIdResponse);
    }


    @SneakyThrows
    @Test(dataProvider = "getStoryOrder")
    public void getStoreOderByIdTest(Order orderRequest, String id) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder baseUri = UriComponentsBuilder.fromHttpUrl(url).port(port).path(basePath);

        String uriPost = baseUri.build() + "/store/order";
        String uriGet = baseUri.build() + "/store/order/"+id;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonRequestBody = ow.writeValueAsString(orderRequest);

        restTemplate.exchange(uriPost, HttpMethod.POST, new HttpEntity<>(jsonRequestBody, headers), String.class);

        Order getStoreOrderIdResponse = restTemplate.exchange(uriGet, HttpMethod.GET, new HttpEntity<>(headers), Order.class).getBody();

        Assert.assertEquals(orderRequest, getStoreOrderIdResponse);
    }

    @Test
    public void justGetPetExceptionTest() {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder baseUri = UriComponentsBuilder.fromHttpUrl(url).port(port).path(basePath);

        String uriGet = baseUri.build() + "/pet/8373";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);


        HttpClientErrorException exception =
                Assert.expectThrows(
                        HttpClientErrorException.NotFound.class,
                        () -> restTemplate.exchange(uriGet, HttpMethod.GET, new HttpEntity<>(headers), Pet.class)
                );
        System.out.println(exception.toString());
    }

    @Test
    public void getUserByUserNameBadRequestExceptionTest() {

        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder baseUri = UriComponentsBuilder.fromHttpUrl(url).port(port).path(basePath);

        String uriGet = baseUri.build() + "/user/Anton";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        try {
            restTemplate.exchange(uriGet, HttpMethod.GET, new HttpEntity<>(headers), User.class);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
        }
    }

    @SneakyThrows
    @Test(dataProvider = "bodyData")
    public void postGetUserByUserNameTest(User userRequest) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder baseUri = UriComponentsBuilder.fromHttpUrl(url).port(port).path(basePath);

        String uriPost = baseUri.build() + "/user";
        String uriGet = baseUri.build() + "/user/Mouero";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonRequestBody = ow.writeValueAsString(userRequest);

        restTemplate.exchange(uriPost, HttpMethod.POST, new HttpEntity<>(jsonRequestBody, headers), String.class);
        User getStoreOrderIdResponse = restTemplate.exchange(uriGet, HttpMethod.GET, new HttpEntity<>(headers), User.class).getBody();

        Assert.assertEquals(userRequest, getStoreOrderIdResponse);
    }

    @DataProvider(name = "bodyData")
    public Object[][] bodyData() {
        return new Object[][]{
                {
                        new User().id(64L)
                                .username("Mouero")
                                .firstName("Anton")
                                .lastName("Dubovyy")
                                .email("anton.dubovyy@yandex,ru")
                                .password("12345678")
                                .phone("8 999 452-46-71")
                                .userStatus(1)},
                {
                        new User().id(65L)
                                .username("MAZDA")
                                .firstName("Demio")
                                .lastName("azazaz")
                                .email("zazazaazza@yandex,ru")
                                .password("322")
                                .phone("1232154215")
                                .userStatus(2)}

        };
    }
    @DataProvider(name = "getStoryOrder")
    public Object[][] getStoryOrder() {
        return new Object[][]{
                {
                        new Order()
                                .id(1L)
                                .petId(1978L)
                                .quantity(4)
                                .status(Order.StatusEnum.PLACED)
                                .complete(true)
                        ,"1"
                },
                {
                        new Order()
                                .id(2L)
                                .petId(1911L)
                                .quantity(5)
                                .status(Order.StatusEnum.DELIVERED)
                                .complete(false)
                        ,"2"
                }
        };
    }
}