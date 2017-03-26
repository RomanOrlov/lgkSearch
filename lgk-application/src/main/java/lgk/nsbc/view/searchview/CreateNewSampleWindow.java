package lgk.nsbc.view.searchview;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.v7.data.validator.StringLengthValidator;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import lgk.nsbc.presenter.SearchPresenter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@VaadinSessionScope
@SpringView
public class CreateNewSampleWindow extends Window implements View {
    private SearchPresenter searchPresenter;

    @Autowired
    public CreateNewSampleWindow(SearchPresenter searchPresenter) {
        super("Создать новую выборку");
        this.searchPresenter = searchPresenter;
    }

    @PostConstruct
    private void init() {
        setModal(true);
        setWidth("50%");
        setHeight("50%");
        TextField name = new TextField("Название");
        name.setSizeFull();
        name.setRequired(true);
        name.addValidator(new StringLengthValidator("Название должно быть от 4 до 20 символов", 4, 20, false));
        name.setValidationVisible(true);

        TextField comments = new TextField("Комментарии");
        comments.setSizeFull();

        Button accept = new Button("Принять", event -> {
            if (!name.isValid()) return;
            searchPresenter.createNewSample(name.getValue(), comments.getValue());
            close();
        });
        accept.setStyleName("primary");

        Button cancel = new Button("Отмена", event -> close());

        HorizontalLayout buttons = new HorizontalLayout(accept, cancel);
        buttons.setSizeUndefined();
        buttons.setSpacing(true);

        VerticalLayout content = new VerticalLayout(name, comments, buttons);
        content.setMargin(new MarginInfo(true, true, true, true));
        content.setSpacing(true);
        content.setSizeFull();
        setContent(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        getUI().addWindow(this);
    }
}
