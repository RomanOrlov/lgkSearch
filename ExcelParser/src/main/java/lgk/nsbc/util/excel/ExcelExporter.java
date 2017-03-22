package lgk.nsbc.util.excel;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Экспорт данных в excel в том же виде, в каком они представлены
 * в интерфейсе
 */
@Service
public class ExcelExporter {
    public void exportToExcel() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow firstHeader = sheet.createRow(0);
        XSSFRow secondHeader = sheet.createRow(1);
        // Сливаем ячейки
        //sheet.addMergedRegion(new CellRangeAddress())
    }
}
