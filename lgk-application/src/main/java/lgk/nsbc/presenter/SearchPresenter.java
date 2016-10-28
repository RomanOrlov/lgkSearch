package lgk.nsbc.presenter;

import lgk.nsbc.backend.Target;
import lgk.nsbc.backend.samples.Sample;
import lgk.nsbc.backend.search.dbsearch.Criteria;
import lgk.nsbc.backend.search.dbsearch.SelectColumn;
import com.vaadin.data.util.BeanItemContainer;

import java.util.Collection;
import java.util.List;

public interface SearchPresenter {
    void handleAddToSample(Collection collection);
    void createNewSample(String name, String comment);
    void handleDeleteCurrentSample();
    void handleDeleteSampleData(Collection<Object> selectedRows);
    void acceptSelectColumnsChange(List<SelectColumn> selectColumns);
    void acceptCriteriaChange(List<Criteria> selectColumns);
    void handleLookUpSample();

    BeanItemContainer<Sample> getSamples();
    void handleFindButtonClick();

    void handleExportToExcel();
    void changeCurrentSearchTarget(Target searchTarget);

    void handleSampleChanged(Sample sample);
    List<Criteria> getAllCriteria();

    List<SelectColumn> getDisplayInfo();
}