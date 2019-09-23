package util;

import entity.SmtpConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import innerStorage.SmtpConfigStorage;
import model.EmailModel;
import model.EmailTemplate;
import model.ParseResult;
import org.apache.commons.lang.StringUtils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static ParseResult parse(EmailTemplate emailTemplate) {
        List<EmailModel> list = new ArrayList<>();
        List<String> errorList = new ArrayList<>(check(emailTemplate));

        for (Map<String, Object> map : emailTemplate.getParams()) {
            EmailModel model = EmailModel.builder()
                    .bccList(parseAddresses(emailTemplate.getBccList(), errorList))
                    .ccList(parseAddresses(emailTemplate.getCcList(), errorList))
                    .toList(parseAddresses(emailTemplate.getToList(), errorList))
                    .title(replace(emailTemplate.getTitle(), map))
                    .content(replace(emailTemplate.getTemplate(), map))
                    .smtpConfig(SmtpConfigStorage.get(emailTemplate.getSmtpConfigId()))
                    .build();
            list.add(model);
        }
        return ParseResult.builder()
                .error(errorList)
                .modelList(list)
                .build();
    }

    private static List<InternetAddress> parseAddresses(List<String> emailList, List<String> errorList) {
        if (emailList == null) return new ArrayList<>();
        List<InternetAddress> addressList = new ArrayList<>(emailList.size());
        for (String email : emailList) {
            InternetAddress address = parseAddress(email, errorList);
            if (address == null) continue;
            addressList.add(address);
        }

        return addressList;
    }

    private static InternetAddress parseAddress(String email, List<String> errorList) {
        try {
            return new InternetAddress(email);
        } catch (AddressException e) {
            e.printStackTrace();
            errorList.add(email + "邮箱解析失败");
            return null;
        }
    }


    public static List<String> check(EmailTemplate emailTemplate) {
        List<String> errorList = new ArrayList<>();
        if (emailTemplate == null || StringUtils.isBlank(emailTemplate.getTemplate())) {
            errorList.add("参数为空");
            return errorList;
        }

        LinkedList<Map<String, Object>> params = emailTemplate.getParams();
        for (int x = 0; params.size() > x; x++) {

            String result = replace(emailTemplate.getTemplate(), params.get(x));

            List<String> unreplaced = getUnreplaced(result);
            if (unreplaced.size() > 0) {
                String error = unreplaced.stream().collect(Collectors.joining("，"));
                errorList.add("第" + (x+1) + "个模板中存在未替换的参数：" + error);
                continue;
            }
        }

        return errorList;
    }


    private static String replace(String result, Map<String, Object> map) {
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            String valueString = "";
            if (value instanceof String) {
                valueString = String.valueOf(value);
            }
            if (value instanceof Date) {
                valueString = simpleDateFormat.format(value);
            }

            result = result.replace(getPlaceHolder(key), valueString);
        }
        return result;
    }


    private static String getPlaceHolder(String key) {
        return "${" + key + "}";
    }


    private static List<String> getUnreplaced(String source) {
        List<String> keyList = new ArrayList<>();
        int start = 0, end = 0, index = 1;
        int length = source.length();

        while (length > index) {
            if (source.charAt(index - 1) == '$' && source.charAt(index) == '{') {
                start = index;
            }
            if (source.charAt(index) == '}') {
                end = index;
            }

            if (start < end) {
                keyList.add(source.substring(start, end));
                start = 0; end = 0;
            }
            index++;
        }

        return keyList;
    }

}
