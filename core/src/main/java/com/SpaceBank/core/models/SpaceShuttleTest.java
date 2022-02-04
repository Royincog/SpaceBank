package com.SpaceBank.core.models;

import com.SpaceBank.core.slingpojos.Weather;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Model(adaptables = {Resource.class, SlingHttpServletRequest.class})
@Slf4j
public class SpaceShuttleTest {


    private final Map<String, Object> responseMap;
    private final List<Weather> weatherList;
    private final ObjectMapper objectMapper;
    private final Weather weatherObject;

    public Weather getWeatherObject() throws IOException {
        readTree();
        return weatherObject;
    }

    //Constructor to initialize values lala
    public SpaceShuttleTest() {
        this.responseMap = Maps.newHashMap();
        this.weatherList = Lists.newArrayList();
        this.objectMapper = new ObjectMapper();
        this.weatherObject = new Weather();
    }

    @PostConstruct
    private void init() throws IOException {
        log.info("The Object mapper info " + objectMapper.toString());
        log.info("The map i got is " + responseMap.size());
        log.info("The response from weather is " + getInfo());
        log.info("The SpaceShuttle exexuted");
    }

    private String getInfo() {
        HttpResponse<String> response = Unirest.get("https://community-open-weather-map.p.rapidapi.com/weather?q=Hong%20Kong&lat=0&lon=0&callback=test&id=2172797&lang=null&units=imperial&mode=xml")
                .header("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                .header("x-rapidapi-key", "ed117fd37dmshd6eddf4627228d6p1d5fd5jsnca0f4f5f355a")
                .asString();
        String result = response.getBody();
        result = result.substring(result.indexOf("(") + 1, result.lastIndexOf(")"));
        return result;
    }


    private void readTree() throws IOException {
        String[] description = new String[1];
        String temp_min = "";
        String temp_max = "";
        String pressure = "";
        String humidity = "";
        String avgTemp = "";

        String response = getInfo();
        JsonNode jsonNode = objectMapper.readTree(response);
        //Get an entryset
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        List<Map.Entry<String, JsonNode>> entryList = ImmutableList.copyOf(fields);

        log.info("Info about entries " + entryList);

        for (Map.Entry<String, JsonNode> entry : entryList) {
            if (entry.getKey().equals("weather")) {
                ArrayNode weatherArrayNode = (ArrayNode) entry.getValue();
                weatherArrayNode.forEach((node) -> {
                    description[0] += " " + node.get("main");
                });
            }
            if(entry.getKey().equals("main")){
                JsonNode mainNode = (JsonNode) entry.getValue();
                temp_max = mainNode.get("temp_max").asText();
                temp_min =  mainNode.get("temp_min").asText();
                pressure = mainNode.get("pressure").asText();
                humidity = mainNode.get("humidity").asText();
                avgTemp =  mainNode.get("temp").asText();
            }
        }
        description[0] = description[0].replace("null","").trim();
        String actualDescription = changetheDescription(description[0].replaceAll("\"", ""));
        weatherObject.setAvgtemp(avgTemp);
        weatherObject.setDescription(actualDescription);
        weatherObject.setHumidity(humidity);
        weatherObject.setPressure(pressure);
        weatherObject.setTemp_min(temp_min);
        weatherObject.setTemp_max(temp_max);

    }

    private String changetheDescription(String s) {
        StringBuffer newString = new StringBuffer(s);
        if( s.split(" ").length > 1) {


            newString.insert(s.indexOf(' ') + 1, "& ");
            return newString.toString();
        }
        return newString.toString();
    }
}


