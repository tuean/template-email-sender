package innerStorage;

import entity.SmtpConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SmtpConfigStorage {

    private static Map<String, SmtpConfig> smtpConfigMap = new HashMap<String, SmtpConfig>();


    public static String put(SmtpConfig smtpConfig) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        smtpConfigMap.put(uuid, smtpConfig);
        return uuid;
    }

    public static SmtpConfig get(String key) {
        return smtpConfigMap.get(key);
    }

}
