package com.huochai.jsonchange.domain;

import lombok.Data;

/**
 *
 *@author peilizhi
 *@date 2024/10/19 16:41
 **/
@Data
public class FilterCondition {


    private String path;


    private Object value;
}
