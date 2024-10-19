package com.huochai.jsonchange.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huochai.jsonchange.domain.DataOperation;
import com.huochai.jsonchange.domain.FilterCondition;
import com.huochai.jsonchange.domain.JsonEditRequest;

import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 *
 *@author peilizhi
 *@date 2024/10/19 16:12
 **/
@Service
public class JsonEditService {


    private final ObjectMapper objectMapper;

    public JsonEditService() {
        ObjectMapper mapper = new ObjectMapper();
        // 禁用默认的美化输出，使JSON紧凑无空格和换行
        mapper.writer().withDefaultPrettyPrinter();
        this.objectMapper = mapper;
    }

    public String editJson(JsonEditRequest request) throws Exception {
        JsonNode root = objectMapper.readTree(request.getInputJson());
        boolean matches = true;

        JsonNode result = root;
        // 多个条件是组层递增的
        for (int i = 0; i < request.getFilters().size(); i++) {
            FilterCondition filter = request.getFilters().get(i);
            String[] split = filter.getPath().split("\\.");
            result = getNodeByPath(result, split, 0, String.valueOf(filter.getValue()));
            if (result == null) {
                matches = false;
                break;
            }
        }


        //If filter conditions are met, apply data operations
        if (matches) {
            for (DataOperation operation : request.getChanges()) {
                switch (operation.getType()) {
                    case "ADD":
                    case "UPDATE":
                        addNode(result, operation.getPath(), operation.getValue(), operation.getValueType());
                        break;
                    case "DELETE":
                        deleteNode(result, operation.getPath());
                        break;
                }
            }
        }

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
    }

    private JsonNode getNodeByPath(JsonNode node, String[] path, int index, String value) {

        if (node.isObject()) {
            ObjectNode jsonNodes = (ObjectNode) node;
            String string = path[index];
            if (jsonNodes.has(string)) {
                // 走到最后
                if (path.length - 1 == index) {
                    // 比较value 是否相等
                    String text = jsonNodes.get(string).asText();
                    return Objects.equals(text, value) ? jsonNodes : null;
                } else {
                    // 继续向下找
                    return getNodeByPath(jsonNodes.get(string), path, index + 1, value);
                }

            } else {
                return null;
            }
        } else if (node.isArray()) {
            for (JsonNode arrayElement : node) {
                JsonNode nodeByPath = getNodeByPath(arrayElement, path, index, value);
                if (null != nodeByPath) {
                    return nodeByPath;
                }
            }
        }
        return null;
    }


    private void addNode(JsonNode rootNode, String path, Object value, String valueType) {
        switch (valueType) {
            case "string":
                String valueStr = String.valueOf(value);
                ((ObjectNode) rootNode).put(path, valueStr);
                break;
            case "integer":
                int valueInt = Integer.parseInt(String.valueOf(value));
                ((ObjectNode) rootNode).put(path, valueInt);
                break;
            case "float":
                double valueDouble = (double) (value);
                ((ObjectNode) rootNode).put(path, valueDouble);
                break;
            default:
                ((ObjectNode) rootNode).put(path, objectMapper.valueToTree(value));
        }

    }

    private void deleteNode(JsonNode rootNode, String path) {
        ((ObjectNode) rootNode).remove(path);
    }

    private void updateNode(JsonNode rootNode, String path, Object value, String valueType) {
        ((ObjectNode) rootNode).replace(path, objectMapper.valueToTree(value));
    }
}

