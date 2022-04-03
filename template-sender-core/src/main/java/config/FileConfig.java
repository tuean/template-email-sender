package config;

import util.FileUtil;

import java.io.*;
import java.util.Properties;

public class FileConfig {

    public static String makeFilePath(String fileName) {
        String projectPath = FileUtil.getProjectPath();
        return projectPath + File.separator + fileName + ".properties";
    }

    public static Properties loadProperties(String fileName) {
        String filePath = makeFilePath(fileName);
        Properties prop = new Properties();
        try(InputStream in = new BufferedInputStream(new FileInputStream(filePath))) {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;
    }

    public static boolean storeProperties(Properties properties, String fileName) {
        String filePath = makeFilePath(fileName);
        try {
            FileOutputStream oFile = new FileOutputStream(filePath, false);//true表示追加打开
            properties.store(oFile, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


}
