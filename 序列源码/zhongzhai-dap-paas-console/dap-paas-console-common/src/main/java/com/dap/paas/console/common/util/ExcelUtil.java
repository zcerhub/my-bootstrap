package com.dap.paas.console.common.util;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

public class  ExcelUtil<T> {

    /**
     * @param sheetNames sheet名
     * @param heads      首行的值，可以为null。当heads为null时，putData方法中datas的key会作为首行，列顺序为datas的顺序
     * @return
     */
    public static Workbook creatExcelSheets(List<String> sheetNames, Map<String, List<String>> heads) {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        for (String sheetName : sheetNames) {
            HSSFSheet sheet = hssfWorkbook.createSheet(sheetName);
            List<String> sheetHead = heads.get(sheetName);
            HSSFRow titleRow = sheet.createRow(0);
            if (sheetHead != null) {
                for (int i = 0; i < sheetHead.size(); i++) {
                    titleRow.createCell(i).setCellValue(sheetHead.get(i));
                }
            }
        }
        return hssfWorkbook;
    }

    public static Workbook creatExcelSheets(List<String> sheetNames,List<String> sheetHead) {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        for (String sheetName : sheetNames) {
            HSSFSheet sheet = hssfWorkbook.createSheet(sheetName);
            HSSFRow titleRow = sheet.createRow(0);
            if (sheetHead != null) {
                for (int i = 0; i < sheetHead.size(); i++) {
                    titleRow.createCell(i).setCellValue(sheetHead.get(i));
                }
            }
        }
        return hssfWorkbook;
    }
    public static HSSFWorkbook creatExcelSheet(HSSFWorkbook hssfWorkbook,String sheetName,List<String> sheetHead) {
        HSSFSheet sheet = hssfWorkbook.createSheet(sheetName);
        HSSFRow titleRow = sheet.createRow(0);
        if (sheetHead != null) {
            for (int i = 0; i < sheetHead.size(); i++) {
                titleRow.createCell(i).setCellValue(sheetHead.get(i));
            }
        }
        return hssfWorkbook;
    }

    /**
     * 首行带筛选
     * @param sheetNames
     * @param sheetHead
     * @param flag
     * @return
     */
    public static Workbook creatExcelSheets(List<String> sheetNames, List<String> sheetHead, boolean flag) {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        for (String sheetName : sheetNames) {
            HSSFSheet sheet = hssfWorkbook.createSheet(sheetName);
            HSSFRow titleRow = sheet.createRow(0);
            if (sheetHead != null) {
                for (int i = 0; i < sheetHead.size(); i++) {
                    HSSFCell cell = titleRow.createCell(i);
                    cell.setCellValue(sheetHead.get(i));
                }
            }

            if (flag){

                Character area = (char)(65+sheetHead.size());
                CellRangeAddress c = CellRangeAddress.valueOf("A1:"+area.toString()+"1");
                sheet.setAutoFilter(c);
            }
        }
        return hssfWorkbook;
    }

    /**
     *
     * @param hssfWorkbook
     * @param sheetName sheet名
     * @param datas 数据< key(对应首行的值)， 数据集合<></>>。当出现key的值和首行（表头）不对应时，
     *              多出的数据自动以追加到后面的列，以key为表头，以data的顺序为表头顺序；
     * @return
     */
    public static Workbook putData(Workbook hssfWorkbook, String sheetName, Map<String, List<String>> datas) {
        Sheet sheet = hssfWorkbook.getSheet(sheetName);
        Row titleRow = sheet.getRow(0);
        int maxColumnNum = new Short(titleRow.getLastCellNum()).intValue();


        for (int i = 0; i < maxColumnNum; i++) {//列
            String stringCellValue = getCellStringVal(titleRow.getCell(i));
            List<String> dataList = datas.get(stringCellValue);

            if (dataList != null) {
                for (int j = 1; j <= dataList.size(); j++) {//行
                    Row currentRow = sheet.getRow(j);
                    if (currentRow == null) {
                        currentRow = sheet.createRow(j);
                    }
                    currentRow.createCell(i).setCellValue(dataList.get(j - 1));
                }
            }
            datas.remove(stringCellValue);
        }
        //剩余的找不到表头的数据，追加到后面
        if (datas.keySet().size() == 0) {

        } else {
            Iterator<String> iterator = datas.keySet().iterator();
            while (iterator.hasNext()) {

                Row firstRow = sheet.getRow(0);
                int j = 0;
                int column = maxColumnNum + j;
                String next = iterator.next();
                firstRow.createCell(column).setCellValue(next);
                List<String> otherData = datas.get(next);

                for (int i = 1; i <= otherData.size(); i++) {
                    Row currentRow = sheet.getRow(i);
                    if (currentRow == null) {
                        currentRow = sheet.createRow(i);
                    }
                    currentRow.createCell(column).setCellValue(otherData.get(i - 1));
                }
                j++;
            }
        }
        return hssfWorkbook;
    }

