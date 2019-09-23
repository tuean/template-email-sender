package model;

import entity.SmtpConfig;
import lombok.*;

import javax.mail.internet.InternetAddress;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailModel {

    private String title;

    private String content;

    private List<InternetAddress> toList;

    private List<InternetAddress> ccList;

    private List<InternetAddress> bccList;

    private SmtpConfig smtpConfig;

}
