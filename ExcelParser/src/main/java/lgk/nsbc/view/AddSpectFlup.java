package lgk.nsbc.view;

import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.LongRangeValidator;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import lgk.nsbc.template.dao.*;
import lgk.nsbc.template.model.*;
import lgk.nsbc.template.model.spect.ContourType;
import lgk.nsbc.template.model.spect.TargetType;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Component
@Scope("prototype")
public class AddSpectFlup extends Window {
    private List<NbcTarget> targets;
    private VerticalLayout content = new VerticalLayout();
    private VerticalLayout targetsLayout = new VerticalLayout();
    private DateField dateField = new DateField("Дата исследования");
    private DoubleTextField dozeField = new DoubleTextField("Доза");
    private TextField spectNumField = new TextField("Номер ОФЕКТ");

    private DataUnit hypField = new DataUnit("Гипофиз");
    private DataBlock hizField = new DataBlock("Хориоидальное сплетение");

    private List<TargetData> targetDataList = new ArrayList<>();

    @Autowired
    private BasPeopleDao basPeopleDao;
    @Autowired
    private NbcPatientsDao nbcPatientsDao;
    @Autowired
    private NbcProcDao nbcProcDao;
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
    private NbcPatients nbcPatients;
    private Long selectedRowId;

    public AddSpectFlup() {
    }

    public AddSpectFlup(NbcPatients nbcPatients) {
        this.nbcPatients = nbcPatients;
    }

    public AddSpectFlup(NbcPatients nbcPatients, Long selectedRowId) {
        this.nbcPatients = nbcPatients;
        this.selectedRowId = selectedRowId;
    }

    @PostConstruct
    public void init() {
        this.targets = nbcTargetDao.getPatientsTargets(nbcPatients);
        dateField.setRequired(true);
        spectNumField.addValidator(new LongRangeValidator("Неверный параметр", 0L, Long.MAX_VALUE));
        spectNumField.setConverter(new StringToLongConverter());
        spectNumField.setNullRepresentation("");

        Label label = new Label(nbcPatients.toString());
        label.setSizeUndefined();

        setCaption("Добавление данных ОФЕКТ");
        setModal(true);
        setClosable(false);
        setSizeFull();

        hizField.setMargin(new MarginInfo(false, false, true, false));

        Panel panel = new Panel(targetsLayout);
        panel.setSizeFull();

        // Пустышка, лишь для красивого формата
        Label blank = new Label();
        blank.setHeight("55px");
        blank.setWidth("100%");

        targetsLayout.setWidth("100%");
        targetsLayout.setHeightUndefined();
        targetsLayout.setSpacing(true);
        targetsLayout.addComponent(blank);

        Button addTargets = new Button("Добавить мишень");
        Button removeSelectedTargets = new Button("Удалить выбранные");
        HorizontalLayout firstLine = new HorizontalLayout(dateField, dozeField, spectNumField, addTargets, removeSelectedTargets);
        firstLine.setWidth("100%");
        firstLine.setHeight("40px");
        firstLine.setSpacing(true);
        firstLine.setComponentAlignment(addTargets, Alignment.MIDDLE_CENTER);
        firstLine.setComponentAlignment(removeSelectedTargets, Alignment.MIDDLE_CENTER);
        addTargets.addClickListener(event -> {
            TargetData targetData = new TargetData(targets);
            targetDataList.add(targetData);
            if (targetsLayout.getComponentCount() == 1) {
                targetsLayout.addComponent(targetData, 0);
                targetsLayout.setComponentAlignment(targetData, Alignment.TOP_CENTER);
            } else
                targetsLayout.addComponent(targetData, targetsLayout.getComponentCount() - 1 - 1);
        });
        removeSelectedTargets.addClickListener(event -> {
            targetDataList.stream()
                    .filter(TargetData::isRowSelected)
                    .forEach(components -> targetsLayout.removeComponent(components));
            targetDataList.removeIf(TargetData::isRowSelected);
        });

        TargetData targetData = new TargetData(targets);
        targetDataList.add(targetData);
        targetsLayout.addComponent(targetData, 0);
        targetsLayout.setComponentAlignment(targetData, Alignment.TOP_CENTER);

        HorizontalLayout buttons = initAcceptCancelButtons();

        content.setSizeFull();
        content.setSpacing(true);
        //content.addComponents(label, firstLine, hypField, hizField, targetsLayout, buttons);
        content.addComponents(label, firstLine, hypField, hizField, panel, buttons);
        content.setExpandRatio(panel, 0.5f);
        content.setExpandRatio(hizField, 0.1f);
        content.setExpandRatio(hypField, 0.1f);
        content.setExpandRatio(firstLine, 0.08f);
        //content.setExpandRatio(label, 0.1f);
        //content.setExpandRatio(buttons, 0.1f);

        // Если мы нажали на кнопку просмотреть, - вытаскиваем информацию
        // Напоминаю, что нам был передан id SpectFlup
        if (selectedRowId != null) {
            fillWithData();
        }
        setContent(content);
    }

