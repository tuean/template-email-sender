package excel;

import entity.WholeMessageInfo;

import java.io.InputStream;

public class ExcelParseFactory {


    public static WholeMessageInfo parse(InputStream inputStream) {
        IExcelParser parser = null;
//        if () {
        // TODO: 2019/10/8
//        }
        parser = new ParseByPoi();
        return parser.parse(inputStream);
    }
}
