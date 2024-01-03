

package site.bleem.boot.demo.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDefinedExcelFileUtil {
    private static final Logger log = LoggerFactory.getLogger(UserDefinedExcelFileUtil.class);

    public UserDefinedExcelFileUtil() {
    }

    public <T> void downloadTemplate(Class<T> beanType) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.setStyleSet(this.fontStyle(writer.getWorkbook()));
        writer.writeRow(this.getHeadTitleAndRule(beanType), true);
        File destFile = new File("/Users/admin/Desktop/user2.xlsx");
        writer.flush(destFile);
        System.out.println("write success!");
    }

    public <T> Map<Object, Object> getHeadTitleAndRule(Class<T> beanType) {
        Field[] fields = beanType.getDeclaredFields();
        Map<Object, Object> map = new HashMap();
        Field[] var4 = fields;
        int var5 = fields.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            String alisaName = ((JsonProperty)field.getDeclaredAnnotation(JsonProperty.class)).value();
            String ruleStr = ((JsonProperty)field.getDeclaredAnnotation(JsonProperty.class)).defaultValue();
            map.put(alisaName, ruleStr);
        }

        return map;
    }

    public StyleSet fontStyle(Workbook workbook) {
        StyleSet styleSet = new StyleSet(workbook);
        Font redFont = workbook.createFont();
        redFont.setColor((short)10);
        styleSet.setFont(redFont, true);
        return styleSet;
    }

//    public <T> List<T> importData(Class<T> beanType, String file) {
//        try {
//            ExcelReader excelReader = ExcelUtil.getReader(FileUtil.file(file));
//            Field[] fields = beanType.getDeclaredFields();
//            Field[] var5 = fields;
//            int var6 = fields.length;
//
//            for(int var7 = 0; var7 < var6; ++var7) {
//                Field field = var5[var7];
//                String headName = ((JsonProperty)field.getDeclaredAnnotation(JsonProperty.class)).value();
//                String fieldName = field.getName();
//                excelReader.addHeaderAlias(headName, fieldName);
//            }
//
//            List<T> data = excelReader.readAll(beanType);
//            Iterator var13 = data.iterator();
//
//            while(var13.hasNext()) {
//                T t = var13.next();
//                System.out.println(t.toString());
//            }
//
//            return data;
//        } catch (Exception var11) {
//            log.error(var11.getMessage());
//            return null;
//        }
//    }

    public <T> void downloadData(List<T> data, Class<T> beanType) {
        ExcelWriter excelWriter = ExcelUtil.getWriter();
        Field[] fields = beanType.getDeclaredFields();
        Field[] var5 = fields;
        int var6 = fields.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Field field = var5[var7];
            String headName = ((JsonProperty)field.getDeclaredAnnotation(JsonProperty.class)).value();
            String fieldName = field.getName();
            excelWriter.addHeaderAlias(fieldName, headName);
        }

        excelWriter.write(data);
        File destFile = new File(this.getClass().getResource("/template/employee.xlsx").getPath());
        excelWriter.flush(destFile);
        System.out.println("download success！");
    }

    public static void exportExcel(Collection<?> listData, Class<?> pojoClass, String headTitle, String sheetName, HttpServletResponse response) {
        String exportDateStr = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        ExportParams params = new ExportParams(headTitle, sheetName);
        params.setHeight((short)8);
        params.setStyle(UserDefinedExcelExportStyle.class);
        params.setSecondTitle(String.format("打印时间：%s    统计结果：%s 条  ",exportDateStr,listData.size()));
        params.setSecondTitleHeight((short)8);
        params.setAddIndex(true);
        params.setIndexName("序号");

        try {
            Workbook workbook = ExcelExportProUtil.exportExcel(params, pojoClass, listData);
            String fileName = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
            fileName = URLEncoder.encode(fileName, "UTF8");
            response.setContentType("application/vnd.ms-excel;chartset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception var9) {
            var9.printStackTrace();
        }
    }

}
