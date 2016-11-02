package lgk.nsbc.backend;

import lgk.nsbc.backend.samples.Sample;
import lgk.nsbc.backend.samples.SampleData;
import lgk.nsbc.backend.search.dbsearch.Criteria;
import lgk.nsbc.backend.search.dbsearch.SelectColumn;
import lgk.nsbc.generated.tables.records.SelectionsCriteriaRecord;
import lgk.nsbc.generated.tables.records.SelectionsDataRecord;
import lgk.nsbc.generated.tables.records.SelectionsViewRecord;
import org.jooq.InsertValuesStep3;
import org.jooq.InsertValuesStep4;
import org.jooq.Record3;
import org.jooq.Result;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static lgk.nsbc.generated.Sequences.*;
import static lgk.nsbc.generated.tables.Selections.SELECTIONS;
import static lgk.nsbc.generated.tables.SelectionsCriteria.SELECTIONS_CRITERIA;
import static lgk.nsbc.generated.tables.SelectionsData.SELECTIONS_DATA;
import static lgk.nsbc.generated.tables.SelectionsView.SELECTIONS_VIEW;
import static org.jooq.impl.DSL.val;

/**
 * Класс, отвечающий за CRUD операции выборок, а также данных этих выборок.
 * Created by Роман on 11.07.2016.
 */
@Service
public class SamplesManager {

    /**
     * Вытягивание всех выборок для данного пользователя
     * @param agent_n id пользователя
     * @return Выборки пользователя
     */
    public Collection<Sample> getSamples(long agent_n) throws SQLException{
        Collection<Sample> samples = DB.getDSLContext()
                .select()
                .from(SELECTIONS)
                .where(SELECTIONS.USER_ID.eq(agent_n))
                .fetch().into(Sample.class);
        loadSampleParams(samples);
        // Возможно, не лучшее решение (Надо подгружать по мере необходимости)
        for (Sample sample : samples) {
            sample.setSampleData(getSampleData(sample.getN()));
        }
        return samples;
    }

    /**
     * Добавление новой выборки
     * @param sample выборка
     */
    public void addSample(Sample sample) throws SQLException {
        Long nextID = DB.getDSLContext().select(SELECTIONS_N.nextval()).fetchOne().value1();
        sample.setN(nextID);
        DB.getDSLContext().insertInto(SELECTIONS,
                SELECTIONS.N,
                SELECTIONS.USER_ID,
                SELECTIONS.SELECTION_TYPE,
                SELECTIONS.NAME,
                SELECTIONS.COMMENTS)
                .values(val(sample.getN()),
                        val(sample.getUser_id()),
                        val(sample.getSelection_type()),
                        val(sample.getName()),
                        val(sample.getComments()))
                .execute();
        saveCriteria(sample);
        saveSelectColumns(sample);
    }

    /**
     * Удаление выборки
     * @param sample_n номер выборки
     */
    public void deleteSample(long sample_n) throws SQLException{
        // Внимание! Тут нужно удалять не только выборку, но и связанные с ней данные
        // Удаляем все связанные с выборкой элементы
        DB.getDSLContext().deleteFrom(SELECTIONS_DATA).where(SELECTIONS_DATA.SELECTION_ID.eq(sample_n)).execute();
        // Удаляем запись о выборке
        DB.getDSLContext().deleteFrom(SELECTIONS).where(SELECTIONS.N.eq(sample_n)).execute();
        // Удаляем связанные с ней критерии
        deleteCriteria(sample_n);
        // Удаляем выводимые столбцы
        deleteSelectColumns(sample_n);
    }

    /**
     * Вытягивание данных выборки
     * @param sample_n номер выборки
     * @return данные выборки
     */
    public List<SampleData> getSampleData(long sample_n) throws SQLException {
        Result<Record3<Long,Long,Long>> result = DB.getDSLContext()
                .select(SELECTIONS_DATA.N,SELECTIONS_DATA.SELECTION_ID,SELECTIONS_DATA.UNIQUE_ID)
                .from(SELECTIONS_DATA)
                .where(SELECTIONS_DATA.SELECTION_ID.eq(sample_n))
                .fetch();
        List<SampleData> sampleDatas = new ArrayList<>(result.size());
        for (Record3<Long,Long,Long> record3 : result) {
            sampleDatas.add(new SampleData(record3.value1(),record3.value2(),record3.value3()));
        }
        return sampleDatas;
        /*return DB.getDSLContext()
                .select()
                .from(SELECTIONS_DATA)
                .where(SELECTIONS_DATA.SELECTION_ID.eq(sample_n))
                .fetchInto(SampleData.class);*/
    }

