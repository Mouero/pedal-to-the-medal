import com.beust.ah.A;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dto.*;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class Study {

    private String url = "http://localhost";
    private int port = 8080;
    private String basePath = "/api/v3";

    @SneakyThrows
    @Test
    public void getPetId() {
        //setup
        RestTemplate postPet = new RestTemplate();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder baseUri = UriComponentsBuilder.fromHttpUrl(url).port(port).path(basePath);

        URI uriPost = baseUri.path("/pet").build().toUri();
        URI uriGet = baseUri.path("/pet/0").build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Set photoUrls = new HashSet();
        photoUrls.add("url1");
        photoUrls.add("url2");

        Set tags = new HashSet<>();
        tags.add(
                PetTag.builder()
                        .id(1)
                        .name("Sheltie")
                        .build());
        tags.add(
                PetTag.builder()
                        .id(2)
                        .name("Boy")
                        .build());

        PetMessage petMessage = PetMessage.builder()
                .id(0)
                .name("Night")
                .category(PetCategory.builder().id(1).name("Dogs").build())
                .photoUrls(photoUrls)
                .tags(tags)
                .status(PetStatus.sold)
                .build();

        petMessage.setId(0);
        petMessage.getId();
        //setup

        //deserialize to json
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(petMessage);
        //deserialize to json


        //post pet data
        postPet.exchange(uriPost, HttpMethod.POST, new HttpEntity<>(json, headers), String.class);
        //post pet data

        //get pet data
        String getPetIdResponse = restTemplate.exchange(uriGet, HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();
        //get pet data

        //assert data
        Assert.assertTrue(getPetIdResponse.contains(String.valueOf(petMessage.getId())));
        Assert.assertTrue(getPetIdResponse.contains(petMessage.getName()));
        Assert.assertTrue(getPetIdResponse.contains(String.valueOf(petMessage.getStatus())));
        //assert data

    }

    @SneakyThrows
    @Test
    public void postPetId() {
        RestTemplate postPet = new RestTemplate();
        URI URI = UriComponentsBuilder.fromHttpUrl(url).port(port).path(basePath).path("/pet").build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        var s = new LinkedHashMap<>().put("s","s");

        Set photoUrls = new HashSet();
        photoUrls.add("url1");
        photoUrls.add("url2");

        Set tags = new HashSet<>();
        tags.add(
                PetTag.builder()
                        .id(1)
                        .name("tag2")
                        .build());
        tags.add(
                PetTag.builder()
                        .id(2)
                        .name("tag3")
                        .build());


        PetMessage petMessage = PetMessage.builder()
                .id(0)
                .name("doge")
                .category(PetCategory.builder().id(1).name("Dogs").build())
                .photoUrls(photoUrls)
                .tags(tags)
                .status(PetStatus.sold)
                .build();



        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(petMessage);

        System.out.println(json);

        ResponseEntity<String> resp = postPet.exchange(URI, HttpMethod.POST, new HttpEntity<>(json, headers), String.class);
    }


    @Test
    public void findByStatus() {
        RestTemplate findByStatus = new RestTemplate();
        URI URI = UriComponentsBuilder.fromHttpUrl(url).port(port).path(basePath).path("/pet/findByStatus").queryParam("status", "sold").build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<String> resp = findByStatus.exchange(URI, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        System.out.println(resp);


    }
    //test


}