package com.huochai.jsonchange.domain;

import lombok.Data;

/**
 *
 *@author peilizhi
 *@date 2024/10/19 16:48
 **/
@Data
public class JsonEditResponse {


    private String jsonOutput;


    public JsonEditResponse(String jsonOutput) {
        this.jsonOutput = jsonOutput;
    }
}
