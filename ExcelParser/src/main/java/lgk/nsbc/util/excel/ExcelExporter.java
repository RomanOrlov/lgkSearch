package lgk.nsbc.util.excel;

import com.vaadin.server.Extension;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import lgk.nsbc.util.DateUtils;
import lgk.nsbc.view.spectcrud.SpectGrid;
import lgk.nsbc.view.spectcrud.SpectGridData;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.IntStream;

import static lgk.nsbc.model.spect.MainInfo.*;

/**
 * Экспорт данных в excel в том же виде, в каком они представлены
 * в интерфейсе
 */
public class ExcelExporter extends Button {
    private final SpectGrid grid;
    private Extension currrentExtension;

    public ExcelExporter(SpectGrid grid, String caption) {
        super(caption);
        this.grid = grid;
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
        XSSFRow structureHeader = sheet.createRow(0);
        XSSFRow contourHeader = sheet.createRow(1);
        XSSFRow mainInfo = sheet.createRow(2);
        List<SpectGridData> allItems = grid.getAllItems();
        for (int i = 0; i < allItems.size(); i++) {
            SpectGridData spectGridData = allItems.get(i);
            XSSFRow row = sheet.createRow(i + 3);
            int cellIndex = 0;
            XSSFCell name = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell surname = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell patronymic = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell caseHisNum = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell dose = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell studyDate = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell target = row.createCell(cellIndex++, CellType.STRING);

            XSSFCell hizSphereVolume = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell hizSphereMin30 = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell hizSphereMin60 = row.createCell(cellIndex++, CellType.NUMERIC);

            XSSFCell hizIsoline10Volume = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell hizIsoline10Min30 = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell hizIsoline10Min60 = row.createCell(cellIndex++, CellType.NUMERIC);

            XSSFCell hizIsoline25Volume = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell hizIsoline25Min30 = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell hizIsoline25Min60 = row.createCell(cellIndex++, CellType.NUMERIC);

            XSSFCell targetSphereVolume = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell targetSphereMin30 = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell targetSphereMin60 = row.createCell(cellIndex++, CellType.NUMERIC);

            XSSFCell targetIsoline10Volume = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell targetIsoline10Min30 = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell targetIsoline10Min60 = row.createCell(cellIndex++, CellType.NUMERIC);

            XSSFCell targetIsoline25Volume = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell targetIsoline25Min30 = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell targetIsoline25Min60 = row.createCell(cellIndex++, CellType.NUMERIC);

            XSSFCell hypVolume = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell hypMin30 = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell hypMin60 = row.createCell(cellIndex++, CellType.NUMERIC);

            name.setCellValue(spectGridData.getName());
            surname.setCellValue(spectGridData.getSurname());
            patronymic.setCellValue(spectGridData.getPatronymic());
            caseHisNum.setCellValue(spectGridData.getCaseHistoryNum());
            dose.setCellValue(spectGridData.getDose());
            studyDate.setCellValue(DateUtils.asDate(spectGridData.getStudyDate()));
            target.setCellValue(spectGridData.getTarget().toString());

            hizSphereVolume.setCellValue(spectGridData.getHizSphereVolume());
            hizSphereMin30.setCellValue(spectGridData.getHizSphereMin30());
            hizSphereMin60.setCellValue(spectGridData.getHizSphereMin60());

            hizIsoline10Volume.setCellValue(spectGridData.getHizIsoline10Volume());
            hizIsoline10Min30.setCellValue(spectGridData.getHizIsoline10Min30());
            hizIsoline10Min60.setCellValue(spectGridData.getHizIsoline10Min60());

            hizIsoline25Volume.setCellValue(spectGridData.getHizIsoline25Volume());
            hizIsoline25Min30.setCellValue(spectGridData.getHizIsoline25Min30());
            hizIsoline25Min60.setCellValue(spectGridData.getHizIsoline25Min60());

            targetSphereVolume.setCellValue(spectGridData.getTargetSphereVolume());
            targetSphereMin30.setCellValue(spectGridData.getTargetSphereMin30());
            targetSphereMin60.setCellValue(spectGridData.getTargetSphereMin60());

            targetIsoline10Volume.setCellValue(spectGridData.getTargetIsoline10Volume());
            targetIsoline10Min30.setCellValue(spectGridData.getTargetIsoline10Min30());
            targetIsoline10Min60.setCellValue(spectGridData.getTargetIsoline10Min60());

            targetIsoline25Volume.setCellValue(spectGridData.getTargetIsoline25Volume());
            targetIsoline25Min30.setCellValue(spectGridData.getTargetIsoline25Min30());
            targetIsoline25Min60.setCellValue(spectGridData.getTargetIsoline25Min60());

            hypVolume.setCellValue(spectGridData.getHypVolume());
            hypMin30.setCellValue(spectGridData.getHypMin30());
            hypMin60.setCellValue(spectGridData.getHypMin60());
            // Наведение красоты
            if (i == 0) {
                for (int k = 0; k < cellIndex; k++) {
                    structureHeader.createCell(k);
                    contourHeader.createCell(k);
                }
                mainInfo.createCell(name.getColumnIndex()).setCellValue("Имя");
                mainInfo.createCell(surname.getColumnIndex()).setCellValue("Фамилия");
                mainInfo.createCell(patronymic.getColumnIndex()).setCellValue("Отчество");
                mainInfo.createCell(caseHisNum.getColumnIndex()).setCellValue("Номер истории");
                mainInfo.createCell(dose.getColumnIndex()).setCellValue("Доза");
                mainInfo.createCell(studyDate.getColumnIndex()).setCellValue("Дата исследования");
                mainInfo.createCell(target.getColumnIndex()).setCellValue("Мишень");

                mainInfo.createCell(hizSphereVolume.getColumnIndex()).setCellValue(VOLUME.getName());
                mainInfo.createCell(hizSphereMin30.getColumnIndex()).setCellValue(MIN30.getName());
                mainInfo.createCell(hizSphereMin60.getColumnIndex()).setCellValue(MIN60.getName());
                mainInfo.createCell(hizIsoline10Volume.getColumnIndex()).setCellValue(VOLUME.getName());
                mainInfo.createCell(hizIsoline10Min30.getColumnIndex()).setCellValue(MIN30.getName());
                mainInfo.createCell(hizIsoline10Min60.getColumnIndex()).setCellValue(MIN60.getName());
                mainInfo.createCell(hizIsoline25Volume.getColumnIndex()).setCellValue(VOLUME.getName());
                mainInfo.createCell(hizIsoline25Min30.getColumnIndex()).setCellValue(MIN30.getName());
                mainInfo.createCell(hizIsoline25Min60.getColumnIndex()).setCellValue(MIN60.getName());

                mainInfo.createCell(targetSphereVolume.getColumnIndex()).setCellValue(VOLUME.getName());
                mainInfo.createCell(targetSphereMin30.getColumnIndex()).setCellValue(MIN30.getName());
                mainInfo.createCell(targetSphereMin60.getColumnIndex()).setCellValue(MIN60.getName());
                mainInfo.createCell(targetIsoline10Volume.getColumnIndex()).setCellValue(VOLUME.getName());
                mainInfo.createCell(targetIsoline10Min30.getColumnIndex()).setCellValue(MIN30.getName());
                mainInfo.createCell(targetIsoline10Min60.getColumnIndex()).setCellValue(MIN60.getName());
                mainInfo.createCell(targetIsoline25Volume.getColumnIndex()).setCellValue(VOLUME.getName());
                mainInfo.createCell(targetIsoline25Min30.getColumnIndex()).setCellValue(MIN30.getName());
                mainInfo.createCell(targetIsoline25Min60.getColumnIndex()).setCellValue(MIN60.getName());

                mainInfo.createCell(hypVolume.getColumnIndex()).setCellValue(VOLUME.getName());
                mainInfo.createCell(hypMin30.getColumnIndex()).setCellValue(MIN30.getName());
                mainInfo.createCell(hypMin60.getColumnIndex()).setCellValue(MIN60.getName());

                int structure = structureHeader.getRowNum();
                sheet.addMergedRegion(new CellRangeAddress(structure, structure, hizSphereVolume.getColumnIndex(), hizIsoline25Min60.getColumnIndex()));
                sheet.addMergedRegion(new CellRangeAddress(structure, structure, hypVolume.getColumnIndex(), hypMin60.getColumnIndex()));
                sheet.addMergedRegion(new CellRangeAddress(structure, structure, targetSphereVolume.getColumnIndex(), targetIsoline25Min60.getColumnIndex()));

                int contour = contourHeader.getRowNum();
                sheet.addMergedRegion(new CellRangeAddress(contour, contour, hypVolume.getColumnIndex(), hypMin60.getColumnIndex()));
                sheet.addMergedRegion(new CellRangeAddress(contour, contour, hizSphereVolume.getColumnIndex(), hizSphereMin60.getColumnIndex()));
                sheet.addMergedRegion(new CellRangeAddress(contour, contour, hizIsoline10Volume.getColumnIndex(), hizIsoline10Min60.getColumnIndex()));
                sheet.addMergedRegion(new CellRangeAddress(contour, contour, hizIsoline25Volume.getColumnIndex(), hizIsoline25Min60.getColumnIndex()));
                sheet.addMergedRegion(new CellRangeAddress(contour, contour, targetSphereVolume.getColumnIndex(), targetSphereMin60.getColumnIndex()));
                sheet.addMergedRegion(new CellRangeAddress(contour, contour, targetIsoline10Volume.getColumnIndex(), targetIsoline10Min60.getColumnIndex()));
                sheet.addMergedRegion(new CellRangeAddress(contour, contour, targetIsoline25Volume.getColumnIndex(), targetIsoline25Min60.getColumnIndex()));

                IntStream.range(0, cellIndex).forEach(sheet::autoSizeColumn);
            }
        }
        workbook.write(outputStream);
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
