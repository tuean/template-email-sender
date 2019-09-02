package util;

import java.io.File;

/**
 * Created by zhongxiaotian on 2018/4/7.
 */
public class FileUtil {

    private static String PROJECT_PATH = null;

    /**
     * get runtime path of the jar file
     *
     * @return
     */
    public static String getProjectPath() {
        if (PROJECT_PATH == null) {
            File f = new File(System.getProperty("java.class.path"));
            File dir = f.getAbsoluteFile().getParentFile();
            PROJECT_PATH = dir.toString();
        }
        return PROJECT_PATH;
    }

    public static String getFtlPath() {
        String ftlPath = getProjectPath() + File.separator + "ftl" + File.separator;
        File file = new File(ftlPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return ftlPath;
    }

    /**
     * output the file content of the tmp
     *
     * @return path
     */
    public static String defaultFilePath(){
        String path = getProjectPath() + "/tmp/";
        // to check if run in the debug mode
        if (path.length() > 1000) {
            path = "/Users/tuean/Desktop/tmp/";
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }



    public static void main(String[] args) {
        System.out.println(defaultFilePath());
    }

}
