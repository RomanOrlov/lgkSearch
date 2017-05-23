package lgk.nsbc.spect.view.statistic;

import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.FooterCell;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.NumberRenderer;
import lgk.nsbc.util.DateUtils;
import lgk.nsbc.util.components.GridHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@VaadinSessionScope
public class SampleStatistic extends VerticalLayout implements View, Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired
    private SampleManager sampleManager;

    private Button refresh = new Button("Обновить");
    private Button statisticToExcel = new Button("Данные в Excel");
    private static final DecimalFormat ageFormat = new DecimalFormat("##0");
    private static final DecimalFormat inFormat = new DecimalFormat("##0.00");

    private List<SampleBind> sampleBinds = new ArrayList<>();
    private ListDataProvider<SampleBind> dataProvider = new ListDataProvider<>(sampleBinds);
    private Grid<SampleBind> sampleGrid = new Grid<>("Выборка ОФЕКТ", dataProvider);

    @PostConstruct
    public void init() {
        setSizeFull();
        initSampleGrid();
        initButtons();
        HorizontalLayout buttons = new HorizontalLayout(refresh, statisticToExcel);
        buttons.setWidth("100%");
        addComponents(buttons, sampleGrid);
        setExpandRatio(sampleGrid, 1.0f);
    }

    private void initButtons() {
        refresh.addClickListener(event -> {
            List<SampleBind> spectSample = sampleManager.getSpectSample();
            sampleBinds.clear();
            sampleBinds.addAll(spectSample);
            sampleGrid.getDataProvider().refreshAll();
        });
    }

    private void initSampleGrid() {
        // ФИО
        Grid.Column<SampleBind, String> name = sampleGrid.addColumn(sampleBind -> sampleBind.getPatients().toString())
                .setCaption("ФИО")
                .setHidable(true);
        sampleGrid.addColumn(sampleBind -> sampleBind.getPatients().getN())
                .setCaption("id Пациента")
                .setHidable(true);
        Grid.Column<SampleBind, String> gender = sampleGrid.addColumn(sampleBind -> sampleBind.getPatients().getPeople().getSex())
                .setCaption("Пол")
                .setHidable(true)
                .setHidden(true);
        sampleGrid.addColumn(sampleBind -> sampleBind.getPatients().getCaseHistoryNumber())
                .setCaption("Номер истории болезни")
                .setHidable(true)
                .setHidden(true);
        Grid.Column<SampleBind, String> includedColumn = sampleGrid.addColumn(SampleBind::getInclusionRepresentation)
                .setCaption("Включен")
                .setHidable(true)
                .setHidden(true);
        sampleGrid.addColumn(sampleBind -> sampleBind.getSamplePatients().getComment())
                .setCaption("Комментарий")
                .setHidable(true)
                .setHidden(true);

        sampleGrid.addColumn(sampleBind -> DateUtils.asLocalDate(sampleBind.getPatients().getPeople().getBirthday()))
                .setCaption("Дата рождения")
                .setHidable(true)
                .setHidden(true);
        sampleGrid.addColumn(sampleBind -> {
            if (sampleBind.getSurgeryProc() == null) return null;
            return DateUtils.asLocalDate(sampleBind.getSurgeryProc().getProcBeginTime());
        })
                .setHidable(true)
                .setCaption("Дата хирургии");
        sampleGrid.addColumn(sampleBind -> {
            if (sampleBind.getRadioProc() == null) return null;
            return DateUtils.asLocalDate(sampleBind.getRadioProc().getProcBeginTime());
        }).setCaption("Начало ЛТ")
                .setHidable(true)
                .setHidden(true);

        sampleGrid.addColumn(sampleBind -> {
            if (sampleBind.getRadioProc() == null) return null;
            return DateUtils.asLocalDate(sampleBind.getRadioProc().getProcEndTime());
        }).setCaption("Конец ЛТ")
                .setHidable(true)
                .setHidden(true);

        sampleGrid.addColumn(SampleBind::getMgmt)
                .setCaption("MGMT")
                .setHidable(true);

        sampleGrid.addColumn(SampleBind::getIdh1)
                .setCaption("IDH1")
                .setHidable(true);

        sampleGrid.addColumn(SampleBind::getIdh2)
                .setCaption("IDH2")
                .setHidable(true);

        sampleGrid.addColumn(sampleBind -> {
            if (sampleBind.getSpect1() == null) return null;
            return sampleBind.getSpect1().getStudyDate();
        }).setCaption("#1 ОФЕКТ")
                .setHidable(true);

        Grid.Column<SampleBind, Double> spect1InEarly = sampleGrid.addColumn(SampleBind::getSpect1InEarly, new NumberRenderer(inFormat))
                .setCaption("#1 ИН 30")
                .setHidable(true);

        Grid.Column<SampleBind, Double> spect1InLate = sampleGrid.addColumn(SampleBind::getSpect1InLate, new NumberRenderer(inFormat))
                .setCaption("#1 ИН 60")
                .setHidable(true);

        Grid.Column<SampleBind, Double> spect1InOut = sampleGrid.addColumn(SampleBind::getSpect1InOut, new NumberRenderer(inFormat))
                .setCaption("#1 Вымывание")
                .setHidable(true);

        Grid.Column<SampleBind, Integer> fullYearsAtSurgery = sampleGrid.addColumn(SampleBind::getAgeAtSurgery, new NumberRenderer(ageFormat))
                .setCaption("Возраст (на момент хирургии)")
                .setHidable(true)
                .setHidden(true);

        sampleGrid.setSizeFull();
        sampleGrid.setFrozenColumnCount(1);
        sampleGrid.setSelectionMode(Grid.SelectionMode.NONE);
        // Заполняем маленькую статистику
        FooterRow footerRow = sampleGrid.appendFooterRow();
        sampleGrid.getDataProvider().addDataProviderListener(event -> {
            List<SampleBind> data = event.getSource()
                    .fetch(new Query<>())
                    .collect(toList());
            footerRow.getCell(name).setText("Всего: " + data.size());
            footerRow.getCell(gender).setText(getGenderStats(data));
            footerRow.getCell(includedColumn).setText(getIncludedStats(data));

            meanColumn(footerRow.getCell(fullYearsAtSurgery), data, sampleBind -> sampleBind.getAgeAtSurgery().doubleValue());
            meanColumn(footerRow.getCell(spect1InEarly), data, SampleBind::getSpect1InEarly);
            meanColumn(footerRow.getCell(spect1InLate), data, SampleBind::getSpect1InLate);
            meanColumn(footerRow.getCell(spect1InOut), data, SampleBind::getSpect1InOut);
        });
        // Настраиваем фильттрацию
        HeaderRow filterHeader = sampleGrid.prependHeaderRow();
        filterHeader.setStyleName("align-center"); // ONG стало лучше выглядеть
        GridHeaderFilter.addTextFilter(filterHeader.getCell(name), dataProvider, sampleBind -> sampleBind.getPatients().toString());
        GridHeaderFilter.addListSelectFilter(filterHeader.getCell(gender), dataProvider, sampleBind -> sampleBind.getPatients().getPeople().getSex(), Arrays.asList("М", "Ж"));
        GridHeaderFilter.addIntegerFilter(filterHeader.getCell(fullYearsAtSurgery), dataProvider, SampleBind::getAgeAtSurgery);
        GridHeaderFilter.addListSelectFilter(filterHeader.getCell(includedColumn), dataProvider, SampleBind::getInclusionRepresentation, Arrays.asList("Да", "Нет"));

        GridHeaderFilter.addDoubleFilter(filterHeader.getCell(spect1InEarly), dataProvider, SampleBind::getSpect1InEarly);
        GridHeaderFilter.addDoubleFilter(filterHeader.getCell(spect1InLate), dataProvider, SampleBind::getSpect1InLate);
        GridHeaderFilter.addDoubleFilter(filterHeader.getCell(spect1InOut), dataProvider, SampleBind::getSpect1InOut);
        sampleGrid.setSortOrder(GridSortOrder.asc(name));
    }

    private String getIncludedStats(List<SampleBind> data) {
        return "Включено " + data.stream()
                .filter(sampleBind -> "Y".equals(sampleBind.getSamplePatients().getInclusion()))
                .count();
    }

    private String getGenderStats(List<SampleBind> data) {
        String format = "М - %d(%.1f%%)  Ж - %d(%.1f%%)";
        if (data.isEmpty()) return String.format(format, 0, 0d, 0d, 0);
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

    private void meanColumn(FooterCell footerCell,
                            List<SampleBind> data,
                            ValueProvider<SampleBind, Double> valueProvider) {
        double average = data.stream()
                .map(valueProvider)
                .filter(Objects::nonNull)
                .mapToDouble(value -> value)
                .average().orElse(0.d);
        footerCell.setText(String.format("Среднее %.1f", average));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
