package entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerSetting {

    private String account;

    private String pwd;

    private String smtpAuth;

    private String smtpHost;

    private String smtpPort;

    private String smtpPlain;

    private String smtpSsl;

}
