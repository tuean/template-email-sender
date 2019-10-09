package excel;

import entity.WholeMessageInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class test {

    public static void main(String[] args) throws FileNotFoundException {
        String source = "C:\\Users\\Administrator\\IdeaProjects\\template-email-sender\\test-doc\\template-excel.xlsx";
        WholeMessageInfo reuslt = ExcelParseFactory.parse(new FileInputStream(source));
        System.out.println(11);
    }


}
