package lgk.nsbc.spect.util.excel;

import lgk.nsbc.model.Target;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.model.spect.TargetType;
import lgk.nsbc.spect.util.DateUtils;
import lgk.nsbc.spect.view.spectcrud.SpectGrid;
import lgk.nsbc.spect.view.spectcrud.SpectGridData;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.IntStream;

import static lgk.nsbc.model.spect.MainInfo.*;

public class SpectDataExcelExporter extends ExcelExportButton {
    private final SpectGrid grid;

    public SpectDataExcelExporter(SpectGrid grid, String caption) {
        super(caption);
        this.grid = grid;
    }


    @Override
    protected void exportToExcel(OutputStream outputStream) throws IOException {
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
            XSSFCell dose = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell studyDate = row.createCell(cellIndex++, CellType.NUMERIC);
            XSSFCell target = row.createCell(cellIndex++, CellType.STRING);

            XSSFCell hizSphereVolume = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell hizSphereMin30 = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell hizSphereMin60 = row.createCell(cellIndex++, CellType.STRING);

            XSSFCell hizIsoline10Volume = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell hizIsoline10Min30 = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell hizIsoline10Min60 = row.createCell(cellIndex++, CellType.STRING);

            XSSFCell hizIsoline25Volume = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell hizIsoline25Min30 = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell hizIsoline25Min60 = row.createCell(cellIndex++, CellType.STRING);

            XSSFCell hizIsoline50Volume = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell hizIsoline50Min30 = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell hizIsoline50Min60 = row.createCell(cellIndex++, CellType.STRING);

            XSSFCell targetSphereVolume = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell targetSphereMin30 = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell targetSphereMin60 = row.createCell(cellIndex++, CellType.STRING);

            XSSFCell targetIsoline10Volume = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell targetIsoline10Min30 = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell targetIsoline10Min60 = row.createCell(cellIndex++, CellType.STRING);

            XSSFCell targetIsoline25Volume = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell targetIsoline25Min30 = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell targetIsoline25Min60 = row.createCell(cellIndex++, CellType.STRING);

            XSSFCell targetIsoline50Volume = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell targetIsoline50Min30 = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell targetIsoline50Min60 = row.createCell(cellIndex++, CellType.STRING);

            XSSFCell hypVolume = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell hypMin30 = row.createCell(cellIndex++, CellType.STRING);
            XSSFCell hypMin60 = row.createCell(cellIndex++, CellType.STRING);

            name.setCellValue(spectGridData.getName());
            surname.setCellValue(spectGridData.getSurname());
            patronymic.setCellValue(spectGridData.getPatronymic());
            setCellValue(dose, spectGridData.getDose());
            studyDate.setCellValue(DateUtils.asDate(spectGridData.getStudyDate()));
            setCellValue(target, spectGridData.getTarget());


            setCellValue(hizSphereVolume, spectGridData.getHizSphereVolume());
            setCellValue(hizSphereMin30, spectGridData.getHizSphereMin30());
            setCellValue(hizSphereMin60, spectGridData.getHizSphereMin60());

            setCellValue(hizIsoline10Volume, spectGridData.getHizIsoline10Volume());
            setCellValue(hizIsoline10Min30, spectGridData.getHizIsoline10Min30());
            setCellValue(hizIsoline10Min60, spectGridData.getHizIsoline10Min60());

            setCellValue(hizIsoline25Volume, spectGridData.getHizIsoline25Volume());
            setCellValue(hizIsoline25Min30, spectGridData.getHizIsoline25Min30());
            setCellValue(hizIsoline25Min60, spectGridData.getHizIsoline25Min60());

            setCellValue(hizIsoline50Volume, spectGridData.getHizIsoline50Volume());
            setCellValue(hizIsoline50Min30, spectGridData.getHizIsoline50Min30());
            setCellValue(hizIsoline50Min60, spectGridData.getHizIsoline50Min60());

            setCellValue(targetSphereVolume, spectGridData.getTargetSphereVolume());
            setCellValue(targetSphereMin30, spectGridData.getTargetSphereMin30());
            setCellValue(targetSphereMin60, spectGridData.getTargetSphereMin60());

            setCellValue(targetIsoline10Volume, spectGridData.getTargetIsoline10Volume());
            setCellValue(targetIsoline10Min30, spectGridData.getTargetIsoline10Min30());
            setCellValue(targetIsoline10Min60, spectGridData.getTargetIsoline10Min60());

            setCellValue(targetIsoline25Volume, spectGridData.getTargetIsoline25Volume());
            setCellValue(targetIsoline25Min30, spectGridData.getTargetIsoline25Min30());
            setCellValue(targetIsoline25Min60, spectGridData.getTargetIsoline25Min60());

            setCellValue(targetIsoline50Volume, spectGridData.getTargetIsoline50Volume());
            setCellValue(targetIsoline50Min30, spectGridData.getTargetIsoline50Min30());
            setCellValue(targetIsoline50Min60, spectGridData.getTargetIsoline50Min60());

            setCellValue(hypVolume, spectGridData.getHypVolume());
            setCellValue(hypMin30, spectGridData.getHypMin30());
            setCellValue(hypMin60, spectGridData.getHypMin60());
            // Наведение красоты
            if (i == 0) {
                for (int k = 0; k < cellIndex; k++) {
                    structureHeader.createCell(k);
                    contourHeader.createCell(k);
                }
                mainInfo.createCell(name.getColumnIndex()).setCellValue("Имя");
                mainInfo.createCell(surname.getColumnIndex()).setCellValue("Фамилия");
                mainInfo.createCell(patronymic.getColumnIndex()).setCellValue("Отчество");
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
                mainInfo.createCell(hizIsoline50Volume.getColumnIndex()).setCellValue(VOLUME.getName());
                mainInfo.createCell(hizIsoline50Min30.getColumnIndex()).setCellValue(MIN30.getName());
                mainInfo.createCell(hizIsoline50Min60.getColumnIndex()).setCellValue(MIN60.getName());

                mainInfo.createCell(targetSphereVolume.getColumnIndex()).setCellValue(VOLUME.getName());
                mainInfo.createCell(targetSphereMin30.getColumnIndex()).setCellValue(MIN30.getName());
                mainInfo.createCell(targetSphereMin60.getColumnIndex()).setCellValue(MIN60.getName());
                mainInfo.createCell(targetIsoline10Volume.getColumnIndex()).setCellValue(VOLUME.getName());
                mainInfo.createCell(targetIsoline10Min30.getColumnIndex()).setCellValue(MIN30.getName());
                mainInfo.createCell(targetIsoline10Min60.getColumnIndex()).setCellValue(MIN60.getName());
                mainInfo.createCell(targetIsoline25Volume.getColumnIndex()).setCellValue(VOLUME.getName());
                mainInfo.createCell(targetIsoline25Min30.getColumnIndex()).setCellValue(MIN30.getName());
                mainInfo.createCell(targetIsoline25Min60.getColumnIndex()).setCellValue(MIN60.getName());
                mainInfo.createCell(targetIsoline50Volume.getColumnIndex()).setCellValue(VOLUME.getName());
                mainInfo.createCell(targetIsoline50Min30.getColumnIndex()).setCellValue(MIN30.getName());
                mainInfo.createCell(targetIsoline50Min60.getColumnIndex()).setCellValue(MIN60.getName());

                mainInfo.createCell(hypVolume.getColumnIndex()).setCellValue(VOLUME.getName());
                mainInfo.createCell(hypMin30.getColumnIndex()).setCellValue(MIN30.getName());
                mainInfo.createCell(hypMin60.getColumnIndex()).setCellValue(MIN60.getName());

                int s = structureHeader.getRowNum();
                sheet.addMergedRegion(new CellRangeAddress(s, s, hizSphereVolume.getColumnIndex(), hizIsoline50Min60.getColumnIndex()));
                structureHeader.getCell(hizSphereVolume.getColumnIndex()).setCellValue(TargetType.HIZ.getName());
                sheet.addMergedRegion(new CellRangeAddress(s, s, hypVolume.getColumnIndex(), hypMin60.getColumnIndex()));
                structureHeader.getCell(hypVolume.getColumnIndex()).setCellValue(TargetType.HYP.getName());
                sheet.addMergedRegion(new CellRangeAddress(s, s, targetSphereVolume.getColumnIndex(), targetIsoline50Min60.getColumnIndex()));
                structureHeader.getCell(targetSphereVolume.getColumnIndex()).setCellValue(TargetType.TARGET.getName());

                int c = contourHeader.getRowNum();
                sheet.addMergedRegion(new CellRangeAddress(c, c, hypVolume.getColumnIndex(), hypMin60.getColumnIndex()));
                contourHeader.getCell(hypVolume.getColumnIndex()).setCellValue(ContourType.SPHERE.getName());

                sheet.addMergedRegion(new CellRangeAddress(c, c, hizSphereVolume.getColumnIndex(), hizSphereMin60.getColumnIndex()));
                contourHeader.getCell(hizSphereVolume.getColumnIndex()).setCellValue(ContourType.SPHERE.getName());
                sheet.addMergedRegion(new CellRangeAddress(c, c, hizIsoline10Volume.getColumnIndex(), hizIsoline10Min60.getColumnIndex()));
                contourHeader.getCell(hizIsoline10Volume.getColumnIndex()).setCellValue(ContourType.ISOLYNE10.getName());
                sheet.addMergedRegion(new CellRangeAddress(c, c, hizIsoline25Volume.getColumnIndex(), hizIsoline25Min60.getColumnIndex()));
                contourHeader.getCell(hizIsoline25Volume.getColumnIndex()).setCellValue(ContourType.ISOLYNE25.getName());
                sheet.addMergedRegion(new CellRangeAddress(c, c, hizIsoline50Volume.getColumnIndex(), hizIsoline50Min60.getColumnIndex()));
                contourHeader.getCell(hizIsoline50Volume.getColumnIndex()).setCellValue(ContourType.ISOLYNE50.getName());

                sheet.addMergedRegion(new CellRangeAddress(c, c, targetSphereVolume.getColumnIndex(), targetSphereMin60.getColumnIndex()));
                contourHeader.getCell(targetSphereVolume.getColumnIndex()).setCellValue(ContourType.SPHERE.getName());
                sheet.addMergedRegion(new CellRangeAddress(c, c, targetIsoline10Volume.getColumnIndex(), targetIsoline10Min60.getColumnIndex()));
                contourHeader.getCell(targetIsoline10Volume.getColumnIndex()).setCellValue(ContourType.ISOLYNE10.getName());
                sheet.addMergedRegion(new CellRangeAddress(c, c, targetIsoline25Volume.getColumnIndex(), targetIsoline25Min60.getColumnIndex()));
                contourHeader.getCell(targetIsoline25Volume.getColumnIndex()).setCellValue(ContourType.ISOLYNE25.getName());
                sheet.addMergedRegion(new CellRangeAddress(c, c, targetIsoline50Volume.getColumnIndex(), targetIsoline50Min60.getColumnIndex()));
                contourHeader.getCell(targetIsoline50Volume.getColumnIndex()).setCellValue(ContourType.ISOLYNE50.getName());

                IntStream.range(0, cellIndex).forEach(sheet::autoSizeColumn);
            }
        }
        workbook.write(outputStream);
    }

    private void setCellValue(XSSFCell cell, Target value) {
        if (value == null)
            cell.setCellType(CellType.BLANK);
        else
            cell.setCellValue(value.toString());
    }

    private void setCellValue(XSSFCell cell, Double value) {
        if (value == null)
            cell.setCellType(CellType.BLANK);
        else
            cell.setCellValue(value);
    }
}
