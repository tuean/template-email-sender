package helper;

import entity.PhotoTair;
import entity.WholeMessageInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import innerStorage.SmtpConfigStorage;
import model.EmailTemplate;
import model.ParseResult;
import org.apache.commons.lang.StringUtils;
import util.CommonUtil;
import util.FileUtil;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 邮件正文文字处理部分
 */
public class ContentBuilder {


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
        configuration.setClassForTemplateLoading(ContentBuilder.class, FileUtil.getFtlPath());
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
        List<WholeMessageInfo> list = new ArrayList<>();
        List<String> errorList = new ArrayList<>(check(emailTemplate));

        for (int x = 0; emailTemplate.getParams().size() > x; x++) {
            Map<String, Object> map = emailTemplate.getParams().get(x);
            WholeMessageInfo model = WholeMessageInfo.builder()
                    .bccList(parseAddresses(emailTemplate.getReceiverList().get(x).getBccList(), errorList))
                    .ccList(parseAddresses(emailTemplate.getReceiverList().get(x).getCcList(), errorList))
                    .toList(parseAddresses(emailTemplate.getReceiverList().get(x).getToList(), errorList))
                    .title(replace(emailTemplate.getTitle(), map))
                    .content(replace(emailTemplate.getTemplate(), map))
                    .serverSetting(SmtpConfigStorage.get(emailTemplate.getSmtpConfigId()))
                    .build();
            list.add(model);
        }
        return ParseResult.builder()
                .error(errorList)
                .messageInfoList(list)
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
        if (params != null) {
            for (int x = 0; params.size() > x; x++) {

                Set<String> unreplaced = getUnreplacedWord(emailTemplate.getTemplate());
                Set<String> keySet = params.get(x).keySet();
                if (unreplaced.size() > keySet.size()) {
                    errorList.add("第" + (x + 1) + "个模板中参数有遗漏");
                    continue;
                }

                String result = replace(emailTemplate.getTemplate(), params.get(x));
                Set<String> afterReplaced = getUnreplacedWord(result);
                if (afterReplaced.size() > 0) {
                    String error = unreplaced.stream().collect(Collectors.joining("，"));
                    errorList.add("第" + (x+1) + "个模板中存在未替换的参数：" + error);
                    continue;
                }
            }
        }


        LinkedList<Map<String, PhotoTair>> photos = emailTemplate.getPhotos();
        if (photos != null) {
            for (int x = 0; photos.size() > x; x++) {
                Set<String> photoKey = photos.get(x).keySet();
                Set<String> unreplaced = getUnreplacedPhoto(emailTemplate.getTemplate());
                boolean equalFlag = CommonUtil.checkTwoSetIfEqual(photoKey, unreplaced);
                if (!equalFlag) {
                    errorList.add("第" + (x+1) + "个模板中图片参数有误");
                }
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

            result = result.replace(getWordPlaceHolder(key), valueString);
        }
        return result;
    }



    private static Set<String> getUnreplaced(String source, String type) {
        Set<String> keyList = new HashSet<>();
        int start = 0, end = 0, index = 2;
        int length = source.length();
        if (length < index) return keyList;

        while (length > index) {
            if (source.charAt(index - 2) == '$' && source.charAt(index - 1) == type.toCharArray()[0] && source.charAt(index) == '{') {
                start = index;
            }
            if (source.charAt(index) == '}') {
                end = index;
            }

            if (start < end) {
                keyList.add(source.substring(start, end));
                start = index; end = index;
            }
            index++;
        }

        return keyList;
    }

    public static Set<String> getUnreplacedWord(String source) {
        return getUnreplaced(source, WORD_SEP);
    }

    public static Set<String> getUnreplacedPhoto(String source) {
        return getUnreplaced(source, PHOTO_SEP);
    }


    public static String getWordPlaceHolder(String key) {
        return "$" + WORD_SEP + "{" + key + "}";
    }


    public static String getPhotoPlaceHolder(String key) {
        return "$" + PHOTO_SEP + "{" + key + "}";
    }

    public static boolean isPhotoPlaceHolder(String key) {
        return key.startsWith("$" + PHOTO_SEP + "{") && key.endsWith("}");
    }

    private static final String WORD_SEP = "w";
    private static final String PHOTO_SEP = "f";


    public static LinkedList<String> splitContent(String content) {
        LinkedList<String> result = new LinkedList<>();

        int last = 0, start = 0, end = 0, index = 2;
        if (content.length() < index) return result;
        char[] chars = content.toCharArray();
        while (content.length() > index) {
            if (chars[index - 2] == '$' && chars[index] == '{' &&
                    (chars[index - 1] == WORD_SEP.charAt(0) || chars[index - 1] == PHOTO_SEP.charAt(0)) ) {
                start = index;
            }
            if (chars[index] == '}') {
                end = index;
            }
            if (start < end) {
                if (last < start) {
                    result.add(content.substring(last, start));
                }
                result.add(content.substring(start, end));
                start = index; end = index; last = end;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(" oasda ads ".split("a"));
    }




}
