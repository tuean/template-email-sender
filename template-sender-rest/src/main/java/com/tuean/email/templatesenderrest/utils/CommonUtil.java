package com.tuean.email.templatesenderrest.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;

public class CommonUtil {

    public static String paramCheck(String param) {
        return StringUtils.isBlank(param) ? null : param;
    }

    public static String paramCheck(Object param) {
        return param == null ? null : param.toString();
    }

    public static String emptyFilter(Object param) {
        return param == null ? "" : param.toString();
    }

    public static BigDecimal check(BigDecimal decimal) {
        return decimal == null ? new BigDecimal(0) : decimal;
    }

    public static void downloadResponse(File file, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(file.getName(), "UTF-8"));
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = httpServletResponse.getOutputStream();
        IOUtils.copy(inputStream, outputStream);
        outputStream.close();
        outputStream.flush();
    }
}
