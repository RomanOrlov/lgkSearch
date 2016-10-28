package lgk.nsbc.view.searchview;

import com.vaadin.ui.TextArea;
import com.vaadin.ui.Window;

public class SQLViewWindow extends Window {
    private TextArea sqlRequest = new TextArea();

    public SQLViewWindow(String caption) {
        super(caption);
        setHeight("600px");
        setWidth("700px");
        sqlRequest.setSizeFull();
        sqlRequest.setEnabled(false);
    }

    public void refreshSQLRequest(String sql) {
        sqlRequest.setValue(sql);
    }
}
