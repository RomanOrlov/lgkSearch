package lgk.nsbc.spect.view.histology;

import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.data.provider.Query;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Setter;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.themes.ValoTheme;
import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Proc;
import lgk.nsbc.model.Stud;
import lgk.nsbc.model.dao.dictionary.DicYesNoDao;
import lgk.nsbc.model.dictionary.Gene;
import lgk.nsbc.model.histology.Mutation;
import lgk.nsbc.spect.util.DateUtils;
import lgk.nsbc.spect.util.components.NavigationBar;
import lgk.nsbc.spect.util.components.SuggestionCombobox;
import lgk.nsbc.spect.view.spectcrud.SpectGridData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.dialogs.ConfirmDialog;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@VaadinSessionScope
public class HistologyView extends VerticalLayout implements View {
    @Autowired
    private HistologyManager histologyManager;
    @Autowired
    private SuggestionCombobox suggestionCombobox;

    private Set<Mutation> mutations = new TreeSet<>();
    private Grid<Mutation> mutationGrid = new Grid<>("Мутации", mutations);

    private List<HistologyBind> histology = new ArrayList<>();
    private Grid<HistologyBind> histologyGrid = new Grid<>("Список гистологий", histology);

    private List<Stud> studList = new ArrayList<>();
    private Grid<Stud> studGrid = new Grid<>("Исследования пациента", studList);

    private List<Proc> procList = new ArrayList<>();
    private Grid<Proc> procGrid = new Grid<>("Процедуры", procList);

    private Binder<HistologyBind> binder = new Binder<>();
    private Boolean editMode = false;
    private HistologyBind selectedToEdit;

    private NativeSelect<Stud> surgerySelect = new NativeSelect<>();

    private NavigationBar navigationBar = new NavigationBar();

    private void refreshHistology(Patients patients) {
        List<HistologyBind> histologyBindList = histologyManager.getHistology(patients);
        histology.addAll(histologyBindList);
        histologyGrid.getDataProvider().refreshAll();
    }

