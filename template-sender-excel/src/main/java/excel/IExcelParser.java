package excel;

import entity.WholeMessageInfo;

import java.io.InputStream;

public interface IExcelParser {

    WholeMessageInfo parse(InputStream inputStream);

}
