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
import lgk.nsbc.model.dictionary.ProcType;
import lgk.nsbc.model.histology.Mutation;
import lgk.nsbc.spect.util.DateUtils;
import lgk.nsbc.spect.util.components.NavigationBar;
import lgk.nsbc.spect.util.components.SuggestionCombobox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.dialogs.ConfirmDialog;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;

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

    private NativeSelect<Proc> connectedProcs = new NativeSelect<>("Привязать процедуру", procList);

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
            editMode = false;
            selectedToEdit = null;
        });

        Button showSelectedHistology = new Button("Просмотр");
        showSelectedHistology.addClickListener(event -> edit());

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

        DateField dateField = new DateField("Дата гистологии");
        dateField.setTextFieldEnabled(false);
        CheckBox burdenkoVerification = new CheckBox("Верификация Бурденко");
        TextField ki67From = new TextField("Ki-67,% от");
        TextField ki67To = new TextField("Ki-67,% до");

        HorizontalLayout histologyParams = new HorizontalLayout(connectedProcs, dateField, ki67From, ki67To, burdenkoVerification);
        histologyParams.setComponentAlignment(burdenkoVerification, Alignment.MIDDLE_CENTER);
        histologyParams.setExpandRatio(connectedProcs, 1.0f);
        histologyParams.setWidth("100%");

        connectedProcs.setWidth("100%");
        connectedProcs.setEmptySelectionAllowed(true);
        connectedProcs.addValueChangeListener(event -> {
            Proc selectedProc = event.getValue();
            if (selectedProc != null)
                dateField.setValue(DateUtils.asLocalDate(selectedProc.getProcBeginTime()));
        });
        connectedProcs.setItemCaptionGenerator(proc -> {
            if (proc == null) return null;
            ProcType procType = proc.getProcType();
            LocalDate date = DateUtils.asLocalDate(proc.getProcBeginTime());
            return (procType == null ? "" : procType.toString()) + " " + (date == null ? "" : date.toString());
        });

        TextArea comment = new TextArea("Гистологическое заключение");
        comment.setWidth("100%");
        comment.setRows(4);

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
                bean.setConnectedProc(connectedProcs.getValue());
                bean.setStud(selectedToEdit.getStud());
                histology.remove(selectedToEdit);
                histology.add(bean);
                histologyManager.updateHistology(bean, suggestionCombobox.getValue());
                selectedToEdit = bean; // Все еще в режиме редактирования
                connectedProcs.setSelectedItem(bean.getConnectedProc());
            } else {
                HistologyBind histologyBind = binder.getBean();
                histologyBind.setMutations(new TreeSet<>(mutations));
                histologyBind.setConnectedProc(connectedProcs.getValue());
                histologyManager.createNewHistology(histologyBind, suggestionCombobox.getValue());
                histology.add(histologyBind);
                editMode = true; // Сразу после создания переключаемся в режим редактирования
                selectedToEdit = histologyBind;
                connectedProcs.getDataProvider().refreshAll();
            }
            refreshConnnectedProc(selectedToEdit);
            refreshStudy(suggestionCombobox.getValue());
            histologyGrid.getDataProvider().refreshAll();
        });

        Button addHistology = new Button("Добавить");
        addHistology.addClickListener(event -> {
            if (suggestionCombobox.isEmpty()) {
                Notification.show("Не выбран пациент");
                return;
            }
            editMode = false;
            selectedToEdit = null;
            // bind не работает для очистки полей!
            connectedProcs.setSelectedItem(null);
            histologyGrid.deselectAll();
            cleanCurrentHistology();
            dateField.clear();
            ki67From.clear();
            ki67To.clear();
            comment.clear();
            burdenkoVerification.clear();
            binder.setBean(new HistologyBind());
        });

        HorizontalLayout tools = new HorizontalLayout(suggestionCombobox, showSelectedHistology, addHistology, removeHistology);
        tools.setComponentAlignment(showSelectedHistology, Alignment.BOTTOM_CENTER);
        tools.setComponentAlignment(addHistology, Alignment.BOTTOM_CENTER);
        tools.setComponentAlignment(removeHistology, Alignment.BOTTOM_CENTER);
        tools.setWidth("100%");
        tools.setExpandRatio(suggestionCombobox, 1.0f);

        dateField.setRequiredIndicatorVisible(true);
        binder.forField(dateField)
                .asRequired("Укажите дату гистологии")
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
        binder.forField(connectedProcs)
                .bind(HistologyBind::getConnectedProc, HistologyBind::setConnectedProc);

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
        connectedProcs.getDataProvider().refreshAll();
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
        refreshConnnectedProc(selectedHistology);
        refreshMutations(selectedToEdit);
    }

    private void refreshConnnectedProc(HistologyBind selectedHistology) {
        Optional<Proc> first = connectedProcs.getDataProvider()
                .fetch(new Query<>())
                .filter(proc -> Objects.equals(proc, selectedHistology.getConnectedProc()))
                .findFirst();
        first.ifPresent(proc -> connectedProcs.setSelectedItem(proc));
    }

    private void initProcGrid() {
        procGrid.addColumn(Proc::getN)
                .setCaption("Внутренний id")
                .setHidable(true)
                .setHidden(true);
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
        studGrid.addColumn(Stud::getN)
                .setCaption("Внутренний id")
                .setHidable(true)
                .setHidden(true);
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
        connectedProcs.clear();
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
