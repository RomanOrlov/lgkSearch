package lgk.nsbc.util.excel;

import lgk.nsbc.model.ExcelDataPosition;
import lgk.nsbc.model.StudyRecords;
import lgk.nsbc.model.StudyTarget;
import lgk.nsbc.model.Target;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ParserService {
    @Autowired
    private ExcelDataPosition dataPosition;

    public List<StudyRecords> parseSheet(XSSFSheet sheet) {
        List<StudyRecords> studyRecordses = new ArrayList<>();
        int lastRowNum = sheet.getLastRowNum();
        List<Integer> invalidRows = getInvalidRows(sheet, lastRowNum);
        for (int i = dataPosition.getDataStart(); i <= lastRowNum; i++) {
            if (invalidRows.contains(i)) continue;
            List<XSSFRow> studyRows = new ArrayList<>();
            studyRows.add(sheet.getRow(i)); // Every first target not null
            while ((i + 1) < lastRowNum && isNextRowInCurrentStudy(invalidRows, sheet, i)) {
                i++;
                studyRows.add(sheet.getRow(i));
            }
            StudyRecords studyRecords = parseRecords(studyRows);
            studyRecordses.add(studyRecords);
        }
        return studyRecordses;
    }

    private boolean isNextRowInCurrentStudy(List<Integer> invalidRows, XSSFSheet sheet, Integer currentRowIndex) {
        int nextRowIndex = currentRowIndex + 1;
        if (invalidRows.contains(nextRowIndex))
            return false;
        XSSFCell nextStudyNumCell = sheet.getRow(nextRowIndex).getCell(dataPosition.getSpectNumber(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        return nextStudyNumCell == null;
    }

    private List<Integer> getInvalidRows(XSSFSheet sheet, int lastRowNum) {
        List<Integer> invalidRows = new ArrayList<>();
        for (int i = dataPosition.getDataStart(); i < lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            Integer diagnosisPosition = dataPosition.getDiagnosis();
            XSSFCell cell = row.getCell(diagnosisPosition, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null)
                invalidRows.add(i);
        }
        return invalidRows;
    }

    /**
     * @param studyRows все мишени для одного исследования
     * @return распарсенная запись для исследования
     */
    private StudyRecords parseRecords(List<XSSFRow> studyRows) {
        StudyRecords studyRecords = new StudyRecords();
        // Parsing main info
        XSSFRow mainRow = studyRows.get(0);
        studyRecords.setSpectNumder((long) getCell(mainRow, dataPosition.getSpectNumber()).getNumericCellValue());
        studyRecords.setDate(getDate(getCell(mainRow, dataPosition.getDate())));
        studyRecords.setDose(getValue(getCell(mainRow, dataPosition.getDose())));
        studyRecords.setFullName(getCell(mainRow, dataPosition.getFullName()).getStringCellValue());

        List<StudyTarget> targets = new ArrayList<>();
        targets.add(getStudyTarget(mainRow));
        // Parse addition targets;
        if (studyRows.size() > 1) {
            for (int i = 1; i < studyRows.size(); i++) {
                StudyTarget nextTarget = getStudyTarget(studyRows.get(i));
                for (int j = 0; j < nextTarget.getTargets().size(); j++) {
                    Target target = nextTarget.getTargets().get(j);
                    if (target.getVolume() == null) {
                        target.setVolume(targets.get(0).getTargets().get(j).getVolume());
                    }
                }
                targets.add(nextTarget);
            }
        }
        studyRecords.setTargets(targets);
        return studyRecords;
    }

    private Double getValue(XSSFCell cell) {
        if (cell == null) return null;
        try {
            return cell.getNumericCellValue();
        } catch (IllegalStateException e) {
            return Double.valueOf(cell.getStringCellValue().trim());
        }
    }

    private Date getDate(XSSFCell cell) {
        try {
            return cell.getDateCellValue();
        } catch (IllegalStateException e) {
        }
        DateFormat first = new SimpleDateFormat("dd.MM.yyyy");
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols() {
            @Override
            public String[] getMonths() {
                return new String[]{"января", "февраля", "марта", "апреля", "мая",
                        "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
            }
        };
        DateFormat second = new SimpleDateFormat("d MMMM yyyy", dateFormatSymbols);
        try {
            return first.parse(cell.getStringCellValue());
        } catch (ParseException e) {
        }

        try {
            return second.parse(cell.getStringCellValue());
        } catch (ParseException e) {
        }
        throw new RuntimeException("WHAT THE FUCK!! " + cell.getRawValue() + " " + cell.getStringCellValue());
    }

    private StudyTarget getStudyTarget(XSSFRow row) {
        StudyTarget studyTarget = new StudyTarget();
        studyTarget.setDiagnosis(getCell(row, dataPosition.getDiagnosis()).getStringCellValue());
        List<Target> targets = new ArrayList<>();
        for (Target target : dataPosition.getTargetList()) {
            Target newTarget = new Target(target);
            newTarget.setVolume(getValue(row.getCell(target.getDataStart())));
            newTarget.setCountsAfter30min(getValue(row.getCell(target.getDataStart() + 1)));
            newTarget.setCountsAfter60min(getValue(row.getCell(target.getDataStart() + 2)));
            targets.add(newTarget);
        }
        // Убираем хлам, - где нет информации
        targets.removeIf(target -> target.getVolume() == null && target.getCountsAfter30min() == null && target.getCountsAfter60min() == null);
        studyTarget.setTargets(targets);
        return studyTarget;
    }

    private XSSFCell getCell(XSSFRow row, int index) {
        return row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
    }
}
