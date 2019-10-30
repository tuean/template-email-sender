package builder;

import entity.WholeMessageInfo;
import org.apache.commons.lang.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;


public class MessageBuilder {

    private static Session session = null;

    private static Transport transport = null;


    public static MimeMessage makeMessage(WholeMessageInfo messageInfo) throws MessagingException, IOException {
        MimeMessage message = new MimeMessage(session);

        setReceiver(message, messageInfo);

        addMain(message, messageInfo);

        fillPhotoInContent(message, messageInfo);

        addCalPart(message, messageInfo);

        addAnnexPart(message, messageInfo);

        return message;
    }


    private static String getMineType(WholeMessageInfo messageInfo) {
        boolean htmlFlag = messageInfo.isHtmlFlag();
        String encoding = getEncoding(messageInfo);
        return htmlFlag ? "text/html; charset=" + encoding : "text/calendar; charset=" + encoding;
    }


    private static String getEncoding(WholeMessageInfo messageInfo) {
        return messageInfo.getEncoding() == null ? "utf-8" : messageInfo.getEncoding();
    }


    private static void setReceiver(Message message, WholeMessageInfo messageInfo) throws MessagingException {
        message.setFrom(new InternetAddress(messageInfo.getServerSetting().getAccount()));

        message.addRecipients(MimeMessage.RecipientType.TO, messageInfo.getToList().toArray(new InternetAddress[0]));
        message.addRecipients(MimeMessage.RecipientType.CC, messageInfo.getCcList().toArray(new InternetAddress[0]));
        message.addRecipients(MimeMessage.RecipientType.BCC, messageInfo.getBccList().toArray(new InternetAddress[0]));
    }


    private static void addMain(MimeMessage message, WholeMessageInfo messageInfo) throws MessagingException {
        BodyPart bodypart = new MimeBodyPart();
        bodypart.setContent(messageInfo.getContent(), getMineType(messageInfo));
        MimeMultipart multipart = new MimeMultipart();

        message.setSubject(messageInfo.getTitle(), getEncoding(messageInfo));
        message.setContent(multipart);
        multipart.addBodyPart(bodypart);
    }


    private static void addCalPart(MimeMessage message, WholeMessageInfo messageInfo) throws MessagingException, IOException {
        if (!messageInfo.isICalFlag()) return;
        Multipart multipart = (Multipart) message.getContent();
        if (StringUtils.isBlank(messageInfo.getICalText())) {
            return;
        }

        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(messageInfo.getICalText(), "text/calendar; method=REQUEST; charset=" + getEncoding(messageInfo));
        bodyPart.setDataHandler(
                new DataHandler(
                        new ByteArrayDataSource(
                                messageInfo.getICalText(), "text/calendar;method=REQUEST;charset=" + getEncoding(messageInfo))));
        multipart.addBodyPart(bodyPart);
    }


    private static void addAnnexPart(MimeMessage message, WholeMessageInfo messageInfo) throws IOException, MessagingException {
        Multipart multipart = (Multipart) message.getContent();
        BodyPart fileBodyPart = null;
        DataSource source = null;
        if (messageInfo.getAnnexList() == null || messageInfo.getAnnexList().size() < 1) {
            return;
        }

        for(int i = 0; messageInfo.getAnnexList().size() > i; i++){
            String filePath = messageInfo.getAnnexList().get(i).getFilePath();
            String fileName = messageInfo.getAnnexList().get(i).getFileName();
            // 解决中文乱码问题
            fileName = MimeUtility.encodeWord(fileName);
            source = new FileDataSource(filePath);
            fileBodyPart = new MimeBodyPart();
            fileBodyPart.setDataHandler(new DataHandler(source));
            fileBodyPart.setFileName(fileName);
            multipart.addBodyPart(fileBodyPart);
        }
    }


    private static void fillPhotoInContent(MimeMessage message, WholeMessageInfo messageInfo) {
        // todo show picture in the content of the email
        String content = messageInfo.getContent();
        Set<String> unreplacedPhoto = ContentBuilder.getUnreplacedPhoto(content);
        LinkedList<String> contentSplit = ContentBuilder.splitContent(content);
        for (String param : contentSplit) {
            boolean isPhotoFlag = ContentBuilder.isPhotoPlaceHolder(param);

        }
    }



    /**
     * 根据传入的文件路径创建附件并返回
     */
    public MimeBodyPart createAttachment(String fileName) throws Exception {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(fileName);
        attachmentPart.setDataHandler(new DataHandler(fds));
        attachmentPart.setFileName(fds.getName());
        return attachmentPart;
    }

    /**
     * 根据传入的邮件正文body和文件路径创建图文并茂的正文部分
     */
    public MimeBodyPart createContent(String body, String filePath) throws Exception {
        // 用于保存最终正文部分
        MimeBodyPart contentBody = new MimeBodyPart();
        // 用于组合文本和图片，"related"型的MimeMultipart对象
        MimeMultipart contentMulti = new MimeMultipart("related");

        // 正文的文本部分
        MimeBodyPart textBody = new MimeBodyPart();
        textBody.setContent(body, "text/html;charset=utf-8");
        contentMulti.addBodyPart(textBody);

        // 正文的图片部分
        MimeBodyPart jpgBody = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(filePath);
        jpgBody.setDataHandler(new DataHandler(fds));
        jpgBody.setContentID("photo");
        contentMulti.addBodyPart(jpgBody);

        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文
        contentBody.setContent(contentMulti);
        return contentBody;
    }


}
