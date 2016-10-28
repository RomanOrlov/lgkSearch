package lgk.nsbc.view.searchview;

import lgk.nsbc.backend.Target;
import lgk.nsbc.backend.samples.Sample;
import lgk.nsbc.backend.search.dbsearch.SelectColumn;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.ui.Component;

import java.util.List;
import java.util.function.Consumer;

public interface SearchView extends View,Component {
    void showResults(IndexedContainer container);

    void refreshCriteria();
    void refreshDisplayInfo();
    List<SelectColumn> getOrderedSelections();


    void changeSearchTarget(Target target);
    void changeCurrentSample(Sample sample);

    /**
     * При изменени выборки, в случае если это уже существующая, необходимо разблокировать
     * некоторые кнопки. И напротив, в случае, если это новая выборка, сделать некоторые
     * компоненты недоступными
     * @param enable Сделать доступными компоненты
     */
    void setSampleComponentsEnable(boolean enable);
    void refreshLastSQLRequest(String sql);
    void showErrorMessage(String text);
    void showNewSampleDialog();
    void showConfirmDialog(String caption, String message, Consumer<Boolean> booleanConsumer);
}
