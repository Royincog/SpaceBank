package com.SpaceBank.core.models;


import com.SpaceBank.core.slingpojos.ColorPojo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},cache = true)
public class ColorHutModel {

    private final ObjectMapper objectMapper;
    @Getter
    private ColorPojo colorPallete;


    public ColorHutModel() throws IOException {
        //Object Mapper
        this.objectMapper = new ObjectMapper();
        this.colorPallete = generateRandomColorPallete();
    }


    @PostConstruct
    private void init() throws IOException {

        log.info("We are in post construct in model ");
        log.info("The info about the color nodes " + getColorPojoListFromResponse());
        log.info("A random color pallete " + colorPallete);
    }

private String getResponseFromApi() throws IOException {


    Map<String, Object> fields = new HashMap<>();
    fields.put("step",0);
    fields.put("sort","random");
    fields.put("tags","");

    HttpResponse<String> jsonResponse
            = Unirest.post("https://colorhunt.co/php/feed.php")
            .fields(fields)
            .asString();
return jsonResponse.getBody();
}
private List<ColorPojo> getColorPojoListFromResponse() throws IOException {
    List<ColorPojo> colorPojos = objectMapper.readValue(getResponseFromApi(),
            new TypeReference<List<ColorPojo>>(){});
            return colorPojos;
}
private synchronized ColorPojo generateRandomColorPallete() throws IOException {
        List<ColorPojo> colorPojos = getColorPojoListFromResponse();
        int aRandomNum = (int) (Math.random() * colorPojos.size());
        return colorPojos.get(aRandomNum);
    }

}
