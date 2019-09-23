package util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.Map;

public class TemplateUtil {


    public static String getReplacedTemplate(Template template, Map<String, String> params, Configuration configuration) {

        try {
            // Console output
//            Writer out = new OutputStreamWriter(System.out);
//            template.process(params, out);
//            out.flush();

            // write the freemarker output to a StringWriter
            StringWriter stringWriter = new StringWriter();
            template.process(params, stringWriter);

            return stringWriter.toString();

        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }



    public static String getReplacedTemplate(String html, Map<String, String> params, Configuration configuration) throws IOException {
        Template template = new Template("templateName", new StringReader(html), configuration);
        return getReplacedTemplate(template, params, configuration);
    }

    public static String getReplacedTemplate(String html, Map<String, String> params) throws IOException {
        return getReplacedTemplate(html, params, getConfiguration());
    }

    public static String getReplacedTemplate(Template template, Map<String, String> params) throws IOException {
        return getReplacedTemplate(template, params, getConfiguration());
    }

    public static Configuration getConfiguration() {
        Configuration configuration = prepareConfiguration();
        // Load templates from resources/templatess.
        configuration.setClassForTemplateLoading(TemplateUtil.class, FileUtil.getFtlPath());
        return configuration;
    }

    private static Configuration prepareConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        return configuration;
    }




}
