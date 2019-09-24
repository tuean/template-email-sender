package helper;

import entity.WholeMessageInfo;
import model.EmailTemplate;
import model.ParseResult;
import util.EmailUtil;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;


public class SenderHelper {

    public static List<String> doSend(EmailTemplate emailTemplate) {
        ParseResult result = ContentBuilder.parse(emailTemplate);
        if (result.getError().size() > 0) {
            return result.getError();
        }

        List<String> errorList = new ArrayList<>();
        List<WholeMessageInfo> modelList = result.getMessageInfoList();
        for (int x = 0; modelList.size() > x; x++) {
            WholeMessageInfo model = modelList.get(x);
            try {
                MimeMessage message = MessageBuilder.makeMessage(model);
                EmailUtil.send(message, model.getServerSetting());
            } catch (Exception var) {
                errorList.add("第" + (x+1) + "封邮件发送异常");
            }
        }

        return errorList;
    }


}
