package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder

public class PetMessage {
    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("category")
    private PetCategory category;

    @JsonProperty("photoUrls")
    private Set photoUrls;

    @JsonProperty("tags")
    private Set<PetTag> tags;

    @JsonProperty("status")
    private PetStatus status;
}
