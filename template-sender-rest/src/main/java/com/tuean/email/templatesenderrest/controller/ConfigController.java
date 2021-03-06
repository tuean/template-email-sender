package com.tuean.email.templatesenderrest.controller;

import entity.ServerSetting;
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
    public BaseResponse setSmtpConfig(@RequestBody ServerSetting serverSetting) {
        return new BaseResponse().ok(SmtpConfigStorage.put(serverSetting));
    }

    @RequestMapping(value = "/config/smtp", method = RequestMethod.GET)
    public BaseResponse getSmtpConfig(@RequestParam("key") String key) {
        ServerSetting serverSetting = SmtpConfigStorage.get(key);
        return serverSetting == null ?
                new BaseResponse().error() :
                new BaseResponse().ok(serverSetting);
    }






}
