package lgk.nsbc.view.searchview;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Window;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@VaadinSessionScope
@SpringView
public class SQLViewWindow extends Window implements View {
    private TextArea sqlRequest = new TextArea();

    public SQLViewWindow() {
        super("Последний SQL запрос");
    }

    @PostConstruct
    private void init() {
        setHeight("600px");
        setWidth("700px");
        sqlRequest.setSizeFull();
        sqlRequest.setEnabled(false);
    }

    public void refreshSQLRequest(String sql) {
        sqlRequest.setValue(sql);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        getUI().addWindow(this);
    }
}
