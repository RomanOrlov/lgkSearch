package lgk.nsbc.spect.view.statistic;

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
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.NumberRenderer;
import lgk.nsbc.util.DateUtils;
import lgk.nsbc.util.components.GridHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@VaadinSessionScope
public class SampleStatistic extends VerticalLayout implements View {
    @Autowired
    private SampleManager sampleManager;

    private Button refresh = new Button("Обновить");
    private Button sampleToExcel = new Button("Выборку в Excel");
    private Button statisticToExcel = new Button("Данные в Excel");
    private static final DecimalFormat ageFormat = new DecimalFormat("##0");

    private List<SampleBind> sampleBinds = new ArrayList<>();
    private ListDataProvider<SampleBind> dataProvider = new ListDataProvider<>(sampleBinds);
    private Grid<SampleBind> sampleGrid = new Grid<>("Выборка ОФЕКТ", dataProvider);

    @PostConstruct
    public void init() {
        setSizeFull();
        initSampleGrid();
        initButtons();
        HorizontalLayout buttons = new HorizontalLayout(refresh, sampleToExcel, statisticToExcel);
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
                .setCaption("id Пациента");
        sampleGrid.addColumn(sampleBind -> sampleBind.getPatients().getCaseHistoryNumber())
                .setCaption("Номер истории болезни")
                .setHidable(true)
                .setHidden(true);
        Grid.Column<SampleBind, String> includedColumn = sampleGrid.addColumn(SampleBind::getInclusionRepsresentation)
                .setCaption("Включен")
                .setHidable(true);
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
        Grid.Column<SampleBind, Integer> fullYearsAtSurgery = sampleGrid.addColumn(SampleBind::getAgeAtSurgery,
                new NumberRenderer(ageFormat))
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

            long included = data.stream()
                    .filter(sampleBind -> "Y".equals(sampleBind.getSamplePatients().getInclusion()))
                    .count();
            footerRow.getCell(includedColumn).setText("Включено " + included);

            double averageAge = data.stream()
                    .map(SampleBind::getAgeAtSurgery)
                    .filter(Objects::nonNull)
                    .mapToDouble(value -> value)
                    .average().orElse(0.d);
            footerRow.getCell(fullYearsAtSurgery).setText(String.format("Среднее %.1f", averageAge));
        });
        // Настраиваем фильттрацию
        HeaderRow filterHeader = sampleGrid.prependHeaderRow();
        filterHeader.setStyleName("align-center"); // ONG стало лучше выглядеть
        GridHeaderFilter.addTextFilter(filterHeader.getCell(name), dataProvider, sampleBind -> sampleBind.getPatients().toString());
        GridHeaderFilter.addIntegerFilter(filterHeader.getCell(fullYearsAtSurgery), dataProvider, SampleBind::getAgeAtSurgery);
        sampleGrid.setSortOrder(GridSortOrder.asc(name));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