    /**
     * Выгрузка информации из базы и выставление даных интерфейса
     */
    private void fillWithData() {
        // Найти все записи по выбранной строчке
        // (находим исследование, в рамках которого сделано
        // Толкаем эти данные в таблицу, удаляя старые
        // ПОка легче удалять и вставлять.
    }

    /**
     * Непосредстивенное сохранение в базу
     */
    private void saveToDb() {
        Date studyDate = dateField.getValue();
        Double dose = (Double) dozeField.getConvertedValue();
        NbcStud nbcStud = NbcStud.builder()
                .studydatetime(studyDate)
                .study_type(11L)
                .nbc_patients_n(nbcPatients.getN())
                .build();
        if (!nbcStudDao.isSpectStudyExist(nbcStud)) {
            nbcStudDao.createNbcStud(nbcStud);
        }
        // Общие данные,  - гипофиз, хор. сплетение.
        // Гипофиз
        NbcFlupSpectData hyp = hypField.getData();
        hyp.setContour_size(1L);
        hyp.setStructure_type(TargetType.HYP.getName());
        hyp.setContour_type(ContourType.SPHERE.getName());

        // Хороидальное сплетение
        List<NbcFlupSpectData> hiz = hizField.getListOfData();
        hiz.forEach(hizData -> hizData.setStructure_type(TargetType.HIZ.getName()));

        for (TargetData targetData : targetDataList) {
            NbcFollowUp nbcFollowUp = NbcFollowUp.builder()
                    .nbc_stud_n(nbcStud.getN())
                    .nbc_target_n(targetData.getSelectedTargetN())
                    .build();
            Object spectNumValue = spectNumField.getConvertedValue();
            NbcFlupSpect nbcFlupSpect = NbcFlupSpect.builder()
                    .spect_num(spectNumValue == null ? null : (Long) spectNumValue)
                    .build();
            List<NbcFlupSpectData> listOfData = targetData.getListOfData();
            listOfData.forEach(data -> data.setStructure_type(TargetType.TARGET.getName()));
            listOfData.add(hyp);
            listOfData.addAll(hiz);

            nbcFlupSpectDataDao.createSpectFollowUpData(nbcFollowUp, nbcFlupSpect, listOfData);
        }
    }

    private static Double castFromObject(Object value) {
        if (value == null) return null;
        return (Double) value;
    }

    /**
     * Предикат валидности данных, - выставлена дата, доза, выставлены мишени.
     *
     * @return Валидны ли данные
     */
    private boolean isInputDataValid() {
        if (dateField.isEmpty()) {
            Notification.show("Дата исследовантя не выставлена");
            return false;
        }
        if (dozeField.isEmpty()) {
            Notification.show("Нет значения дозы");
            return false;
        }
        if (targetDataList.stream().anyMatch(TargetData::isEmpty)) {
            Notification.show("Не все мишени выбраны");
            return false;
        }
        return true;
    }

    private HorizontalLayout initAcceptCancelButtons() {
        Button commit = new Button("Принять");
        Button cancel = new Button("Отмена");
        commit.setSizeFull();
        cancel.setSizeFull();
        commit.addClickListener(event -> {
            if (isInputDataValid()) {
                saveToDb();
                close();
            }
        });
        cancel.addClickListener(event -> close());
        HorizontalLayout buttons = new HorizontalLayout(commit, cancel);
        buttons.setWidth("100%");
        buttons.setHeight("40px");
        buttons.setSpacing(true);
        return buttons;
    }


