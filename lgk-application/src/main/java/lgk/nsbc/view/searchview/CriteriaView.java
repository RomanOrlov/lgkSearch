package lgk.nsbc.view.searchview;

import lgk.nsbc.backend.search.dbsearch.Criteria;

import java.util.List;

public interface CriteriaView {

    void setUpCriteria();

    void refreshCriteriaData(List<Criteria> criteriaList);
}
