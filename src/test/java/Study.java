import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;
import petstore.model.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @Test
    public void getStoreOderByIdTest() {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder baseUri = UriComponentsBuilder.fromHttpUrl(url).port(port).path(basePath);

        String uriPost = baseUri.build() + "/store/order";
        String uriGet = baseUri.build() + "/store/order/1";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);


        Order orderRequest = new Order();
        orderRequest
                .id(1L)
                .petId(1978L)
                .quantity(4)
                .status(Order.StatusEnum.PLACED)
                .complete(true);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonRequestBody = ow.writeValueAsString(orderRequest);

        restTemplate.exchange(uriPost, HttpMethod.POST, new HttpEntity<>(jsonRequestBody, headers), String.class);
        Order getStoreOrderIdResponse = restTemplate.exchange(uriGet, HttpMethod.GET, new HttpEntity<>(headers), Order.class).getBody();

        Assert.assertEquals(orderRequest, getStoreOrderIdResponse);
    }


    @SneakyThrows
    @Test
    public void getUserByUserNameTest() {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder baseUri = UriComponentsBuilder.fromHttpUrl(url).port(port).path(basePath);

        String uriPost = baseUri.build() + "/user";
        String uriGet = baseUri.build() + "/user/Mouero";

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        User userRequest = new User();
        userRequest
                .id(5L)
                .username("Mouero")
                .firstName("Anton")
                .lastName("Dubovy")
                .email("anton.bud@yandex.ru")
                .password("pass123654")
                .phone("8 999 452-46-71")
                .userStatus(2);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonRequestBody = ow.writeValueAsString(userRequest);

        restTemplate.exchange(uriPost, HttpMethod.POST, new HttpEntity<>(jsonRequestBody, headers), String.class);
        User getUserByUserName = restTemplate.exchange(uriGet, HttpMethod.GET, new HttpEntity<>(headers), User.class).getBody();

        Assert.assertEquals(userRequest, getUserByUserName);


    }

    @SneakyThrows
    @Test
    public void postUserCreateWithListTest() {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder baseUri = UriComponentsBuilder.fromHttpUrl(url).port(port).path(basePath);

        String uriPost = baseUri.build() + "/user/createWithList";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        User userRequest = new User();
        userRequest
                .id(5L)
                .username("Mouero")
                .firstName("Anton")
                .lastName("Dubovy")
                .email("anton.bud@yandex.ru")
                .password("pass123654")
                .phone("8 999 452-46-71")
                .userStatus(2);

        User newUserRequest = new User();
        userRequest
                .id(6L)
                .username("Ostalf")
                .firstName("Daniyar")
                .lastName("Rakhymbek")
                .email("daniyar.rakhymbek@yandex.ru")
                .password("p3657904")
                .phone("8 991 865-12-49")
                .userStatus(1);
        List<User> listUser = List.of(userRequest,newUserRequest);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonRequestBody = ow.writeValueAsString(listUser);

        restTemplate.exchange(uriPost, HttpMethod.POST, new HttpEntity<>(jsonRequestBody, headers), String.class);

    }

}





