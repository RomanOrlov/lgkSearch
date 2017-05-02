package lgk.nsbc.histology;

import com.google.gwt.user.client.ui.SuggestBox;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.VerticalLayout;
import lgk.nsbc.model.dao.NbcPatientsDao;
import lgk.nsbc.util.components.SuggestionCombobox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.ws.ServiceMode;

/**
 * Created by asindor on 01.05.17.
 */
@Service
@VaadinSessionScope
public class HistologyView extends VerticalLayout implements View {
    @Autowired
    private NbcPatientsDao nbcPatientsDao;
    private SuggestionCombobox suggestionCombobox;

    @PostConstruct
    public void init() {
        suggestionCombobox = new SuggestionCombobox(nbcPatientsDao::getPatientsWithDifferetNames);

        addComponents(suggestionCombobox);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
