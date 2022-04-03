package com.tuean.email.templatesenderrest;

import model.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


//@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<? extends BaseResponse<? extends Object>> handleAllExceptions(Exception ex, WebRequest request) {
        BaseResponse<? extends Object> error = new BaseResponse();
        error.setCode(1);
        String msg = ex.getMessage();
        error.setMessage(msg);
        logger.error("", ex);
        return new ResponseEntity<BaseResponse<? extends Object>>(error, HttpStatus.OK);
    }



}
