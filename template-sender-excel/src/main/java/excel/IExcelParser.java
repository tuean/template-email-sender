package excel;

import model.EmailTemplate;

import java.io.InputStream;

public interface IExcelParser {

    EmailTemplate parse(InputStream inputStream);

}
