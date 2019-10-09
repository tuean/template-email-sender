package constant;

import entity.ServerSetting;
import enums.EmailType;

public class DefaultServerSetting {


    public static ServerSetting getServerSettingByType(EmailType emailType) {
        switch (emailType) {
            case E_163: return get163();
            case E_GMAIL: return getGmail();
        }
        return null;
    }


    private static ServerSetting get163() {
        return ServerSetting.builder()
                .smtpHost("smtp.163.com")
                .smtpSsl("false")
                .smtpAuth("true")
                .smtpPort("25")
                .build();
    }

    private static ServerSetting getGmail() {
        return ServerSetting.builder()
                .smtpHost("smtp.gmail.com")
                .smtpSsl("true")
                .smtpAuth("true")
                .smtpPort("465")
//                .smtpPort("587") // ssl
                .smtpSsl("true")
                .build();
    }

}
