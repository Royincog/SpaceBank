package com.SpaceBank.core.slingpojos;
import lombok.Data;

@Data
public class Weather {

    private String description;
    private String temp_min;
    private String temp_max;
    private String pressure;
    private String humidity;
    private String avgtemp;
}
