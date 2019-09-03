package util;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.InputStream;
import java.util.List;

public class ExcelUtil {


    public static List parseExcel(InputStream in) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader excelReader = new ExcelReader(in, ExcelTypeEnum.XLSX, null, excelListener);
//        excelReader.read();
        List<Sheet> sheets = excelReader.getSheets();
        for (Sheet sheet : sheets) {
            excelReader.read(sheet);
        }
        return excelListener.getResult();
    }

}