    /**
     * 输出文档
     * @param hssfWorkbook
     * @param fileName 文件名 例：xxx.xls
     * @param filePath  文件路径  例：F:/xxx/
     * @throws IOException
     */
    public static void createFile(Workbook hssfWorkbook, String fileName, String filePath) throws IOException {

        char c = filePath.charAt(filePath.length() - 1);
        if(c != '/'){
            filePath = filePath+"/";
        }
        FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath + fileName));
        hssfWorkbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    /**
     * 输出文件到页面
     *
     * @param wb excle
     * @param response 响应
     * @param fileName 文件名
     */
    public static void writeExcel(Workbook wb, HttpServletResponse response, String fileName) {

        OutputStream out = null;
        try {
            //response.reset();
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName,"UTF-8")+".xls");
            out = response.getOutputStream();
            wb.write(out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                wb.close();
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 获取excel第一行标题
     * @param sheet sheet
     * @return String[]
     */
    public static String[] readFirstTitle(HSSFSheet sheet) {
        Row titleRow = sheet.getRow(0);
        String[] title = new String[titleRow.getLastCellNum()];
        for (int i = titleRow.getFirstCellNum(); i < titleRow.getLastCellNum(); i++) {
            Cell cell = titleRow.getCell(i);
            if (Objects.nonNull(cell)) {
                title[i] = getCellStringVal(cell);
            }
        }
        return title;
    }

    /**
     * 解析cell，转成对应的对象
     * @param cell cell
     * @param parser 自定义解析器，针对对象嵌套链表等特殊情况，可为null
     * @return
     */
    public static Object parseCell(Cell cell, Function<Cell, Object> parser) {
        if (Objects.isNull(cell)) {
            return null;
        }
        // 自定义解析规则
        if (Objects.nonNull(parser)) {
            return parser.apply(cell);
        }
        return getCellStringVal(cell);
    }

    /**
     * 读取excel数据转List集合
     * @param file file
     * @param titleMap 首行标题与属性映射
     * @param tClass 转换目标类
     * @param parser 自定义解析器，针对对象嵌套链表等特殊情况，可为null
     * @param <T>
     * @return List<转换目标类>
     */
    public static <T> List<T> readExcelToList(MultipartFile file, Map<String, String> titleMap,
                                              Class<T> tClass, Function<Cell, Object> parser) {
        JSONArray jsonArray = new JSONArray();
        try (InputStream in = file.getInputStream()) {
            POIFSFileSystem fileSystem = new POIFSFileSystem(in);
            HSSFWorkbook wb = new HSSFWorkbook(fileSystem);
            int sheetNum = wb.getNumberOfSheets();
            String[] title = null;
            // 遍历sheet
            for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {
                HSSFSheet sheet = wb.getSheetAt(sheetIndex);
                // 获取第一行标题
                if (Objects.isNull(title)) {
                    title = readFirstTitle(sheet);
                }
                // 遍历行
                for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    int initialCapacity = (int) ((float) title.length / 0.75F + 1.0F);
                    Map<String, Object> map = new HashMap<>(initialCapacity);
                    // 遍历列
                    for (int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) {
                        // 将每一个单元格的值对应属性名放入map
                        Cell cell = row.getCell(cellIndex);
                        String key = title[cellIndex];
                        map.put(titleMap.get(key), parseCell(cell, parser));
                    }
                    jsonArray.add(map);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonArray.toJavaList(tClass);
    }

    /**
     * List<Bean>转Map<String, List<String>>
     * @param sourceList 数据集合
     * @param fieldMap 需要获取的字段名映射，Map<类属性名，对应字段名>
     * @param tClass 被转换目标类
     * @param <T>
     * @return Map<字段名，List<该字段的值>>
     */
    public static <T> Map<String, List<String>> convertListToMapList
            (List<T> sourceList, Map<String, String> fieldMap, Class<T> tClass) {
        // 每一个属性的值都收集到valueList，然后以属性对应字段名称作为key，put到resultMap中
        Map<String, List<String>> resultMap = new HashMap<>(fieldMap.size());
        try {
            for (Map.Entry<String, String> e : fieldMap.entrySet()) {
                List<String> valueList = new ArrayList<>(sourceList.size());
                for (T item : sourceList) {
                    Object obj = new PropertyDescriptor(e.getKey(), tClass).getReadMethod().invoke(item);
                    valueList.add(Objects.isNull(obj) ? "" : obj.toString());
                }
                resultMap.put(e.getValue(), valueList);
            }
        } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    private static String getCellStringVal(Cell cell) {
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case NUMERIC:
                return cell.getStringCellValue();
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            default:
                return StringUtils.EMPTY;
        }
    }

    /**
     * 获取每行的数据
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if (cell.getCellType() == NUMERIC) {
            cell.setCellType(STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            //数字
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            //字符串
            case STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            //Boolean
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            //公式
            case FORMULA:
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            //空值
            case BLANK:
                cellValue = "";
                break;
            //故障
            case ERROR:
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }


}
