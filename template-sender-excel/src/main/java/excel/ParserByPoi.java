package excel;

import constant.DefaultServerSetting;
import entity.ServerSetting;
import entity.WholeMessageInfo;
import enums.EmailType;
import model.EmailTemplate;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.util.*;

public class ParserByPoi implements IExcelParser{


    @Override
    public EmailTemplate parse(InputStream inputStream) {
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            EmailTemplate emailTemplate = new EmailTemplate();
            // content title
            parseEmailInfo(sheet, emailTemplate);
            // account pwd
            parseSenderInfo(sheet, emailTemplate);
            // receive info


        }catch (Exception var) {
            var.printStackTrace();
        }
        return null;
    }

    private static DataFormatter formatter = new DataFormatter();

    private static void parseEmailInfo(Sheet sheet, EmailTemplate emailTemplate) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        int index = 0;
        while (rowIterator.hasNext()) {

            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = formatter.formatCellValue(cell);
                if (index == 1 && StringUtils.isNotBlank(emailTemplate.getTitle())) {
                    emailTemplate.setTitle(cellValue);
                    index = 0;
                }
                if (index == 2 && StringUtils.isNotBlank(emailTemplate.getTemplate())) {
                    emailTemplate.setTemplate(cellValue);
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


    public static void parseSenderInfo(Sheet sheet, EmailTemplate emailTemplate) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        int sender_x = -1, sender_y = -1, pwd_x = -1, pwd_y = -1;
        int index = 0, rowNum = 0;

        String email = "", pwd = "";
        while(rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = formatter.formatCellValue(cell);
                if (sender_x == index && sender_y == rowNum) {
                    email = cellValue;
                }
                if (pwd_x == index && pwd_y == rowNum) {
                    pwd = cellValue;
                }
                switch (cellValue) {
                    case "发件人":
                        sender_x = index + 1;
                        sender_y = rowNum;
                        break;
                    case "发件人密码":
                        pwd_x = index + 1;
                        pwd_y = rowNum;
                        break;
                }

                rowNum++;
            }

            index++;
        }

        EmailType emailType = null;
        if (email.trim().endsWith("@163.com")) {
            emailType = EmailType.E_163;
        }
        if (email.trim().endsWith("@gmail.com")) {
            emailType = EmailType.E_GMAIL;
        }
        if (emailType == null) {
            throw new RuntimeException("do not support this email now");
        }

        ServerSetting serverSetting = DefaultServerSetting.getServerSettingByType(emailType);
        serverSetting.setAccount(email);
        serverSetting.setPwd(pwd);
        emailTemplate.setServerSetting(serverSetting);
    }


    public static List<WholeMessageInfo> parseReceiveInfo(Sheet sheet, WholeMessageInfo result) {
        Iterator<Row> rowIterator = sheet.rowIterator();

        int line = -1, rowNum = 0;
        int toIndex = -1, ccIndex = -1, bccIndex = -1;
        boolean normalFlag = false;
        boolean keyFlag = false;
        Map<Integer, String> normalMap = new HashMap<>();
        Map<Integer, String> keyMap = new HashMap<>();

        Map<String, String> thisNormalMap = new HashMap<>();
        Map<String, String> thisKeyMap = new HashMap<>();

        List<WholeMessageInfo> resultList = new ArrayList<>();

        while(rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            rowNum = 0;

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = formatter.formatCellValue(cell).trim();
                if (line < 0 && "收件人".equals(cellValue)) {
                    line = rowNum;
                    toIndex = rowNum;
                }
                if ("抄送人".equals(cellValue)) {
                    ccIndex = rowNum;
                }
                if ("暗送人".equals(cellValue)) {
                    bccIndex = rowNum;
                }
                if (line == rowNum) {
                    if ("通用属性".equals(cellValue)) {
                        normalFlag = true;
                        keyFlag = false;
                    }
                    if ("特有属性".equals(cellValue)) {
                        normalFlag = false;
                        keyFlag = true;
                    }
                    if (normalFlag) {
                        normalMap.put(rowNum, cellValue);
                    }
                    if (keyFlag) {
                        keyMap.put(rowNum, cellValue);
                    }
                }

                if (rowNum > line) {

                }

                rowNum++;
            }
            line++;
        }

        return resultList;
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
