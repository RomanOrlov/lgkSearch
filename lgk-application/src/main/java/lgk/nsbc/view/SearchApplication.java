package lgk.nsbc.view;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import lgk.nsbc.presenter.SearchPresenterImpl;
import lgk.nsbc.view.searchview.SearchViewImpl;

@Title("DB interface")
@Theme("valo")
@SpringUI
public class SearchApplication extends UI {
    private TabSheet tabs = new TabSheet();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        String lgkSessionId = getPage().getUriFragment();
        // To change key add #9ec2104e97256dc639754ae07b5eb7bf
        if (lgkSessionId == null) {
            lgkSessionId = "9ec2104e97256dc639754ae07b5eb7bf";
        }
        getPage().setUriFragment(null, false);
        setContent(tabs);
        tabs.setSizeFull();
        // Раздел для поиска из базы даных
        SearchPresenterImpl searchPresenter = new SearchPresenterImpl(lgkSessionId);
        searchPresenter.setSearchView(new SearchViewImpl(searchPresenter));
        tabs.addTab(searchPresenter.getSearchView(), "Построение выборок");
        // Планируемый раздел для поиска внутри памяти
        tabs.addTab(new Label("Пока здесь ничего нет"), "Работа с выборками");
    }
}
