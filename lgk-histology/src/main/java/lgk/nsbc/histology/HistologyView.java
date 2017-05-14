package lgk.nsbc.histology;

import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Setter;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lgk.nsbc.model.dao.PatientsDao;
import lgk.nsbc.model.dao.dictionary.DicYesNoDao;
import lgk.nsbc.model.dao.dictionary.GenesDao;
import lgk.nsbc.model.dao.dictionary.MutationTypesDao;
import lgk.nsbc.util.components.SuggestionCombobox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.dialogs.ConfirmDialog;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@VaadinSessionScope
public class HistologyView extends VerticalLayout implements View {
    @Autowired
    private PatientsDao patientsDao;
    @Autowired
    private GenesDao genesDao;
    @Autowired
    private DicYesNoDao dicYesNoDao;
    @Autowired
    private MutationTypesDao mutationTypesDao;
    @Autowired
    private HistologyManager histologyManager;

    private SuggestionCombobox suggestionCombobox;
    private List<MutationBind> mutations = new ArrayList<>();
    private Grid<MutationBind> mutationGrid = new Grid<>("Мутации", mutations);

    private List<HistologyBind> histology = new ArrayList<>();
    private Grid<HistologyBind> histologyGrid = new Grid<>("Список гистологий", histology);

    private Binder<HistologyBind> binder = new Binder<>();
    private Boolean editiMode = false;
    private HistologyBind selectedToEdit;

    @PostConstruct
    public void init() {
        suggestionCombobox = new SuggestionCombobox(patientsDao::getPatientsWithDifferetNames);
        suggestionCombobox.focus();
        suggestionCombobox.addValueChangeListener(event -> {
            clearAll();
            List<HistologyBind> histologyBindList = histologyManager.getHistology(event.getValue());
            histology.addAll(histologyBindList);
            histologyGrid.getDataProvider().refreshAll();
        });

        Button showSelectedHistology = new Button("Просмотр");
        showSelectedHistology.addClickListener(event -> edit());

        Button addHistology = new Button("Добавить");

        Button removeHistology = new Button("Удалить");
        removeHistology.addClickListener(event -> {
            if (histologyGrid.asSingleSelect().isEmpty()) {
                Notification.show("Не выбрана гистология");
                return;
            }
            ConfirmDialog.show(getUI(), "Удалить выбраную запись", "Вы уверены?", "Да", "Нет", dialog -> {
                if (dialog.isConfirmed()) {
                    HistologyBind value = histologyGrid.asSingleSelect().getValue();
                    histologyManager.deleteHistology(value);
                    histology.remove(value);
                    histologyGrid.getDataProvider().refreshAll();
                }
            });
        });

        HorizontalLayout tools = new HorizontalLayout(suggestionCombobox, showSelectedHistology, addHistology, removeHistology);
        tools.setWidth("100%");
        tools.setExpandRatio(suggestionCombobox, 1.0f);
        Label patientsName = new Label();
        suggestionCombobox.addValueChangeListener(event -> patientsName.setValue(event.getValue().toString()));

        DateField dateField = new DateField("Дата гистологии");
        dateField.setTextFieldEnabled(false);
        CheckBox burdenkoVerification = new CheckBox("Верификация Бурденко");
        TextField ki67From = new TextField("Ki-67,% от");
        TextField ki67To = new TextField("Ki-67,% до");

        HorizontalLayout histologyParams = new HorizontalLayout(dateField, ki67From, ki67To, burdenkoVerification);
        histologyParams.setComponentAlignment(burdenkoVerification, Alignment.MIDDLE_CENTER);
        RichTextArea comment = new RichTextArea("Гистологическое заключение");
        comment.setWidth("100%");

        Button saveChanges = new Button("Сохранить изменения");
        saveChanges.addClickListener(event -> {
            if (editiMode) {
                selectedToEdit.setMutationBinds(mutations);
                histologyManager.updateHistology(selectedToEdit, suggestionCombobox.getValue());
            } else {
                HistologyBind histologyBind = binder.getBean();
                histologyBind.setMutationBinds(mutations);
                histologyManager.createNewHistology(histologyBind, suggestionCombobox.getValue());
                histology.add(histologyBind);
                editiMode = true; // Сразу после создания переключаемся в режим редактирования
                selectedToEdit = histologyBind;
            }
            histologyGrid.getDataProvider().refreshAll();
        });

        addHistology.addClickListener(event -> {
            if (suggestionCombobox.isEmpty()) {
                Notification.show("Не выбран пациент");
                return;
            }
            editiMode = false;
            selectedToEdit = null;
            // bind не работает для очистки полей!
            histologyGrid.deselectAll();
            cleanCurrentHistology();
            dateField.clear();
            ki67From.clear();
            ki67To.clear();
            comment.clear();
            burdenkoVerification.clear();
            binder.setBean(HistologyBind.builder().build());
        });

        binder.forField(dateField)
                .bind(HistologyBind::getHistologyDate, HistologyBind::setHistologyDate);
        binder.forField(burdenkoVerification)
                .bind(HistologyBind::getBurdenkoVerification, HistologyBind::setBurdenkoVerification);
        binder.forField(ki67From)
                .withNullRepresentation("")
                .withConverter(new StringToDoubleConverter("Неверно введенное значение"))
                .withValidator(new DoubleRangeValidator("Неверно введенное значение", Double.MIN_VALUE, Double.MAX_VALUE))
                .bind(HistologyBind::getKi67From, HistologyBind::setKi67From);
        binder.forField(ki67To)
                .withNullRepresentation("")
                .withConverter(new StringToDoubleConverter("Неверно введенное значение"))
                .withValidator(new DoubleRangeValidator("Неверно введенное значение", Double.MIN_VALUE, Double.MAX_VALUE))
                .bind(HistologyBind::getKi67To, HistologyBind::setKi67To);
        binder.forField(comment)
                .withNullRepresentation("")
                .bind(HistologyBind::getComment, HistologyBind::setComment);

        saveChanges.setStyleName(ValoTheme.BUTTON_PRIMARY);
        initHistologyGrid();
        AbstractOrderedLayout mutationGrid = initMutationGrid();
        addComponents(tools, patientsName, histologyGrid, histologyParams, comment, mutationGrid, saveChanges);
        setComponentAlignment(saveChanges, Alignment.MIDDLE_CENTER);
    }

