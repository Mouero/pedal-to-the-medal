import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.annotations.Test;

import java.net.URI;

public class Study {

    @Test
    public void getPetId(){
        RestTemplate restTemplate = new RestTemplate();
        URI URI = UriComponentsBuilder.fromHttpUrl("https://petstore.swagger.io/").path("/v2/pet/1").build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/json");

        ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.GET, new HttpEntity<>(headers), String.class);


        System.out.println(response);
    }

    @Test()
    public void postPetId() {
        RestTemplate postPet = new RestTemplate();
        String uri = "https://petstore.swagger.io//v2/pet";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");

        String json = "{\"id\":0,\"category\":{\"id\":0,\"name\":\"string\"},\"name\":\"doggie\",\"photoUrls\":[\"string\"],\"tags\":[{\"id\":0,\"name\":\"string\"}],\"status\":\"available\"}";

        ResponseEntity<String> resp = postPet.exchange(uri, HttpMethod.PUT, new HttpEntity<>(json, headers), String.class);

        System.out.println(resp);
    }

//sdfdsfsed


}