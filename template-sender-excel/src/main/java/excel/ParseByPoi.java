package excel;

import entity.WholeMessageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ParseByPoi implements IExcelParser {

    private static DataFormatter formatter = new DataFormatter();

    @Override
    public WholeMessageInfo parse(InputStream inputStream) {
        try {
            WholeMessageInfo result = new WholeMessageInfo();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            parseEmailInfo(sheet, result);


            return result;
        } catch (Exception var) {
            var.printStackTrace();
        }

        return null;
    }

    private static void parseEmailInfo(Sheet sheet, WholeMessageInfo result) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        int index = 0;
        while (rowIterator.hasNext()) {

            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = formatter.formatCellValue(cell);
                if (index == 1 && StringUtils.isNotBlank(result.getTitle())) {
                    result.setTitle(cellValue);
                    index = 0;
                }
                if (index == 2 && StringUtils.isNotBlank(result.getContent())) {
                    result.setContent(cellValue);
                    index = 0;
                    return;
                }
                switch (cellValue.trim()) {
                    case "标题":
                        index = 1;
                        break;
                    case "正文":
                        index = 2;
                        break;
                    default:
                        index = 0;
                        break;
                }
            }
        }
    }


    public static void getSenderInfo(Sheet sheet, WholeMessageInfo result) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        int sender_x = -1,  pwd_x = -1;
        int index = 0, rowNum = 0;
        while(rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = formatter.formatCellValue(cell);
                switch (cellValue) {
                    case "发件人":
                        sender_x = index;
                        break;
                    case "发件人密码":
                        pwd_x = index;
                        break;
                }

                rowNum++;
            }

            index++;
        }
    }



    public static List<Object> parseBySheet(Sheet sheet) {
        List<Object> list = new LinkedList<>();
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            List<String> param = new LinkedList<>();

            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = formatter.formatCellValue(cell);
                param.add(cellValue);
            }
            list.add(param);
        }
        return list;
    }



}
