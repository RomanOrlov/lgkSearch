package lgk.nsbc.view.searchview;

import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import lgk.nsbc.backend.info.DisplayedInfo;
import lgk.nsbc.backend.info.SampleAdapter;
import lgk.nsbc.backend.info.searchable.SearchTarget;

import java.util.List;
import java.util.function.Consumer;

public interface SearchView extends View, Component {
    void showResults(IndexedContainer container);

    void refreshCriteria();

    void refreshDisplayInfo();

    List<DisplayedInfo> getOrderedSelections();


    void changeSearchTarget(SearchTarget searchTarget);

    void changeCurrentSample(SampleAdapter sampleAdapter);

    /**
     * При изменени выборки, в случае если это уже существующая, необходимо разблокировать
     * некоторые кнопки. И напротив, в случае, если это новая выборка, сделать некоторые
     * компоненты недоступными
     *
     * @param enable Сделать доступными компоненты
     */
    void setSampleComponentsEnable(boolean enable);

    void refreshLastSQLRequest(String sql);

    void showErrorMessage(String text);

    void showNewSampleDialog();

    void showConfirmDialog(String caption, String message, Consumer<Boolean> booleanConsumer);
}