    private void edit() {
        if (histologyGrid.asSingleSelect().isEmpty()) {
            Notification.show("Не выбрана гистология");
            return;
        }
        editiMode = true;
        HistologyBind selectedHistology = histologyGrid.asSingleSelect().getValue();
        selectedToEdit = selectedHistology;
        binder.readBean(selectedHistology);
        mutations.clear();
        if (selectedHistology.getMutationBinds() != null)
            mutations.addAll(selectedHistology.getMutationBinds());
        mutationGrid.getDataProvider().refreshAll();
    }

    private void initHistologyGrid() {
        histologyGrid.setWidth("100%");
        histologyGrid.setHeightByRows(3);
        histologyGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        histologyGrid.addColumn(HistologyBind::getHistologyDate)
                .setCaption("Дата");
        histologyGrid.addColumn(HistologyBind::getComment)
                .setCaption("Комментарий");
        histologyGrid.addColumn(histologyBind -> Objects.equals(histologyBind.getBurdenkoVerification(), true) ? "Да" : "Нет")
                .setCaption("Верификация Бурденко");
    }

    private AbstractOrderedLayout initMutationGrid() {
        mutationGrid.addColumn(MutationBind::getGenes)
                .setCaption("Ген")
                .setEditorBinding(getEditorBind(mutationGrid,
                        new NativeSelect<>("Ген", genesDao.getGenes().values()),
                        MutationBind::getGenes,
                        MutationBind::setGenes)
                );
        mutationGrid.addColumn(MutationBind::getMutationTypes)
                .setCaption("Тип мутации")
                .setEditorBinding(getEditorBind(mutationGrid,
                        new NativeSelect<>("Тип мутации", mutationTypesDao.getMutationTypes().values()),
                        MutationBind::getMutationTypes,
                        MutationBind::setMutationTypes)
                );
        mutationGrid.addColumn(MutationBind::getDicYesNo)
                .setCaption("Наличие")
                .setEditorBinding(getEditorBind(mutationGrid,
                        new NativeSelect<>("Наличие", dicYesNoDao.getDicYesNo().values()),
                        MutationBind::getDicYesNo,
                        MutationBind::setDicYesNo)
                );
        mutationGrid.setHeightByRows(3);
        mutationGrid.getEditor().setEnabled(true);
        mutationGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        mutationGrid.setWidth("700px");

        Button addMutation = new Button();
        addMutation.setStyleName(ValoTheme.BUTTON_PRIMARY);
        addMutation.setIcon(VaadinIcons.PLUS);
        addMutation.addClickListener(clickEvent -> {
            mutations.add(0, new MutationBind());
            mutationGrid.getDataProvider().refreshAll();
        });

        Button removeMutation = new Button();
        removeMutation.setStyleName(ValoTheme.BUTTON_PRIMARY);
        removeMutation.setIcon(VaadinIcons.MINUS);
        removeMutation.addClickListener(clickEvent -> {
            Set<MutationBind> selected = mutationGrid.getSelectedItems();
            mutations.removeAll(selected);
            mutationGrid.getDataProvider().refreshAll();
        });

        VerticalLayout mutationButtons = new VerticalLayout();
        mutationButtons.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        mutationButtons.addComponents(addMutation, removeMutation);
        //mutationButtons.setSizeFull();

        HorizontalLayout mutation = new HorizontalLayout(mutationButtons, mutationGrid);
        mutation.setExpandRatio(mutationGrid, 1.0f);
        return mutation;
    }

    private <T> Binder.Binding<MutationBind, T> getEditorBind(Grid<MutationBind> mutationGrid, NativeSelect<T> editSelect, ValueProvider<MutationBind, T> getter, Setter<MutationBind, T> setter) {
        editSelect.setEmptySelectionAllowed(true);
        return mutationGrid.getEditor()
                .getBinder()
                .forField(editSelect)
                .asRequired("Значение должно быть выбрано")
                .withValidator(Objects::nonNull, "Значение должно быть выбрано")
                .bind(getter, setter);
    }

    private void clearAll() {
        histology.clear();
        histologyGrid.getDataProvider().refreshAll();
        cleanCurrentHistology();
    }

    private void cleanCurrentHistology() {
        mutations.clear();
        mutationGrid.getDataProvider().refreshAll();
        binder.removeBean();
        binder.setBean(null);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
