package lgk.nsbc.presenter;

import com.vaadin.data.util.BeanItemContainer;
import lgk.nsbc.backend.info.DisplayedInfo;
import lgk.nsbc.backend.info.SampleAdapter;
import lgk.nsbc.backend.info.criteria.Criteria;
import lgk.nsbc.backend.info.searchable.SearchTarget;
import lgk.nsbc.view.searchview.SearchView;

import java.util.Collection;
import java.util.List;

public interface SearchPresenter {
    /* CRUD sample*/
    void createNewSample(String name, String comment);

    void handleLookUpSample();

    void handleAddToSample(Collection collection);

    void handleDeleteCurrentSample();

    BeanItemContainer<SampleAdapter> getSamples();

    List<DisplayedInfo> getDisplayInfo();

    List<Criteria> getAvailableCriteria();

    void handleSampleChanged(SampleAdapter sampleAdapter);

    void changeCurrentSearchTarget(SearchTarget searchTarget);

    void handleDeleteSampleData(Collection<Object> selectedRows);

    void acceptCriteriaChange(List<Criteria> selectColumns);

    void acceptDisplayedInfoChange(List<DisplayedInfo> selectColumns);

    void handleFindButtonClick();

    void handleExportToExcel();

    void setSearchView(SearchView searchView);
}