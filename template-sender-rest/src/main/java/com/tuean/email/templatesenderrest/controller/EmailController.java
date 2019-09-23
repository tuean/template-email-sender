package com.tuean.email.templatesenderrest.controller;


import com.alibaba.fastjson.JSON;
import model.BaseResponse;
import model.EmailTemplate;
import model.ParseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import util.SenderHelper;
import util.TemplateUtil;

import javax.validation.constraints.Email;
import java.util.List;


@RestController
public class EmailController {

    private static Logger logger = LoggerFactory.getLogger(EmailController.class);


    @RequestMapping(value = "/template/check", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse checkTemplate(@RequestBody EmailTemplate emailTemplate) {
        List<String> errorList = TemplateUtil.check(emailTemplate);
        return errorList.size() > 1 ? new BaseResponse().error(JSON.toJSONString(errorList)) : new BaseResponse().ok();
    }

    @RequestMapping(value = "/email/send", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse send(@RequestBody EmailTemplate emailTemplate) {
        List<String> errorList = SenderHelper.doSend(emailTemplate);
        return errorList.size() > 1 ? new BaseResponse().error(JSON.toJSONString(errorList)) : new BaseResponse().ok();
    }



}
