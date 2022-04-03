package com.tuean.email.templatesenderrest.controller;

import com.tuean.email.templatesenderrest.utils.Util;
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

    @RequestMapping(value = "/config/smtp/{key}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse setSmtpConfig(@PathVariable("key") String key, @RequestBody ServerSetting serverSetting) {
        SmtpConfigStorage.put(key, serverSetting);
        return new BaseResponse().ok();
    }

    @RequestMapping(value = "/config/smtp", method = RequestMethod.GET)
    public BaseResponse getSmtpConfig(@RequestParam("key") String key, @RequestParam("pwd") String pwd) {
        ServerSetting serverSetting = SmtpConfigStorage.get(key);
        if (serverSetting == null) return new BaseResponse().error();
        if (!Util.equals(serverSetting.getPwd(), pwd)) return new BaseResponse().error();
        return new BaseResponse().ok(serverSetting);
    }






}
