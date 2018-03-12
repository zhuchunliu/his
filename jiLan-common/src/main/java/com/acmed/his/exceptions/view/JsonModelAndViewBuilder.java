package com.acmed.his.exceptions.view;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Darren on 2017-11-24
 **/
public class JsonModelAndViewBuilder {
    public static ModelAndView build(Object object){
        FastJsonJsonView fastJsonJsonView = new FastJsonJsonView();
        fastJsonJsonView.setExtractValueFromSingleKeyModel(true);
        ModelAndView modelAndView = new ModelAndView(fastJsonJsonView);
        modelAndView.addObject(object);
        return modelAndView;
    }
}
