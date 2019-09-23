package entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmtpConfig {

    private String auth;

    private String host;

    private String port;

    private Boolean authPlainDisable;

    private Boolean sslEnable;



}
