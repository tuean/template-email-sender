package innerStorage;

import entity.ServerSetting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SmtpConfigStorage {

    private static Map<String, ServerSetting> smtpConfigMap = new HashMap<String, ServerSetting>();


    public static String put(ServerSetting smtpConfig) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        smtpConfigMap.put(uuid, smtpConfig);
        return uuid;
    }

    public static ServerSetting get(String key) {
        return smtpConfigMap.get(key);
    }

}
