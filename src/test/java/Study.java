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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;
import petstore.api.PetApi;
import petstore.model.Category;
import petstore.model.Order;
import petstore.model.Pet;
import petstore.model.Tag;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

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

    @Test
    public void test(){


    }
}