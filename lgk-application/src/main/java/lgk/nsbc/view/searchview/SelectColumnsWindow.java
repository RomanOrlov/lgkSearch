package lgk.nsbc.view.searchview;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.*;
import lgk.nsbc.backend.info.DisplayedInfo;
import lgk.nsbc.presenter.SearchPresenter;
import lgk.nsbc.view.TwinTablesSelect;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@VaadinSessionScope
@SpringView
public class SelectColumnsWindow extends Window implements View {
    private SearchPresenter searchPresenter;
    private TwinTablesSelect<DisplayedInfo> viewColumnSelect;

    @Autowired
    public SelectColumnsWindow(SearchPresenter searchPresenter) {
        super("Настроить выводимую информацию");
        this.searchPresenter = searchPresenter;
    }

    @PostConstruct
    private void init() {
        viewColumnSelect = new TwinTablesSelect<>(DisplayedInfo.class,
                new ArrayList<>(),
                new ArrayList<>(),
                searchPresenter::acceptDisplayedInfoChange);
        viewColumnSelect.setWidth("100%");
        viewColumnSelect.setHeight("500px");
        setModal(true);
        setWidth("75%");
        setHeight("600px");
        Button accept = new Button("Принять");
        accept.setStyleName("primary");
        Button cancel = new Button("Отмена");
        accept.addClickListener(clickEvent -> {
            viewColumnSelect.acceptChanges();
            close();
        });
        cancel.addClickListener(clickEvent -> close());
        HorizontalLayout buttons = new HorizontalLayout(accept, cancel);
        buttons.setWidth("100%");
        buttons.setSpacing(true);
        buttons.setComponentAlignment(accept, Alignment.MIDDLE_CENTER);
        buttons.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER);
        VerticalLayout components = new VerticalLayout(viewColumnSelect, buttons);
        components.setSpacing(true);
        setContent(components);
    }

    public List<DisplayedInfo> getOrderedSelections() {
        return viewColumnSelect.getOrderedSelections();
    }

    public void refreshDisplayInfo(List<DisplayedInfo> selectColumns) {
        viewColumnSelect.refreshData(selectColumns);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        getUI().addWindow(this);
    }
}
