package lgk.nsbc.view.spectflup;

import com.vaadin.data.Binder;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import lgk.nsbc.dao.*;
import lgk.nsbc.model.NbcPatients;
import lgk.nsbc.model.NbcTarget;
import lgk.nsbc.util.SpectData;
import lgk.nsbc.view.spectflup.bind.SpectDataPropertySet;
import lgk.nsbc.view.spectflup.bind.SpectFlupData;
import lombok.NoArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static lgk.nsbc.model.spect.ContourType.SPHERE;
import static lgk.nsbc.model.spect.TargetType.*;

@SpringComponent
@Scope("prototype")
@NoArgsConstructor
public class AddSpectFlup extends Window {
    // Все мишени пациента
    private VerticalLayout targetsLayout = new VerticalLayout();

    private DateField dateField = new DateField("Дата исследования");
    private TextField dozeField = new TextField("Доза");
    private DataUnit hypField;
    private DataBlock hizField;
    private final List<TargetData> targetDataList = new ArrayList<>();
    private Binder<SpectFlupData> bind;

    @Autowired
    private NbcStudInjDao nbcStudInjDao;
    @Autowired
    private NbcStudDao nbcStudDao;
    @Autowired
    private NbcFollowUpDao nbcFollowUpDao;
    @Autowired
    private NbcFlupSpectDataDao nbcFlupSpectDataDao;
    @Autowired
    private NbcTargetDao nbcTargetDao;
    @Autowired
    private DSLContext context;

    private SpectData spectData;
    // Выбранный пациент
    private NbcPatients nbcPatients;
    private List<NbcTarget> targets;
    // В случае редактирования выбранная id строчки с flup
    private Long selectedRowId;


    public AddSpectFlup(NbcPatients nbcPatients, SpectData spectData) {
        this.spectData = spectData;
        this.nbcPatients = nbcPatients;
        bind = Binder.withPropertySet(new SpectDataPropertySet(null));
        hypField = new DataUnit(bind, SPHERE, HYP);
        hizField = new DataBlock(bind, HIZ);
    }

    public AddSpectFlup(NbcPatients nbcPatients, SpectData spectData, Long selectedRowId) {
        this(nbcPatients, spectData);
        this.selectedRowId = selectedRowId;
    }

    @PostConstruct
    public void init() {
        VerticalLayout content = new VerticalLayout();
        this.targets = nbcTargetDao.getPatientsTargets(nbcPatients);

        Label shortPatientInfo = new Label(nbcPatients.toString());
        shortPatientInfo.setSizeUndefined();

        setCaption("Добавление данных ОФЕКТ");
        setModal(true);
        setClosable(false);
        setSizeFull();

        hizField.setMargin(new MarginInfo(false, false, true, false));

        Panel targetsPanel = new Panel(targetsLayout);
        targetsPanel.setSizeFull();

        // Пустышка, лишь для красивого формата
        Label blank = new Label();
        blank.setHeight("55px");
        blank.setWidth("100%");

        targetsLayout.addComponent(blank);
        targetsLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        addTargetData();

        Button addTargets = new Button("Добавить мишень");
        Button removeSelectedTargets = new Button("Удалить выбранные");
        HorizontalLayout firstLine = new HorizontalLayout(dateField, dozeField, addTargets, removeSelectedTargets);
        firstLine.setWidth("100%");
        firstLine.setHeight("40px");
        firstLine.setComponentAlignment(addTargets, Alignment.MIDDLE_CENTER);
        firstLine.setComponentAlignment(removeSelectedTargets, Alignment.MIDDLE_CENTER);
        addTargets.addClickListener(event -> addTargetData());
        removeSelectedTargets.addClickListener(event -> {
            targetDataList.stream()
                    .filter(components -> components.isSelected.getValue())
                    .forEach(components -> targetsLayout.removeComponent(components));
            targetDataList.removeIf(TargetData::isRowSelected);
        });


        HorizontalLayout buttons = initAcceptCancelButtons();

        content.setSizeFull();
        content.addComponents(shortPatientInfo, firstLine, hypField, hizField, targetsPanel, buttons);
        content.setExpandRatio(targetsPanel, 0.5f);
        content.setExpandRatio(hizField, 0.1f);
        content.setExpandRatio(hypField, 0.1f);
        content.setExpandRatio(firstLine, 0.08f);

        // Если мы нажали на кнопку просмотреть, - вытаскиваем информацию
        // Напоминаю, что нам был передан id SpectFlup
        if (selectedRowId != null) {
            fillWithData();
        }
        setContent(content);
    }

    private void addTargetData() {
        TargetData targetData = new TargetData(targets, TARGET);
        targetDataList.add(targetData);
        if (targetsLayout.getComponentCount() == 1) {
            targetsLayout.addComponent(targetData, 0);
        } else
            targetsLayout.addComponent(targetData, targetsLayout.getComponentCount() - 1 - 1);
    }

    private HorizontalLayout initAcceptCancelButtons() {
        Button commit = new Button("Принять");
        Button cancel = new Button("Отмена");
        commit.setSizeFull();
        cancel.setSizeFull();
        commit.addClickListener(event -> {
            if (!isInputDataValid()) return;
            // Удаляем старые записи
            if (selectedRowId != null) {
                deleteOldData();
            }
            saveToDb();
            // Обновляем результат.
            spectData.readData(nbcPatients);
            close();
        });
        cancel.addClickListener(event -> close());
        HorizontalLayout buttons = new HorizontalLayout(commit, cancel);
        buttons.setWidth("100%");
        buttons.setHeight("40px");
        buttons.setSpacing(true);
        return buttons;
    }
}
