package entity;

import lombok.*;

import javax.mail.internet.InternetAddress;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SenderInfo {


    private String senderAccount;

    private String senderPwd;

    private List<InternetAddress> toList;

    private List<InternetAddress> ccList;

    private List<InternetAddress> bccList;

    private String title;

    private String content;

    private String encoding;

    private SmtpConfig smtpConfig;

}
