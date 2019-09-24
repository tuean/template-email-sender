package util;

import config.GlobalConfig;
import constant.Constant;
import entity.ServerSetting;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static constant.Constant.*;

/**
 * 邮件通知工具
 */
public class EmailUtil {

    private static Session session;

    private static Transport transport;

    private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);


    public void connect(ServerSetting serverSetting) throws MessagingException {
        Properties props = new Properties();
        if (serverSetting.getSmtpAuth() != null) {
            props.setProperty("mail.smtp.auth", serverSetting.getSmtpAuth());
        }
        if (serverSetting.getSmtpHost() != null) {
            props.setProperty("mail.smtp.host", serverSetting.getSmtpHost());
        }
        if (serverSetting.getSmtpPort() != null) {
            props.setProperty("mail.smtp.port", "25");
        }
        if (serverSetting.getSmtpPlain() != null) {
            props.setProperty("mail.smtp.auth.plain.disable", serverSetting.getSmtpPlain());
        }
        if (serverSetting.getSmtpSsl() != null) {
            props.setProperty("mail.smtp.ssl.enable", serverSetting.getSmtpSsl());
        }

        session = Session.getInstance(props);
        transport = session.getTransport("smtp");
        transport.connect(serverSetting.getAccount(), serverSetting.getPwd());
    }

    public void connect() throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.host","smtp.xx.xx");
        props.setProperty("mail.smtp.port", "25");
        props.setProperty("mail.smtp.auth.plain.disable", "true");
        // 使用SSL连接，见com.sun.mail.smtp.SMTPTransport
        // 某些邮件服务器，如Google，需要SSL安全连接
//        props.setProperty("mail.smtp.ssl.enable", "true");

        session = Session.getInstance(props);
        transport = session.getTransport("smtp");
        transport.connect("", "");
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

        Session session = Session.getInstance(props);
        Transport transport = session.getTransport("smtp");
        transport.connect(serverSetting.getAccount(), serverSetting.getPwd());

        transport.sendMessage(message, message.getAllRecipients());
    }


}