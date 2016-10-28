package lgk.nsbc.backend;

import lgk.nsbc.backend.samples.Sample;
import lgk.nsbc.backend.search.dbsearch.*;

import java.util.*;

import static lgk.nsbc.backend.Target.*;
import static lgk.nsbc.backend.search.dbsearch.CriteriaType.*;
import static lgk.nsbc.generated.tables.BasPeople.BAS_PEOPLE;
import static lgk.nsbc.generated.tables.NbcDiag_2015.*;
import static lgk.nsbc.generated.tables.NbcOrganizations.*;
import static lgk.nsbc.generated.tables.NbcPatients.*;
import static lgk.nsbc.generated.tables.NbcPatientsDiagnosis.*;

/**
 * Класс, содержащий различный набор критериев для поиска.
 * Критерии собираются вручную.
 * Как и в случае с SearchManager, возможно слудет вынести данные критерии в базу или куда то еще.
 * Created by Роман on 08.05.2016.
 */
public class SelectColumnManager {

    // Основной список всех доступных критериев/столбцов
    private HashMap<Integer,SelectColumn> selectColumns = new HashMap<>();

    {
        // PATIENTS
        selectColumns.put(1,new SelectColumn(1,TEXT,"Имя",BAS_PEOPLE.NAME));
        selectColumns.put(2,new SelectColumn(2,TEXT,"Фамилия",BAS_PEOPLE.SURNAME));
        selectColumns.put(3,new SelectColumn(3,TEXT,"Отчество",BAS_PEOPLE.PATRONYMIC));
        selectColumns.put(4,new SelectColumn(4,DATE,"День рождения",BAS_PEOPLE.BIRTHDAY));
        selectColumns.put(5,new SelectColumn(5,LIST,"Основной диагноз",NBC_DIAG_2015.TEXT));
        selectColumns.put(6,new SelectColumn(6,LIST,"Диагноз по LGK",NBC_PATIENTS_DIAGNOSIS.TEXT));
        selectColumns.put(7,new SelectColumn(7,TEXT,"Клинический диагноз",NBC_PATIENTS.FULL_DIAGNOSIS));
        selectColumns.put(8,new SelectColumn(8,LIST,"Организация",NBC_ORGANIZATIONS.NAME));
        selectColumns.put(9,new SelectColumn(9,TEXT,"Пол",BAS_PEOPLE.SEX));
        selectColumns.put(10,new SelectColumn(10,TEXT,"Номер истории болезни", NBC_PATIENTS.CASE_HISTORY_NUM));

    }

    private ArrayList<Criteria> getCriteriaForPatient() {
        ArrayList<Criteria> criteriaForPatient = new ArrayList<>();
        criteriaForPatient.add(new CriteriaText(new SelectColumn(selectColumns.get(1)), PATIENT));
        criteriaForPatient.add(new CriteriaText(new SelectColumn(selectColumns.get(2)), PATIENT));
        criteriaForPatient.add(new CriteriaText(new SelectColumn(selectColumns.get(3)), PATIENT));
        criteriaForPatient.add(new CriteriaDate(new SelectColumn(selectColumns.get(4)), PATIENT));
        criteriaForPatient.add(new CriteriaList(new SelectColumn(selectColumns.get(5)), PATIENT,NBC_DIAG_2015.TEXT,NBC_DIAG_2015.N));
        criteriaForPatient.add(new CriteriaList(new SelectColumn(selectColumns.get(6)), PATIENT,NBC_PATIENTS_DIAGNOSIS.TEXT,NBC_PATIENTS_DIAGNOSIS.N));
        criteriaForPatient.add(new CriteriaText(new SelectColumn(selectColumns.get(7)), PATIENT));
        criteriaForPatient.add(new CriteriaList(new SelectColumn(selectColumns.get(8)), PATIENT,NBC_ORGANIZATIONS.NAME,NBC_ORGANIZATIONS.N));
        return criteriaForPatient;
    }

