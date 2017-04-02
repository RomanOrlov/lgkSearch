package lgk.nsbc.util.excel;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.server.Extension;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.Grid.Column;
import com.vaadin.ui.Notification;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.model.spect.TargetType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * Экспорт данных в excel в том же виде, в каком они представлены
 * в интерфейсе
 */
public class ExcelExporter extends Button {
    private final Grid grid;
    private final List<Object> exludedProperties = new ArrayList<>();
    private Extension currrentExtension;

    public ExcelExporter(Grid grid, String caption) {
        super(caption);
        this.grid = grid;
        exludedProperties.add("#id");
        StreamResource myResource = createResource();
        FileDownloader fileDownloader = new FileDownloader(myResource);
        fileDownloader.extend(this);
        addClickListener(event -> {
            if (currrentExtension != null) removeExtension(currrentExtension);
            currrentExtension = fileDownloader;
        });

    }

    public void exportToExcel(OutputStream outputStream) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Экспорт данных");
        XSSFRow firstHeader = sheet.createRow(0);
        XSSFRow secondHeader = sheet.createRow(1);
        XSSFRow headers = sheet.createRow(2);

        Container.Indexed dataSource = grid.getContainerDataSource();
        List<?> filteredProperties = dataSource.getContainerPropertyIds()
                .stream()
                .filter(o -> !exludedProperties.contains(o))
                .collect(toList());
        List<Column> columns = filteredProperties.stream()
                .map(grid::getColumn)
                .collect(toList());
        // Задаем заголовки
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            XSSFCell headersCell = headers.createCell(i);
            firstHeader.createCell(i);
            secondHeader.createCell(i);
            headersCell.setCellValue(column.getHeaderCaption());
        }
        // Неопсредственно заливаем данные
        for (int i = 0; i < dataSource.size(); i++) {
            XSSFRow row = sheet.createRow(i + 3);
            Object idByIndex = dataSource.getIdByIndex(i);
            Item item = dataSource.getItem(idByIndex);
            int cellIndex = 0;
            for (Object propertyId : filteredProperties) {
                Object value = item.getItemProperty(propertyId).getValue();
                XSSFCell cell = row.createCell(cellIndex);
                cell.setCellValue(value.toString());
                cellIndex++;
            }
        }
        // Сливаем ячейки в первых двух заголовках для красоты
        createMergedRegionsForTargets(sheet, filteredProperties);
        createMergedRegionsForContours(sheet, filteredProperties);
        workbook.write(outputStream);
        // Выставляем размеры столбцов
        IntStream.range(0, filteredProperties.size()).forEach(sheet::autoSizeColumn);
    }

    private void createMergedRegionsForTargets(XSSFSheet sheet, List<?> properties) {
        for (TargetType targetType : TargetType.values()) {
            List<Integer> indexes = properties.stream()
                    .filter(o -> ((String) o).contains(targetType.toString()))
                    .map(properties::indexOf)
                    .sorted()
                    .collect(toList());
            // То есть если более одной ячейки
            if (indexes.size() > 1) {
                Integer firstIndex = indexes.get(0);
                Integer lastIndex = indexes.get(indexes.size() - 1);
                int regionIndex = sheet.addMergedRegion(new CellRangeAddress(0, 0, firstIndex, lastIndex));
                sheet.getRow(0).getCell(firstIndex).setCellValue(targetType.getName());
            }
        }
    }

    private void createMergedRegionsForContours(XSSFSheet sheet, List<?> properties) {
        for (ContourType contourType : ContourType.values()) {
            List<Integer> indexes = properties.stream()
                    .filter(o -> ((String) o).contains(contourType.toString()))
                    .map(properties::indexOf)
                    .sorted()
                    .collect(toList());
            // То есть если более одной ячейки
            if (indexes.size() > 1) {
                // Группируем проперти
                List<List<Integer>> groupedProperties = new ArrayList<>();
                int propertyIndex = 0;
                while (propertyIndex < indexes.size()) {
                    List<Integer> group = new ArrayList<>();
                    Integer prevIndex = indexes.get(propertyIndex);
                    group.add(prevIndex);
                    while ((propertyIndex+1 < indexes.size()) && (indexes.get(propertyIndex + 1) - prevIndex) == 1) {
                        Integer nextIndex = indexes.get(propertyIndex + 1);
                        group.add(nextIndex);
                        prevIndex = nextIndex;
                        propertyIndex++;
                    }
                    propertyIndex++;
                    groupedProperties.add(group);
                }
                for (List<Integer> groupedProperty : groupedProperties) {
                    // То есть если более одной ячейки
                    if (groupedProperty.size() > 1) {
                        Integer firstIndex = groupedProperty.get(0);
                        Integer lastIndex = groupedProperty.get(groupedProperty.size() - 1);
                        System.out.println(firstIndex + " " + lastIndex);
                        int regionIndex = sheet.addMergedRegion(new CellRangeAddress(1, 1, firstIndex, lastIndex));
                        sheet.getRow(1).getCell(firstIndex).setCellValue(contourType.getName());
                    }
                }
            }
        }
    }

    private StreamResource createResource() {
        return new StreamResource((StreamResource.StreamSource) () -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1_000_000);
            try {
                exportToExcel(byteArrayOutputStream);
                return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                Notification.show("Невозможно загрузить excel file");
                e.printStackTrace();
                return null;
            }
        }, "export.xlsx");
    }

}
