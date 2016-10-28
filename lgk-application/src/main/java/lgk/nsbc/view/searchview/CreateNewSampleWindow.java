package lgk.nsbc.view.searchview;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.util.function.BiConsumer;

public class CreateNewSampleWindow extends Window {

    public CreateNewSampleWindow(String caption, BiConsumer<String, String> createNewSample) {
        super(caption);
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
            createNewSample.accept(name.getValue(), comments.getValue());
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
}
