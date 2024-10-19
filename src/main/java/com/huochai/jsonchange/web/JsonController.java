package com.huochai.jsonchange.web;

import com.huochai.jsonchange.Service.JsonEditService;
import com.huochai.jsonchange.domain.JsonEditRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

/**
 *
 *@author peilizhi
 *@date 2024/10/19 16:14
 **/
@Controller
public class JsonController {


    @Resource
    private JsonEditService jsonEditService;


    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("jsonEditRequest", new JsonEditRequest());
        return "index";
    }


    @PostMapping("/process-json")
    public ResponseEntity<String> editJson(@RequestBody JsonEditRequest jsonEditRequest, Model model) {
        try {
            String result = jsonEditService.editJson(jsonEditRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("处理失败，请检查输入。");
        }
    }

}
