package lgk.nsbc.spect.util.excel;

import com.vaadin.data.provider.Query;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import lgk.nsbc.spect.view.statistic.SampleBind;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class StatisticExcelExporter extends ExcelExportButton {
    private final Grid<SampleBind> grid;
    private final CheckBox selectAll = new CheckBox("Все столбцы");

    public StatisticExcelExporter(Grid<SampleBind> grid, String caption) {
        super(caption);
        this.grid = grid;
        addComponent(selectAll);
    }

    @Override
    protected void exportToExcel(OutputStream outputStream) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Экспорт данных");
        XSSFRow header = sheet.createRow(0);
        // Будут выбраны только те, которые прошли фильтры
        List<SampleBind> filteredData = grid.getDataProvider()
                .fetch(new Query<>())
                .collect(toList());
        List<Grid.Column<SampleBind, ?>> visibleColumn = grid.getColumns()
                .stream()
                .filter(column -> selectAll.getValue() || !column.isHidden())
                .collect(toList());
        header.getLastCellNum();
        for (Grid.Column<SampleBind, ?> column : visibleColumn) {
            short lastCellNum = header.getLastCellNum();
            XSSFCell cell = header.createCell(lastCellNum == -1 ? 0 : lastCellNum);
            cell.setCellValue(column.getCaption());
        }
        for (SampleBind sampleBind : filteredData) {
            XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            for (Grid.Column<SampleBind, ?> column : visibleColumn) {
                short lastCellNum = row.getLastCellNum();
                XSSFCell cell = row.createCell(lastCellNum == -1 ? 0 : lastCellNum);
                Object apply = column.getValueProvider().apply(sampleBind);
                if (apply instanceof Double) {
                    cell.setCellValue(String.format("%.2f", (Double) apply));
                    //cell.setCellType(CellType.NUMERIC);
                } else if (apply instanceof LocalDate) {
                    cell.setCellValue(apply.toString());
                    //cell.setCellType(CellType.NUMERIC);
                } else {
                    cell.setCellValue(apply == null ? "" : apply.toString());
                }
            }
        }
        IntStream.range(0, header.getLastCellNum()).forEach(sheet::autoSizeColumn);
        workbook.write(outputStream);
    }
}