    private ArrayList<Criteria> getCriteriaForFollowUps() {
        return new ArrayList<>();
    }

    private ArrayList<Criteria> getCriteriaForProcedure() {
        return new ArrayList<>();
    }

    private ArrayList<Criteria> getCriteriaForTargets() {
        return new ArrayList<>();
    }

    private ArrayList<SelectColumn> getSelectColumnsForPatient() {
        ArrayList<SelectColumn> selectColumnsForPatient = new ArrayList<>();
        for (int i=1;i<11;i++) {
            selectColumnsForPatient.add(new SelectColumn(selectColumns.get(i)));
        }
        return selectColumnsForPatient;
    }

    private ArrayList<SelectColumn> getSelectColumnsForProcedure() {
        ArrayList<SelectColumn> selectColumnsForProcedure = new ArrayList<>();
        return selectColumnsForProcedure;
    }

    private ArrayList<SelectColumn> getSelectColumnsForMark() {
        ArrayList<SelectColumn> selectColumnsForMark = new ArrayList<>();
        return selectColumnsForMark;
    }

    private ArrayList<SelectColumn> getSelectColumnsForFollowUp() {
        ArrayList<SelectColumn> selectColumnsForFollowUp = new ArrayList<>();
        return selectColumnsForFollowUp;
    }

    /**
     * @param target Цель критерия
     * @return Список критериев для target
     */
    public ArrayList<Criteria> getCriteria(Target target) {
       switch (target) {
           case PATIENT:
               return getCriteriaForPatient();
           case PROCEDURE:
               return getCriteriaForProcedure();
           case MARK:
               return getCriteriaForTargets();
           case FOLLOWUP:
               return getCriteriaForFollowUps();
       }
        throw new IllegalArgumentException(" Нет информации об этом критерии ");
    }

    public ArrayList<Criteria> getAllCriteria() {
        ArrayList<Criteria> allCriteria = new ArrayList<>();
        for (Target target : Target.values()) {
            allCriteria.addAll(getCriteria(target));
        }
        return allCriteria;
    }

    /**
     * @param searchTarget Цель поиска
     * @return Список доступной информации для вывода
     */
    public ArrayList<SelectColumn> getViewForSearch(Target searchTarget) {
        switch (searchTarget) {
            case PATIENT:
                return getSelectColumnsForPatient();
            case PROCEDURE:
                return getSelectColumnsForProcedure();
            case MARK:
                return getSelectColumnsForMark();
            case FOLLOWUP:
                return getSelectColumnsForFollowUp();
        }
        throw new IllegalArgumentException(" Неизвестная цель ");
    }

    public void parseSamples(Collection<Sample> samples) {
        samples.forEach(this::parseSample);
    }

    /**
     * Каждой выборке выдается набор SelectColumn и создаются все критерии,
     * далее для загруженных из базы данных воссоздаются критерии, и выставляются выбранными
     * Тоже самое и для SelectColumn
     * @param sample выборка
     */
    private void parseSample(Sample sample) {
        // Парсим выводимые столбцы
        List<SelectColumn> selectColumn = getViewForSearch(sample.getSearchTarget());
        selectColumn.stream()
                .filter(column -> sample.getViewInfo().contains((int) column.getUniqueNum()))
                .forEach(column -> column.setSelected(true));
        sample.setAllSelectColumns(selectColumn);
        // Парсим критерии
        ArrayList<Criteria> allCriteria = getAllCriteria();
        allCriteria.stream()
                .filter(criteria -> sample.getCriteriaParams().containsKey((int)criteria.getUniqueNum()))
                .forEach(criteria -> {
                    criteria.parseCriteriaStringValue(sample.getCriteriaParams().get((int)criteria.getUniqueNum()));
                    criteria.setSelected(true);
                });
        sample.setAllCriteria(allCriteria);
    }
}