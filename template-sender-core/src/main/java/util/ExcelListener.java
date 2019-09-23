package util;

import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;

import java.util.LinkedList;
import java.util.List;

public class ExcelListener extends AnalysisEventListener {

    //自定义用于暂时存储data。
    //可以通过实例获取该值
    private List result = new LinkedList<>();
    public void invoke(Object object, AnalysisContext context) {
        String sheetName = context.getCurrentSheet().getSheetName();

//        System.out.println("当前行："+context.getCurrentRowNum());
//        System.out.println(object);
        //数据存储到list，供批量处理，或后续自己业务逻辑处理。
        result.add(object);

        //根据自己业务做处理
//        doSomething(object);

    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List getResult() {
        return result;
    }
}
