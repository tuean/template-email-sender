package com.tuean.email.templatesenderrest.controller;

import entity.SmtpConfig;
import innerStorage.SmtpConfigStorage;
import model.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
public class ConfigController {

    private static Logger logger = LoggerFactory.getLogger(ConfigController.class);



    @RequestMapping(value = "/config/smtp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse setSmtpConfig(@RequestBody SmtpConfig smtpConfig) {
        return new BaseResponse().ok(SmtpConfigStorage.put(smtpConfig));
    }

    @RequestMapping(value = "/config/smtp", method = RequestMethod.GET)
    public BaseResponse getSmtpConfig(@RequestParam("key") String key) {
        SmtpConfig smtpConfig = SmtpConfigStorage.get(key);
        return smtpConfig == null ?
                new BaseResponse().error() :
                new BaseResponse().ok(smtpConfig);
    }






}
