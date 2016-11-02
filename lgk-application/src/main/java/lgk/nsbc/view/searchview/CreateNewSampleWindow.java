package lgk.nsbc.view.searchview;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.*;
import lgk.nsbc.presenter.SearchPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.function.BiConsumer;

@VaadinSessionScope
@SpringView
public class CreateNewSampleWindow extends Window implements View {
    @Autowired
    private SearchPresenter searchPresenter;

    public CreateNewSampleWindow() {
        super("Создать новую выборку");
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
