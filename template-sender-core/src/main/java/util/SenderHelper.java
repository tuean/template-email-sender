package util;

import com.sun.nio.sctp.MessageInfo;
import entity.ServerSetting;
import org.apache.commons.lang.StringUtils;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static constant.Constant.*;

public class SenderHelper {

    private static Session session;

    private static Transport transport;

    public static void send(MimeMessage message, ServerSetting serverSetting) throws MessagingException {
        Properties props = new Properties();
        props.setProperty(SMTP_AUTH, serverSetting.getSmtpAuth());
        props.setProperty(SMTP_HOST, serverSetting.getSmtpHost());
        props.setProperty(SMTP_PORT, serverSetting.getSmtpPort());
        props.setProperty(SMTP_PLAIN, serverSetting.getSmtpPlain());
        // 使用SSL连接，见com.sun.mail.smtp.SMTPTransport
        // 某些邮件服务器，如Google，需要SSL安全连接
//        props.setProperty("mail.smtp.ssl.enable", "true");
        if (StringUtils.isNotBlank(serverSetting.getSmtpSsl())) {
            props.setProperty(SMTP_SSL, serverSetting.getSmtpSsl());
        }

        session = Session.getInstance(props);
        transport = session.getTransport("smtp");
        transport.connect(serverSetting.getAccount(), serverSetting.getPwd());

        transport.sendMessage(message, message.getAllRecipients());
    }

    public static void close() {
        try {
            if (transport != null) {
                transport.close();
                transport = null;
            }
        } catch (Exception e) {

        }
        session = null;
    }


}
