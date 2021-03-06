package lgk.nsbc.spect.view.statistic;

import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.FooterCell;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.NumberRenderer;
import lgk.nsbc.model.Patients;
import lgk.nsbc.model.SamplePatients;
import lgk.nsbc.spect.util.DateUtils;
import lgk.nsbc.spect.util.components.GlobalGridFilter;
import lgk.nsbc.spect.util.components.GridHeaderFilter;
import lgk.nsbc.spect.util.components.NavigationBar;
import lgk.nsbc.spect.util.components.SuggestionCombobox;
import lgk.nsbc.spect.util.excel.StatisticExcelExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.dialogs.ConfirmDialog;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static lgk.nsbc.spect.util.components.GridHeaderFilter.addDoubleFilter;
import static lgk.nsbc.spect.util.components.GridHeaderFilter.addRadioButtonFilter;
import static lgk.nsbc.spect.view.statistic.Dynamic.*;

@Service
@VaadinSessionScope
public class SampleStatistic extends VerticalLayout implements View, Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired
    private SampleManager sampleManager;
    @Autowired
    private SuggestionCombobox combobox;

    private List<SampleBind> sampleBinds = new ArrayList<>();
    private ListDataProvider<SampleBind> dataProvider = new ListDataProvider<>(sampleBinds);
    private Grid<SampleBind> sampleGrid = new Grid<>(dataProvider);

    private Button refresh = new Button("Обновить");
    private HorizontalLayout statisticToExcel = new StatisticExcelExporter(sampleGrid, "Данные в Excel");
    private Button addToSample = new Button("Добавить");
    private Button removeFromSample = new Button("Удалить");
    private Button clearAllFilters = new Button("Сброс фильтров");
    private static final DecimalFormat ageFormat = new DecimalFormat("###0");
    private static final DecimalFormat inFormat = new DecimalFormat("##0.00");
    private final GlobalGridFilter<SampleBind> globalGridFilter = new GlobalGridFilter<>();

    private NavigationBar navigationBar = new NavigationBar();

    @PostConstruct
    public void init() {
        setSizeFull();
        initSampleGrid();
        initButtons();
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        buttons.addComponents(combobox, refresh, addToSample, removeFromSample, clearAllFilters, statisticToExcel);
        buttons.setComponentAlignment(combobox, Alignment.TOP_CENTER);
        buttons.setWidth("100%");
        buttons.setExpandRatio(combobox, 1.0f);
        addComponents(navigationBar, buttons, sampleGrid);
        setExpandRatio(sampleGrid, 1.0f);
    }

    private void initButtons() {
        clearAllFilters.addClickListener(event -> {
            globalGridFilter.clearAllFields();
            dataProvider.setFilter(sampleBind -> true);
        });
        refresh.addClickListener(event -> {
            List<SampleBind> spectSample = sampleManager.getSpectSample();
            spectSample.sort(SampleBind::compareTo);
            sampleBinds.clear();
            sampleBinds.addAll(spectSample);
            sampleGrid.getDataProvider().refreshAll();
        });
        addToSample.addClickListener(clickEvent -> handleAddToSample());
        removeFromSample.addClickListener(clickEvent -> {
            if (sampleGrid.asSingleSelect().isEmpty()) {
                Notification.show("Не выбран пациент из выборки");
                return;
            }
            ConfirmDialog.show(getUI(), "Удалить выбраную запись", "Вы уверены?", "Да", "Нет", dialog -> {
                if (dialog.isConfirmed()) {
                    SampleBind value = sampleGrid.asSingleSelect().getValue();
                    sampleManager.removeSamplePatient(value);
                    sampleGrid.deselectAll();
                    sampleBinds.remove(value);
                    dataProvider.refreshAll();
                }
            });
        });
    }

    private void handleAddToSample() {
        if (!combobox.getSelectedItem().isPresent()) {
            Notification.show("Не выбран пациент");
            return;
        }
        Patients selectedPatient = combobox.getValue();
        Set<Long> uniquePatientsId = sampleBinds.stream()
                .map(SampleBind::getSamplePatients)
                .map(SamplePatients::getPatientId)
                .collect(toSet());
        if (uniquePatientsId.contains(selectedPatient.getN())) {
            Notification.show("Данный пациент уже находится в выборке");
            return;
        }
        Set<String> uniqueNames = sampleBinds.stream()
                .map(SampleBind::getPatients)
                .map(Patients::getFullName)
                .collect(toSet());
        if (uniqueNames.contains(selectedPatient.getFullName())) {
            Notification.show("Пациент с таким же именем уже находится в выборке");
            return;
        }
        SampleBind simpleBind = sampleManager.getTemplateSimpleBind(selectedPatient);
        sampleBinds.add(simpleBind);
        sampleBinds.sort(SampleBind::compareTo);
        dataProvider.refreshAll();
        sampleGrid.select(simpleBind);
        sampleGrid.scrollTo(sampleBinds.indexOf(simpleBind));
    }

    private void initSampleGrid() {
        sampleGrid.setColumnReorderingAllowed(true);
        sampleGrid.getEditor().setEnabled(true);
        sampleGrid.getEditor().addSaveListener(editorSaveEvent -> {
            SampleBind bean = editorSaveEvent.getBean();
            dataProvider.refreshItem(bean);
            sampleManager.updateSamplePatient(bean);
        });

        Grid.Column<SampleBind, String> name = sampleGrid.addColumn(sampleBind -> sampleBind.getPatients().getFullName())
                .setCaption("ФИО")
                .setHidable(true);

        sampleGrid.addColumn(sampleBind -> sampleBind.getPatients().getN())
                .setCaption("id Пациента")
                .setHidden(true);

        Grid.Column<SampleBind, String> gender = sampleGrid.addColumn(sampleBind -> sampleBind.getPatients().getPeople().getSex())
                .setCaption("Пол")
                .setHidden(true);

        sampleGrid.addColumn(sampleBind -> sampleBind.getPatients().getCaseHistoryNumber())
                .setCaption("Номер истории болезни")
                .setHidden(true);

        DateField obitDate = new DateField();
        sampleGrid.addColumn(sampleBind -> sampleBind.getPatients().getPeople().getObit())
                .setCaption("Смерть")
                .setEditorComponent(obitDate, (sampleBind, date) -> sampleBind.getPatients().getPeople().setObit(date))
                .setEditable(true)
                .setHidden(true);

        NativeSelect<String> inclusionEdit = new NativeSelect<>();
        inclusionEdit.setItems(asList("Да", "Нет"));
        inclusionEdit.setEmptySelectionAllowed(true);

        Grid.Column<SampleBind, String> includedColumn = sampleGrid.addColumn(SampleBind::getInclusionRepresentation)
                .setCaption("Включен")
                .setHidden(false)
                .setEditorComponent(inclusionEdit, SampleBind::setInclusionRepresentation)
                .setEditable(true);

        /*sampleGrid.addColumn(sampleBind -> sampleBind.getSamplePatients().getComment())
                .setCaption("Комментарий")
                .setHidable(true)
                .setHidden(true);*/

        sampleGrid.addColumn(sampleBind -> DateUtils.asLocalDate(sampleBind.getPatients().getPeople().getBirthday()))
                .setCaption("Дата рождения")
                .setHidden(true);

        DateField surgeryDateField = new DateField();
        surgeryDateField.setTextFieldEnabled(false);

        sampleGrid.addColumn(SampleBind::getSurgeryDate)
                .setCaption("Дата хирургии")
                .setEditorComponent(surgeryDateField, SampleBind::setSurgeryProcDate);

        Grid.Column<SampleBind, Long> surgeryRtPeriodColumn = sampleGrid.addColumn(SampleBind::getSurgeryRTPeriod)
                .setCaption("Время между хирургией и ЛТ")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getRtBeginDate)
                .setCaption("Начало ЛТ")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getRtEndDate)
                .setCaption("Конец ЛТ")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getMgmt)
                .setCaption("MGMT")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getIdh1)
                .setCaption("IDH1")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getIdh2)
                .setCaption("IDH2")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect1Date)
                .setCaption("№1 ОФЕКТ");

        Grid.Column<SampleBind, Double> spect1InEarly = sampleGrid.addColumn(SampleBind::getSpect1InEarly, new NumberRenderer(inFormat))
                .setCaption("№1 ИН 30");

        Grid.Column<SampleBind, Double> spect1InLate = sampleGrid.addColumn(SampleBind::getSpect1InLate, new NumberRenderer(inFormat))
                .setCaption("№1 ИН 60");

        Grid.Column<SampleBind, Double> spect1InOut = sampleGrid.addColumn(SampleBind::getSpect1InOut, new NumberRenderer(inFormat))
                .setCaption("№1 Вымывание")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect2Date)
                .setCaption("№2 ОФЕКТ")
                .setHidden(true);

        Grid.Column<SampleBind, Double> spect2InEarly = sampleGrid.addColumn(SampleBind::getSpect2InEarly, new NumberRenderer(inFormat))
                .setCaption("№2 ИН 30")
                .setHidden(true);

        Grid.Column<SampleBind, Double> spect2InLate = sampleGrid.addColumn(SampleBind::getSpect2InLate, new NumberRenderer(inFormat))
                .setCaption("№2 ИН 60")
                .setHidden(true);

        Grid.Column<SampleBind, Double> spect2InOut = sampleGrid.addColumn(SampleBind::getSpect2InOut, new NumberRenderer(inFormat))
                .setCaption("№2 Вымывание")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect3Date)
                .setCaption("№3 ОФЕКТ")
                .setHidden(true);

        Grid.Column<SampleBind, Double> spect3InEarly = sampleGrid.addColumn(SampleBind::getSpect3InEarly, new NumberRenderer(inFormat))
                .setCaption("№3 ИН 30")
                .setHidden(true);

        Grid.Column<SampleBind, Double> spect3InLate = sampleGrid.addColumn(SampleBind::getSpect3InLate, new NumberRenderer(inFormat))
                .setCaption("№3 ИН 60")
                .setHidden(true);

        Grid.Column<SampleBind, Double> spect3InOut = sampleGrid.addColumn(SampleBind::getSpect3InOut, new NumberRenderer(inFormat))
                .setCaption("№3 Вымывание")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect1In30Isoline10, new NumberRenderer(inFormat))
                .setCaption("№1 ИН 30 Изолиния 10")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect1In60Isoline10, new NumberRenderer(inFormat))
                .setCaption("№1 ИН 60 Изолиния 10")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect1In30Isoline25, new NumberRenderer(inFormat))
                .setCaption("№1 ИН 30 Изолиния 25")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect1In60Isoline25, new NumberRenderer(inFormat))
                .setCaption("№1 ИН 60 Изолиния 25")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect1In30Isoline50, new NumberRenderer(inFormat))
                .setCaption("№1 ИН 30 Изолиния 50")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect1In60Isoline50, new NumberRenderer(inFormat))
                .setCaption("№1 ИН 60 Изолиния 50")
                .setHidden(true);


        sampleGrid.addColumn(SampleBind::getSpect2In30Isoline10, new NumberRenderer(inFormat))
                .setCaption("№2 ИН 30 Изолиния 10")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect2In60Isoline10, new NumberRenderer(inFormat))
                .setCaption("№2 ИН 60 Изолиния 10")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect2In30Isoline25, new NumberRenderer(inFormat))
                .setCaption("№2 ИН 30 Изолиния 25")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect2In60Isoline25, new NumberRenderer(inFormat))
                .setCaption("№2 ИН 60 Изолиния 25")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect2In30Isoline50, new NumberRenderer(inFormat))
                .setCaption("№2 ИН 30 Изолиния 50")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSpect2In60Isoline50, new NumberRenderer(inFormat))
                .setCaption("№2 ИН 60 Изолиния 50")
                .setHidden(true);

        Grid.Column<SampleBind, Long> fullYearsAtSurgery = sampleGrid.addColumn(SampleBind::getAgeAtSurgery, new NumberRenderer(ageFormat))
                .setCaption("Возраст")
                .setDescriptionGenerator(sampleBind -> "Если есть дата хирургии, считается от даты хирургии. Если нет, считается на текущий момент")
                .setHidden(true);

        Grid.Column<SampleBind, Long> recurrencePeriodColumn = sampleGrid.addColumn(SampleBind::getRecurrence, new NumberRenderer(ageFormat))
                .setCaption("Безрецидивидный период")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getRecurrenceCause)
                .setCaption("Последний след (рецидивив)")
                .setHidable(true)
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getRecurrenceCensor)
                .setCaption("Цензурирован ли рецидив")
                .setHidden(true);

        Grid.Column<SampleBind, Long> survivalPeriodColumn = sampleGrid.addColumn(SampleBind::getSurvival)
                .setCaption("Выживаемость")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSurvivalCause)
                .setCaption("Последний след (выживаемость)")
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getSurvivalCensor)
                .setCaption("Цензурирована ли выживаемость")
                .setHidden(true);

        Grid.Column<SampleBind, Dynamic> dynamic1_2_30 = sampleGrid.addColumn(SampleBind::getSpect1_2Dynamic30)
                .setCaption("ОФЕКТ 1 vs 2 30 мин")
                .setHidden(true);

        Grid.Column<SampleBind, Dynamic> dynamic1_2_60 = sampleGrid.addColumn(SampleBind::getSpect1_2Dynamic60)
                .setCaption("ОФЕКТ 1 vs 2 60 мин")
                .setHidden(true);

        Grid.Column<SampleBind, Dynamic> dynamic2_3_30 = sampleGrid.addColumn(SampleBind::getSpect2_3Dynamic30)
                .setCaption("ОФЕКТ 2 vs 3 30 мин")
                .setHidden(true);

        Grid.Column<SampleBind, Dynamic> dynamic2_3_60 = sampleGrid.addColumn(SampleBind::getSpect2_3Dynamic60)
                .setCaption("ОФЕКТ 2 vs 3 60 мин")
                .setHidden(true);

        sampleGrid.getColumns().forEach(column -> column.setHidable(true));

        sampleGrid.setSizeFull();
        sampleGrid.setFrozenColumnCount(1);
        sampleGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        // Заполняем маленькую статистику
        FooterRow footerRow = sampleGrid.appendFooterRow();
        sampleGrid.getDataProvider().addDataProviderListener(event -> {
            List<SampleBind> allData = event.getSource()
                    .fetch(new Query<>())
                    .collect(toList());
            // Нас интересует статистика только включенных в выборку
            List<SampleBind> includedData = allData.stream()
                    .filter(sampleBind -> Objects.equals("Y", sampleBind.getSamplePatients().getInclusion()))
                    .collect(toList());
            footerRow.getCell(name).setText("Всего: " + allData.size());
            footerRow.getCell(gender).setText(getGenderStats(includedData));
            footerRow.getCell(includedColumn).setText(getIncludedStats(includedData));

            meanLongColumn(footerRow.getCell(fullYearsAtSurgery), includedData, SampleBind::getAgeAtSurgery);
            meanDoubleColumn(footerRow.getCell(spect1InEarly), includedData, SampleBind::getSpect1InEarly);
            meanDoubleColumn(footerRow.getCell(spect1InLate), includedData, SampleBind::getSpect1InLate);
            meanDoubleColumn(footerRow.getCell(spect1InOut), includedData, SampleBind::getSpect1InOut);

            meanDoubleColumn(footerRow.getCell(spect2InEarly), includedData, SampleBind::getSpect2InEarly);
            meanDoubleColumn(footerRow.getCell(spect2InLate), includedData, SampleBind::getSpect2InLate);
            meanDoubleColumn(footerRow.getCell(spect2InOut), includedData, SampleBind::getSpect2InOut);

            meanDoubleColumn(footerRow.getCell(spect3InEarly), includedData, SampleBind::getSpect3InEarly);
            meanDoubleColumn(footerRow.getCell(spect3InLate), includedData, SampleBind::getSpect3InLate);
            meanDoubleColumn(footerRow.getCell(spect3InOut), includedData, SampleBind::getSpect3InOut);

            meanLongColumn(footerRow.getCell(recurrencePeriodColumn), includedData, SampleBind::getRecurrence);
            meanLongColumn(footerRow.getCell(survivalPeriodColumn), includedData, SampleBind::getSurvival);
            meanLongColumn(footerRow.getCell(survivalPeriodColumn), includedData, SampleBind::getSurgeryRTPeriod);
        });
        // Настраиваем фильттрацию
        HeaderRow filterHeader = sampleGrid.prependHeaderRow();
        filterHeader.setStyleName("align-center"); // ONG стало лучше выглядеть
        GridHeaderFilter.addTextFilter(filterHeader.getCell(name),
                dataProvider,
                sampleBind -> sampleBind.getPatients().toString(),
                globalGridFilter);

        addRadioButtonFilter(filterHeader.getCell(gender),
                dataProvider,
                sampleBind -> sampleBind.getPatients().getPeople().getSex(),
                asList("М", "Ж"),
                globalGridFilter);

        GridHeaderFilter.addLongFilter(filterHeader.getCell(fullYearsAtSurgery),
                dataProvider,
                SampleBind::getAgeAtSurgery,
                globalGridFilter);

        GridHeaderFilter.addCheckBoxFilter(filterHeader.getCell(includedColumn),
                dataProvider,
                SampleBind::getInclusionRepresentation,
                "Да",
                globalGridFilter);

        addDoubleFilter(filterHeader.getCell(spect1InEarly),
                dataProvider,
                SampleBind::getSpect1InEarly,
                globalGridFilter);

        addDoubleFilter(filterHeader.getCell(spect1InLate),
                dataProvider,
                SampleBind::getSpect1InLate,
                globalGridFilter);

        addDoubleFilter(filterHeader.getCell(spect1InOut),
                dataProvider,
                SampleBind::getSpect1InOut,
                globalGridFilter);

        addDoubleFilter(filterHeader.getCell(spect2InEarly),
                dataProvider,
                SampleBind::getSpect2InEarly,
                globalGridFilter);

        addDoubleFilter(filterHeader.getCell(spect2InLate),
                dataProvider,
                SampleBind::getSpect2InLate,
                globalGridFilter);

        addDoubleFilter(filterHeader.getCell(spect2InOut),
                dataProvider,
                SampleBind::getSpect2InOut,
                globalGridFilter);

        addDoubleFilter(filterHeader.getCell(spect3InEarly),
                dataProvider,
                SampleBind::getSpect3InEarly,
                globalGridFilter);

        addDoubleFilter(filterHeader.getCell(spect3InLate),
                dataProvider,
                SampleBind::getSpect3InLate,
                globalGridFilter);

        addDoubleFilter(filterHeader.getCell(spect3InOut),
                dataProvider,
                SampleBind::getSpect3InOut,
                globalGridFilter);

        List<Dynamic> options = asList(POSITIVE, NEGATIVE, STABLE);
        addRadioButtonFilter(filterHeader.getCell(dynamic1_2_30),
                dataProvider,
                SampleBind::getSpect1_2Dynamic30,
                options,
                globalGridFilter);

        addRadioButtonFilter(filterHeader.getCell(dynamic1_2_60),
                dataProvider,
                SampleBind::getSpect1_2Dynamic60,
                options,
                globalGridFilter);

        addRadioButtonFilter(filterHeader.getCell(dynamic2_3_30),
                dataProvider,
                SampleBind::getSpect2_3Dynamic30,
                options,
                globalGridFilter);

        addRadioButtonFilter(filterHeader.getCell(dynamic2_3_60),
                dataProvider,
                SampleBind::getSpect2_3Dynamic60,
                options,
                globalGridFilter);
        sampleGrid.setSortOrder(GridSortOrder.asc(name));
    }

    private String getIncludedStats(List<SampleBind> data) {
        return "Включено " + data.stream()
                .filter(sampleBind -> "Y".equals(sampleBind.getSamplePatients().getInclusion()))
                .count();
    }

    private String getGenderStats(List<SampleBind> data) {
        String format = "М - %d(%.1f%%)  Ж - %d(%.1f%%)";
        if (data.isEmpty()) return String.format(format, 0, 0d, 0, 0d);
        long malesCount = data.stream()
                .map(sampleBind -> sampleBind.getPatients().getPeople().getSex())
                .filter(s -> s.equals("М"))
                .count();
        long femalesCount = data.stream()
                .map(sampleBind -> sampleBind.getPatients().getPeople().getSex())
                .filter(s -> s.equals("Ж"))
                .count();
        return String.format(format, malesCount, (100.0d * malesCount) / data.size(), femalesCount, (100.0d * femalesCount) / data.size());
    }

    private void meanDoubleColumn(FooterCell footerCell,
                                  List<SampleBind> data,
                                  ValueProvider<SampleBind, Double> valueProvider) {
        double average = data.stream()
                .map(valueProvider)
                .filter(Objects::nonNull)
                .mapToDouble(value -> value)
                .average().orElse(0.d);
        footerCell.setText(String.format("Среднее %.1f", average));
    }

    private void meanLongColumn(FooterCell footerCell,
                                List<SampleBind> data,
                                ValueProvider<SampleBind, Long> valueProvider) {
        double average = data.stream()
                .map(valueProvider)
                .filter(Objects::nonNull)
                .mapToDouble(value -> value)
                .average().orElse(0.d);
        footerCell.setText(String.format("Среднее %.1f", average));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        refresh.click();
    }
}
