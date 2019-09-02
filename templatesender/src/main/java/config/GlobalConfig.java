package config;

import java.util.Map;

public interface GlobalConfig {

    // 1:db 2:.properties

    Integer type = -1;

    void init();

    void save();

    void reload();

    Map getConfig();

    String getValue(String key);

    void set(String key, Object value);


}
