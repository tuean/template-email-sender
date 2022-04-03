package helper;

import entity.ServerSetting;
import entity.WholeMessageInfo;
import builder.ContentBuilder;
import builder.MessageBuilder;
import model.EmailTemplate;
import model.ParseResult;
import util.EmailUtil;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;


public class SenderHelper {

    public static List<String> ignoreErrorWhileSending(EmailTemplate emailTemplate) {
        ParseResult result = ContentBuilder.parse(emailTemplate);
        if (result.getError().size() > 0) {
            return result.getError();
        }

        List<String> errorList = new LinkedList<>();
        List<WholeMessageInfo> modelList = result.getMessageInfoList();
        Map<MimeMessage, ServerSetting> map = new LinkedHashMap<>();
        for (int x = 0; modelList.size() > x; x++) {
            WholeMessageInfo model = modelList.get(x);
            try {
                MimeMessage message = MessageBuilder.makeMessage(model);
                map.put(message, model.getServerSetting());
            } catch (Exception var) {
                errorList.add("第" + (x+1) + "封邮件内容异常");
            }
        }

        if (errorList.size() > 0) return errorList;

        int index = 0;
        for (MimeMessage message : map.keySet()) {
            index++;
            try {
                EmailUtil.send(message, map.get(message));
            } catch (MessagingException e) {
                errorList.add("第" + (index+1) + "封邮件发送异常");
            }
        }

        return errorList;
    }


    public static List<String> doSend(EmailTemplate emailTemplate) {
        List<String> result = new ArrayList<>();

        return result;
    }


}
