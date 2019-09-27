package com.tuean.email.templatesenderrest.controller;


import model.BaseResponse;
import model.EmailTemplate;
import model.TemplateCheckRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import helper.SenderHelper;
import helper.ContentBuilder;

import java.util.List;


@RestController
public class EmailController {

    private static Logger logger = LoggerFactory.getLogger(EmailController.class);

    @RequestMapping(value = "/template/check", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse getTemplatePaarma(@RequestBody TemplateCheckRequest templateCheckRequest) {
        return new BaseResponse().ok(ContentBuilder.parseContent(templateCheckRequest.getContent()));
    }

    @RequestMapping(value = "/email/check", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse checkTemplate(@RequestBody EmailTemplate emailTemplate) {
        List<String> errorList = ContentBuilder.check(emailTemplate);
        return new BaseResponse().withError(errorList);
    }

    @RequestMapping(value = "/email/send", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse send(@RequestBody EmailTemplate emailTemplate) {
        List<String> errorList = SenderHelper.doSend(emailTemplate);
        return new BaseResponse().withError(errorList);
    }



}