    @PostConstruct
    public void init() {
        suggestionCombobox.focus();
        suggestionCombobox.addValueChangeListener(event -> {
            clearAll();
            Patients patients = event.getValue();
            refreshHistology(patients);
            refreshStudy(patients);
            binder.setBean(HistologyBind.builder().build()); // Пустая
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

        DateField dateField = new DateField("Дата гистологии");
        dateField.setTextFieldEnabled(false);
        CheckBox burdenkoVerification = new CheckBox("Верификация Бурденко");
        TextField ki67From = new TextField("Ki-67,% от");
        TextField ki67To = new TextField("Ki-67,% до");

        HorizontalLayout histologyParams = new HorizontalLayout(dateField, ki67From, ki67To, burdenkoVerification);
        histologyParams.setComponentAlignment(burdenkoVerification, Alignment.MIDDLE_CENTER);
        TextArea comment = new TextArea("Гистологическое заключение");
        comment.setWidth("100%");

        Button saveChanges = new Button("Сохранить изменения");
        saveChanges.addClickListener(event -> {
            if (mutationGrid.getEditor().isOpen()) {
                Notification.show("Закончите радактирование мутаций");
                return;
            }
            if (!binder.writeBeanIfValid(binder.getBean())) {
                binder.validate();
                return;
            }
            // Доп. проверяем, а не удалили ли мы запись, которую редактируем
            if (editMode && histologyManager.checkHistologyExist(selectedToEdit.getHistology().getN())) {
                HistologyBind bean = binder.getBean();
                bean.setHistology(selectedToEdit.getHistology());
                bean.setMutations(new TreeSet<>(mutations));
                histology.remove(selectedToEdit);
                histology.add(bean);
                histologyManager.updateHistology(bean, suggestionCombobox.getValue());
                selectedToEdit = bean; // Все еще в режиме редактирования
            } else {
                HistologyBind histologyBind = binder.getBean();
                histologyBind.setMutations(new TreeSet<>(mutations));
                histologyManager.createNewHistology(histologyBind, suggestionCombobox.getValue());
                histology.add(histologyBind);
                editMode = true; // Сразу после создания переключаемся в режим редактирования
                selectedToEdit = histologyBind;
            }
            refreshStudy(suggestionCombobox.getValue());
            histologyGrid.getDataProvider().refreshAll();
        });

        addHistology.addClickListener(event -> {
            if (suggestionCombobox.isEmpty()) {
                Notification.show("Не выбран пациент");
                return;
            }
            editMode = false;
            selectedToEdit = null;
            // bind не работает для очистки полей!
            histologyGrid.deselectAll();
            cleanCurrentHistology();
            dateField.clear();
            ki67From.clear();
            ki67To.clear();
            comment.clear();
            burdenkoVerification.clear();
            binder.setBean(new HistologyBind());
        });

        dateField.setRequiredIndicatorVisible(true);
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
        initStudGrid();
        initProcGrid();
        HorizontalLayout grids = new HorizontalLayout(histologyGrid, studGrid, procGrid);
        grids.setWidth("100%");
        grids.setExpandRatio(histologyGrid, 1.0f);
        initMutationGrid();
        addComponents(navigationBar, tools, grids, histologyParams, comment, mutationGrid, saveChanges);
        setComponentAlignment(saveChanges, Alignment.MIDDLE_CENTER);
        mutations.addAll(HistologyBind.getMutationsTemplate());
        mutationGrid.getDataProvider().refreshAll();
    }

    private void refreshStudy(Patients patients) {
        studList.clear();
        studList.addAll(histologyManager.getPatientStudy(patients));
        studGrid.getDataProvider().refreshAll();
        procList.clear();
        procList.addAll(histologyManager.getPatientsProc(patients));
        procGrid.getDataProvider().refreshAll();
    }

    private void refreshMutations(HistologyBind histologyBind) {
        mutations.clear();
        if (histologyBind.getMutations() != null)
            mutations.addAll(histologyBind.getMutations());
        mutationGrid.getDataProvider().refreshAll();
    }

    private void edit() {
        if (histologyGrid.asSingleSelect().isEmpty()) {
            Notification.show("Не выбрана гистология");
            return;
        }
        if (mutationGrid.getEditor().isOpen()) {
            mutationGrid.getEditor().cancel();
        }
        editMode = true;
        HistologyBind selectedHistology = histologyGrid.asSingleSelect().getValue();
        selectedToEdit = selectedHistology;
        binder.readBean(selectedHistology);
        refreshMutations(selectedToEdit);
    }

    private void initProcGrid() {
        Grid.Column<Proc, LocalDate> dateColumn = procGrid.addColumn(proc -> DateUtils.asLocalDate(proc.getProcBeginTime()))
                .setCaption("Дата");
        procGrid.addColumn(Proc::getProcType)
                .setCaption("Тип");
        procGrid.setHeightByRows(3);
        procGrid.setWidth("300px");
        FooterRow footerRow = procGrid.appendFooterRow();
        procGrid.getDataProvider().addDataProviderListener(event -> {
            long count = event.getSource()
                    .fetch(new Query<>())
                    .count();
            footerRow.getCell(dateColumn).setText("Всего " + count);
        });
    }

    private void initHistologyGrid() {
        histologyGrid.setWidth("100%");
        histologyGrid.setHeightByRows(3);
        histologyGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        histologyGrid.addColumn(histologyBind -> histologyBind.getHistology().getN())
                .setCaption("Внутренний Id")
                .setHidable(true)
                .setHidden(true);
        Grid.Column<HistologyBind, LocalDate> dateColumn = histologyGrid.addColumn(HistologyBind::getHistologyDate)
                .setCaption("Дата");
        histologyGrid.addColumn(HistologyBind::getComment)
                .setCaption("Комментарий");
        histologyGrid.addColumn(histologyBind -> Objects.equals(histologyBind.getBurdenkoVerification(), true) ? "Да" : "Нет")
                .setCaption("Верификация Бурденко");
        FooterRow footerRow = histologyGrid.appendFooterRow();
        histologyGrid.getDataProvider().addDataProviderListener(event -> {
            long count = event.getSource()
                    .fetch(new Query<>())
                    .count();
            footerRow.getCell(dateColumn).setText("Всего " + count);
        });
    }

    private void initMutationGrid() {
        Grid.Column<Mutation, Gene> geneColumn = mutationGrid.addColumn(Mutation::getGene)
                .setCaption("Ген");
        mutationGrid.addColumn(mutation -> mutation.getGene() == null ? null : mutation.getGene().getMutationType())
                .setCaption("Тип мутации");
        mutationGrid.addColumn(Mutation::getDicYesNo)
                .setCaption("Наличие")
                .setEditorBinding(getEditorBind(mutationGrid,
                        new NativeSelect<>("Наличие", DicYesNoDao.getDicYesNo().values()),
                        Mutation::getDicYesNo,
                        Mutation::setDicYesNo)
                );
        mutationGrid.setSortOrder(GridSortOrder.asc(geneColumn));
        mutationGrid.setHeightByRows(6);
        mutationGrid.getEditor().setEnabled(true);
        mutationGrid.getEditor().addSaveListener(event -> {
            mutations.remove(event.getBean());
            mutations.add(event.getBean());
            mutationGrid.getDataProvider().refreshItem(event.getBean());
            mutationGrid.getDataProvider().refreshAll();

        });
        mutationGrid.setSelectionMode(Grid.SelectionMode.NONE);
        mutationGrid.setWidth("100%");
    }

    private void initStudGrid() {
        studGrid.setHeightByRows(3);
        Grid.Column<Stud, LocalDate> dateColumn = studGrid.addColumn(stud -> DateUtils.asLocalDate(stud.getStudyDateTime()))
                .setCaption("Дата");
        studGrid.addColumn(Stud::getStudType)
                .setCaption("Тип");
        studGrid.setWidth("300px");
        FooterRow footerRow = studGrid.appendFooterRow();
        studGrid.getDataProvider().addDataProviderListener(event -> {
            long count = event.getSource()
                    .fetch(new Query<>())
                    .count();
            footerRow.getCell(dateColumn).setText("Всего " + count);
        });
    }

    private <T> Binder.Binding<Mutation, T> getEditorBind(Grid<Mutation> mutationGrid, NativeSelect<T> editSelect, ValueProvider<Mutation, T> getter, Setter<Mutation, T> setter) {
        editSelect.setEmptySelectionAllowed(true);
        return mutationGrid.getEditor()
                .getBinder()
                .forField(editSelect)
                .asRequired("Значение должно быть выбрано")
                .withValidator(Objects::nonNull, "Значение должно быть выбрано")
                .bind(getter, setter);
    }

    private void clearAll() {
        studList.clear();
        studGrid.getDataProvider().refreshAll();
        procList.clear();
        procGrid.getDataProvider().refreshAll();
        histology.clear();
        histologyGrid.getDataProvider().refreshAll();
        cleanCurrentHistology();
    }

    private void cleanCurrentHistology() {
        mutations.clear();
        Set<Mutation> mutationsTemplate = HistologyBind.getMutationsTemplate();
        mutations.addAll(mutationsTemplate);
        mutationGrid.getDataProvider().refreshAll();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