    /**
     * Данные для мишени
     */
    private static class TargetData extends DataBlock {
        CheckBox isSelected = new CheckBox();
        ComboBox selectedTarget;

        public TargetData(List<NbcTarget> targets) {
            HorizontalLayout components = new HorizontalLayout();
            selectedTarget = new ComboBox("Выберите мишень", targets);
            selectedTarget.setRequired(true);
            selectedTarget.setNullSelectionAllowed(false);
            selectedTarget.setWidth("170px");
            selectedTarget.setHeight("40px");
            components.addComponents(isSelected, selectedTarget);
            addComponent(components, 0);
            components.setComponentAlignment(isSelected, Alignment.BOTTOM_LEFT);
            components.setComponentAlignment(selectedTarget, Alignment.BOTTOM_LEFT);
            components.setSpacing(true);
            isSelected.setHeight("40px");
            isSelected.setWidth("20px");
            MarginInfo margin = new MarginInfo(true, false, true, false);
            setMargin(margin);
            setExpandRatio(components, 0.9f);
        }

        public boolean isRowSelected() {
            return isSelected.getValue();
        }

        public boolean isEmpty() {
            return selectedTarget.isEmpty();
        }

        public Long getSelectedTargetN() {
            NbcTarget nbcTarget = (NbcTarget) selectedTarget.getValue();
            return nbcTarget.getN();
        }
    }

    /**
     * DataUnit для сферы и всех изолиний
     */
    private static class DataBlock extends HorizontalLayout {
        protected DataUnit sphere = new DataUnit("Сфера");
        protected DataUnit isolyne10 = new DataUnit("Изолиния 10");
        protected DataUnit isolyne25 = new DataUnit("Изолиния 25");

        public DataBlock(String caption) {
            this();
            setCaption(caption);
        }

        public DataBlock() {
            setWidth("100%");
            setHeight("40px");
            setSpacing(true);
            addComponents(sphere, isolyne10, isolyne25);
            setExpandRatio(sphere, 1.0f);
            setExpandRatio(isolyne10, 1.0f);
            setExpandRatio(isolyne25, 1.0f);
        }

        public List<NbcFlupSpectData> getListOfData() {
            List<NbcFlupSpectData> spectDataList = new ArrayList<>();
            // Сфера
            NbcFlupSpectData sphereData = sphere.getData();
            sphereData.setContour_size(1L);
            sphereData.setContour_type(ContourType.SPHERE.getName());
            // Изолиния 10
            NbcFlupSpectData isolyne10Data = isolyne10.getData();
            isolyne10Data.setContour_size(10L);
            isolyne10Data.setContour_type(ContourType.ISOLYNE10.getName());
            // Изолиния 25
            NbcFlupSpectData isolyne25Data = isolyne25.getData();
            isolyne25Data.setContour_size(25L);
            isolyne25Data.setContour_type(ContourType.ISOLYNE25.getName());
            spectDataList.add(sphereData);
            spectDataList.add(isolyne10Data);
            spectDataList.add(isolyne25Data);
            return spectDataList;
        }
    }

    /**
     * Данные об объеме, ранней и поздней фазы
     */
    private static class DataUnit extends HorizontalLayout {
        final TextField volume = new DoubleTextField("Объем");
        final TextField earlyPhase = new DoubleTextField("30 мин");
        final TextField latePhase = new DoubleTextField("60 мин");

        public DataUnit(String caption) {
            super();
            setWidth("100%");
            setHeight("100%");
            setCaption(caption);
            addComponents(volume, earlyPhase, latePhase);
            setSpacing(true);
        }

        public NbcFlupSpectData getData() {
            return NbcFlupSpectData.builder()
                    .volume(castFromObject(volume.getConvertedValue()))
                    .early_phase(castFromObject(earlyPhase.getConvertedValue()))
                    .late_phase(castFromObject(latePhase.getConvertedValue()))
                    .build();
        }
    }

    private static class DoubleTextField extends TextField {
        public DoubleTextField(String caption) {
            super(caption);
            setHeight("40px");
            setWidth("100%");
            setNullRepresentation("");
            addValidator(new DoubleRangeValidator("Некорректное число", 0.d, Double.MAX_VALUE));
            setConverter(new StringToDoubleConverter());
        }
    }
}
