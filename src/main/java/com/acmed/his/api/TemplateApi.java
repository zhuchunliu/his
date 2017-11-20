package com.acmed.his.api;

import com.acmed.his.model.DiagnosisTpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
@RestController
@Api("模板信息")
@RequestMapping("/tpl")
public class TemplateApi {

    @Autowired


    @GetMapping("/diagnosis/list")
    public List<DiagnosisTpl> getDiagnosisList(){

        return null;
    }
}
