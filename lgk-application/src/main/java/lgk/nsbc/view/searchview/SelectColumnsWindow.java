package lgk.nsbc.view.searchview;

import lgk.nsbc.backend.search.dbsearch.SelectColumn;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import lgk.nsbc.view.TwinTablesSelect;

import java.util.List;
import java.util.function.Consumer;

public class SelectColumnsWindow extends Window {
    private TwinTablesSelect<SelectColumn> viewColumnSelect;

    public SelectColumnsWindow(String caption, Consumer<List<SelectColumn>> saveToDbSelected) {
        super(caption);
        viewColumnSelect = new TwinTablesSelect<>(
                new BeanItemContainer<>(SelectColumn.class),
                new BeanItemContainer<>(SelectColumn.class),
                saveToDbSelected);
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

    public List<SelectColumn> getOrderedSelections() {
        return viewColumnSelect.getOrderedSelections();
    }

    public void refreshDisplayInfo(List<SelectColumn> selectColumns) {
        viewColumnSelect.refreshData(selectColumns);
    }
}
