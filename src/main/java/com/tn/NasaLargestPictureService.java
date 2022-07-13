package com.tn;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class NasaLargestPictureService {
    @Value("${nasa.picture.base_url}")
    private String baseUrl;
    @Value("${nasa.picture.api_key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Cacheable("picture")
    @SneakyThrows
    public String getLargestPictureURl(int sol) {
        var apiUri = UriComponentsBuilder.fromUriString(baseUrl)
                                         .queryParam("sol", sol)
                                         .queryParam("api_key", apiKey)
                                         .build()
                                         .toUriString();

        var jsonNode = restTemplate.getForObject(apiUri, JsonNode.class);

        List<String> originalUrls = ofNullable(jsonNode).map(jnode -> jnode.get("photos"))
                                                        .map(photos -> photos.findValuesAsText("img_src"))
                                                        .orElse(emptyList());

        var sizeToUrlMap = originalUrls.stream()
                                       .collect(toMap(identity(),
                                                      originalUrl -> getSize(getLocation(URI.create(originalUrl)))));

        var maxEntry = sizeToUrlMap.entrySet()
                                   .stream()
                                   .max(Map.Entry.comparingByValue())
                                   .orElseThrow(NoSuchElementException::new);

        return maxEntry.getKey();
    }

    private URI getLocation(URI originalUrl) {
        return restTemplate.headForHeaders(originalUrl)
                           .getLocation();
    }

    private Long getSize(URI location) {
        return restTemplate.headForHeaders(location)
                           .getContentLength();
    }
}
