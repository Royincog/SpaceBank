package com.SpaceBank.core.slingpojos;

import lombok.Data;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Data
public class ColorPojo {

    private String code;
    private String likes;
    private String date;

    public List<String> getColorCodes(){
        List<String> listofCodes = Lists.newArrayList();
        for(int i = 0;i<code.length();i=i+6){
            String hexCode = code.substring(i,i+6);
            listofCodes.add(hexCode);
        }
        return listofCodes;
    }
}