    /**
     * Добавление данных в выборку
     * @param sampleData Данные выборки
     */
    public void addSampleData(Collection<SampleData> sampleData) throws SQLException {
        for (SampleData data : sampleData) {
            data.setN(DB.getDSLContext().select(SELECTIONS_DATA_N.nextval()).fetchOne().value1());
        }
        // Вставляем данные одной транзакцией
        InsertValuesStep3<SelectionsDataRecord,Long,Long,Long> insertStep = DB.getDSLContext()
                .insertInto(SELECTIONS_DATA,
                        SELECTIONS_DATA.N,
                        SELECTIONS_DATA.SELECTION_ID,
                        SELECTIONS_DATA.UNIQUE_ID);
        for (SampleData data : sampleData) {
            insertStep = insertStep.values(data.getN(),data.getSelection_id(),data.getUnique_id());
        }
        insertStep.execute();
    }

    /**
     * Удаление данных из выборки (Сначала вытаскиваем все уникальные номера, потом удаляем их)
     * @param sampleData Данные выборки, которые необходимо удалить
     */
    public void removeFromSample(Collection<SampleData> sampleData) throws SQLException {
        List<Long> sampleDataN = sampleData.stream().map(SampleData::getN).collect(Collectors.toList());
        DB.getDSLContext()
                .deleteFrom(SELECTIONS_DATA)
                .where(SELECTIONS_DATA.N.in(sampleDataN))
                .execute();
    }

    public void saveSelectColumns(Sample sample) throws SQLException{
        List<SelectColumn> selectColumns = sample.getOnlySelectedColumn();
        for (SelectColumn column : selectColumns) {
            column.setN(DB.getDSLContext().select(SELECTIONS_VIEW_N.nextval()).fetchOne().value1());
        }
        InsertValuesStep3<SelectionsViewRecord,Long,Long,Integer> insertStep = DB.getDSLContext()
                .insertInto(SELECTIONS_VIEW,
                        SELECTIONS_VIEW.N,
                        SELECTIONS_VIEW.SAMPLE_N,
                        SELECTIONS_VIEW.VIEW_ID);
        for (SelectColumn column: selectColumns) {
            insertStep = insertStep.values(column.getN(),sample.getN(),column.getUniqueNum());
        }
        insertStep.execute();
    }

    /** Удаляем всю информацию о выбранных столбцах
     * @param sample_id уникальный id выборки
     * @throws SQLException
     */
    public void deleteSelectColumns(long sample_id) throws SQLException{
        DB.getDSLContext()
                .deleteFrom(SELECTIONS_VIEW)
                .where(SELECTIONS_VIEW.SAMPLE_N.eq(sample_id))
                .execute();
    }

    /**
     * Удаляем всю информацию о настроенных критериях
     * @param sample_id уникальный id выборки
     */
    public void deleteCriteria(long sample_id) throws SQLException{
        DB.getDSLContext()
                .deleteFrom(SELECTIONS_CRITERIA)
                .where(SELECTIONS_CRITERIA.SAMPLE_N.eq(sample_id))
                .execute();
    }

    public void saveCriteria(Sample sample) throws SQLException{
        List<Criteria> criteriaList = sample.getOnlySelectedCriteria();
        for (Criteria criteria : criteriaList) {
            criteria.setN(DB.getDSLContext().select(SELECTIONS_CRITERIA_N.nextval()).fetchOne().value1());
        }
        InsertValuesStep4<SelectionsCriteriaRecord,Long,Long,Integer,String> insertStep = DB.getDSLContext()
                .insertInto(SELECTIONS_CRITERIA,
                        SELECTIONS_CRITERIA.N,
                        SELECTIONS_CRITERIA.SAMPLE_N,
                        SELECTIONS_CRITERIA.CRITERIA_ID,
                        SELECTIONS_CRITERIA.STRING_VALUE);
        for (Criteria criteria: sample.getOnlySelectedCriteria()) {
            insertStep = insertStep.values(criteria.getN(),sample.getN(),criteria.getUniqueNum(),criteria.getCriteriaStringValue());
        }
        insertStep.execute();
    }

    public void loadSampleParams(Collection<Sample> samples) throws SQLException{
        for (Sample sample : samples) {
            sample.setViewInfo(DB.getDSLContext()
                    .select(SELECTIONS_VIEW.VIEW_ID)
                    .from(SELECTIONS_VIEW)
                    .where(SELECTIONS_VIEW.SAMPLE_N.eq(sample.getN()))
                    .fetch(SELECTIONS_VIEW.VIEW_ID));
            sample.setCriteriaParams(DB.getDSLContext()
            .selectFrom(SELECTIONS_CRITERIA)
                    .where(SELECTIONS_CRITERIA.SAMPLE_N.eq(sample.getN()))
                    .fetchMap(SELECTIONS_CRITERIA.CRITERIA_ID,SELECTIONS_CRITERIA.STRING_VALUE));
        }
    }
}