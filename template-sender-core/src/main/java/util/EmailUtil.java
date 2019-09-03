package util;

import config.GlobalConfig;
import constant.Constant;
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

/**
 * 邮件通知工具
 */
public class EmailUtil {

    private static Session session;

    private static Transport transport;

    private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);


    @Autowired
    private GlobalConfig globalConfig;


    public String getSenderAccount() {
        return globalConfig.getValue(Constant.SENDER_ACCOUNT);
    }

    public String getSenderPwd() {
        return globalConfig.getValue(Constant.SENDER_PWD);
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
        transport.connect(getSenderAccount(), getSenderPwd());
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