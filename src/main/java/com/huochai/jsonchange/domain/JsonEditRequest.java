package com.huochai.jsonchange.domain;

import java.util.List;

import lombok.Data;

/**
 *
 *@author peilizhi
 *@date 2024/10/19 16:46
 **/
@Data
public class JsonEditRequest {


    private String inputJson;

    private List<FilterCondition> filters;


    private List<DataOperation> changes;
}
