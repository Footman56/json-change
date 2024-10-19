package com.huochai.jsonchange.domain;

import lombok.Data;

/**
 *
 *@author peilizhi
 *@date 2024/10/19 16:46
 **/
@Data
public class DataOperation {

    private String type;


    private String path;


    private Object value;


    /**
     * 值类型
     */
    private String valueType;
}
