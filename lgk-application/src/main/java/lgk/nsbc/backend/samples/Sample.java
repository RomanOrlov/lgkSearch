package lgk.nsbc.backend.samples;

import lgk.nsbc.backend.Target;
import lgk.nsbc.backend.search.dbsearch.Criteria;
import lgk.nsbc.backend.search.dbsearch.SelectColumn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Bean для выборок
 * Помимо обычных параметров, вроде уникального номера, имени, комментария,
 * выборке принадлежит информация о выбранных столбцах, критериях.
 */
public class Sample {
    private long n;
    private long user_id;
    private long selection_type;
    private String name;
    private String comments;
    private Target searchTarget;

    /* Информация о выводимых столбцах (какие столбцы выводились в последний раз)
        * Далее сравниваем с текущими выбранными столбацми и в случае различия перезаписываем*/
    private List<Integer> viewInfo = new ArrayList<>();

    /* Информация о критериях, подгружаемая из базы
    * Используется для восстановления критериев,
    * а также в дальнейшем для сохранения в базу */
    private Map<Integer,String> criteriaParams = new HashMap<>();

    // Все критерии
    private List<Criteria> allCriteria;
    // Все доступные выводы столбцов
    private List<SelectColumn> allSelectColumns;

    // Данные выборки.
    private List<SampleData> sampleData = new ArrayList<>();

    public Sample() {}

    public Sample(String name, String comments, long user_id, long selection_type) {
        this.name = name;
        this.comments = comments;
        this.user_id = user_id;
        this.selection_type = selection_type;
    }

    //region A lot of Set&Get Stuff
    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSelection_type() {
        return selection_type;
    }

    public void setSelection_type(long selection_type) {
        this.selection_type = selection_type;
    }

    public List<Integer> getViewInfo() {
        return viewInfo;
    }

    public Map<Integer, String> getCriteriaParams() {
        return criteriaParams;
    }

    public List<SampleData> getSampleData() {
        return sampleData;
    }

    public void setSampleData(List<SampleData> sampleData) {
        this.sampleData = sampleData;
    }

    public List<Criteria> getAllCriteria() {
        return allCriteria;
    }

    public void setAllCriteria(List<Criteria> allCriteria) {
        this.allCriteria = allCriteria;
    }

    public List<SelectColumn> getAllSelectColumns() {
        return allSelectColumns;
    }

    public void setAllSelectColumns(List<SelectColumn> allSelectColumns) {
        this.allSelectColumns = allSelectColumns;
    }

    /**
     * Сохраняем информацию о выбранных столбцах, и сразу же превращаем её в SelectColumns
     * @param viewInfo Загруженные из базы данные о выбранных столбцах
     */
    public void setViewInfo(List<Integer> viewInfo) {
        this.viewInfo = viewInfo;
    }

    /**
     * @param criteriaParams Загруженные из базы пара номер критерия и его значение
     */
    public void setCriteriaParams(Map<Integer, String> criteriaParams) {
        this.criteriaParams = criteriaParams;
    }

    public Target getSearchTarget() {
        if (searchTarget==null) {
            searchTarget = Target.values()[(int)selection_type];
        }
        return searchTarget;
    }

    public void setSearchTarget(Target searchTarget) {
        this.searchTarget = searchTarget;
    }

    public List<Criteria> getOnlySelectedCriteria() {
        return allCriteria.stream()
                .filter(Criteria::isSelected)
                .filter(criteria -> criteria.getCondition()!=null)
                .collect(Collectors.toList());
    }

    public List<SelectColumn> getOnlySelectedColumn() {
        return allSelectColumns.stream()
                .filter(SelectColumn::isSelected)
                .collect(Collectors.toList());
    }
    //endregion

    @Override
    public boolean equals(Object obj) {
        if (obj==null||!(obj instanceof Sample)) {
            return false;
        } else {
            return ((Sample) obj).getN() == this.getN();
        }
    }

    @Override
    public String toString() {
        return name+" "+(comments==null?"":comments);
    }
}
