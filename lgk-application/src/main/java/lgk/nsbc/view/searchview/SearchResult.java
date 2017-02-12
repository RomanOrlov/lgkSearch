package lgk.nsbc.view.searchview;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.Grid;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static lgk.nsbc.view.Util.getColumnFilter;

@VaadinSessionScope
@Component
public class SearchResult extends Grid {

    @PostConstruct
    protected void init() {
        setVisible(false);
        addFooterRowAt(0).setStyleName("primary");
        addHeaderRowAt(0).setStyleName("primary");
        setSizeFull();
        setColumnReorderingAllowed(true);
        setSelectionMode(Grid.SelectionMode.MULTI);
    }

    public void refreshGrid(IndexedContainer container) {
        setVisible(true);
        setEnabled(true);
        removeAllColumns();
        setContainerDataSource(container);
        Grid.HeaderRow headerRow = getHeaderRow(0);
        Object firstColumnId = null;
        for (Grid.Column column : getColumns()) {
            column.setHidable(true);
            Object columnId = column.getPropertyId();
            Grid.HeaderCell headerCell = headerRow.getCell(columnId);
            headerCell.setComponent(getColumnFilter(this, columnId));
            headerCell.setStyleName("filter-header");
            if (firstColumnId == null) {
                firstColumnId = columnId;
            }
        }
        Grid.FooterRow footerRow = getFooterRow(0);
        footerRow.getCell(firstColumnId).setText("Количество результатов: " + container.size());
    }
}