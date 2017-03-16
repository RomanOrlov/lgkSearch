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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static lgk.nsbc.template.model.spect.ContourType.*;
import static lgk.nsbc.template.model.spect.TargetType.HIZ;
import static lgk.nsbc.template.model.spect.TargetType.HYP;
import static lgk.nsbc.template.model.spect.TargetType.TARGET;

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
    private NbcFlupSpectDao nbcFlupSpectDao;
    @Autowired
    private DSLContext context;
    private NbcPatients nbcPatients;
    private Long selectedRowId;
    private DataToDelete dataToDelete;
    private Button clickSimulation;

    public AddSpectFlup() {
    }

    public AddSpectFlup(NbcPatients nbcPatients, Button simulateClick) {
        this.clickSimulation = simulateClick;
        this.nbcPatients = nbcPatients;
    }

    public AddSpectFlup(NbcPatients nbcPatients, Long selectedRowId, Button simulateClick) {
        this.nbcPatients = nbcPatients;
        this.selectedRowId = selectedRowId;
        this.clickSimulation = simulateClick;
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
        addTargets.addClickListener(event -> addTargetData());
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

    private void addTargetData() {
        TargetData targetData = new TargetData(targets);
        targetDataList.add(targetData);
        if (targetsLayout.getComponentCount() == 1) {
            targetsLayout.addComponent(targetData, 0);
            targetsLayout.setComponentAlignment(targetData, Alignment.TOP_CENTER);
        } else
            targetsLayout.addComponent(targetData, targetsLayout.getComponentCount() - 1 - 1);
    }

    /**
     * Выгрузка информации из базы и выставление даных интерфейса
     */
    private void fillWithData() {
        NbcFlupSpect nbcFlupSpect = nbcFlupSpectDao.findById(selectedRowId);
        NbcFollowUp nbcFollowUp = nbcFollowUpDao.findById(nbcFlupSpect.getNbc_followup_n());
        NbcStud nbcStud = nbcStudDao.findById(nbcFollowUp.getNbc_stud_n());
        List<NbcFollowUp> byStudy = nbcFollowUpDao.findByStudy(nbcStud);
        List<NbcFlupSpect> nbcFlupSpects = new ArrayList<>();
        dataToDelete = new DataToDelete(byStudy,nbcStud,nbcFlupSpects);
        // Добавляем необходимое количество строк в мишенях
        for (int i = 0; i < byStudy.size() - 1; i++) {
            addTargetData();
        }
        for (int i = 0; i < byStudy.size(); i++) {
            NbcFollowUp followUp = byStudy.get(i);
            TargetData targetDataFields = targetDataList.get(i);
            // Выставляем выбранную мишень
            NbcTarget target = targets.stream()
                    .filter(nbcTarget -> Objects.equals(nbcTarget.getN(), followUp.getNbc_target_n()))
                    .findFirst().orElseThrow(RuntimeException::new);
            targetDataList.get(i).selectedTarget.setValue(target);

            NbcFlupSpect flupSpect = nbcFlupSpectDao.findByFollowUp(followUp);
            nbcFlupSpects.add(flupSpect);
            List<NbcFlupSpectData> bySpectFlup = nbcFlupSpectDataDao.findBySpectFlup(flupSpect);
            // Заполняем один раз
            if (i == 0) {
                // Заполняем гипофиз
                NbcFlupSpectData hypSphereData = SPHERE.getDataOfContour(HYP.getSublistOfTarget(bySpectFlup));
                hypField.setSpectData(hypSphereData);
                // Заполняем хороидальное сплетение
                List<NbcFlupSpectData> hizSublist = HIZ.getSublistOfTarget(bySpectFlup);
                NbcFlupSpectData hizSphereData = SPHERE.getDataOfContour(hizSublist);
                NbcFlupSpectData hizIsolyne10Data = ISOLYNE10.getDataOfContour(hizSublist);
                NbcFlupSpectData hizIsolyne25Data = ISOLYNE25.getDataOfContour(hizSublist);
                hizField.setListOfData(hizSphereData, hizIsolyne10Data, hizIsolyne25Data);
            }
            // Заполняем строчку с данными опухоли
            List<NbcFlupSpectData> targetDataList = TARGET.getSublistOfTarget(bySpectFlup);
            NbcFlupSpectData targetSphereData = SPHERE.getDataOfContour(targetDataList);
            NbcFlupSpectData targetIsolyne10Data = ISOLYNE10.getDataOfContour(targetDataList);
            NbcFlupSpectData targetIsolyne25Data = ISOLYNE25.getDataOfContour(targetDataList);
            targetDataFields.setListOfData(targetSphereData, targetIsolyne10Data, targetIsolyne25Data);
        }
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
        NbcFlupSpectData hyp = hypField.getSpectData();
        hyp.setContour_size(1L);
        hyp.setStructure_type(HYP.getName());
        hyp.setContour_type(SPHERE.getName());

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
                // Удаляем старые записи
                if (selectedRowId != null) {
                    deleteOldData();
                }
                saveToDb();
                clickSimulation.click();
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

    private void deleteOldData() {
        context.transaction(configuration -> {
            nbcStudDao.deleteStudy(dataToDelete.getNbcStud());
            nbcFollowUpDao.deleteFollowUp(dataToDelete.getNbcFollowUps());
            List<Long> ids = dataToDelete.getNbcFlupSpects()
                    .stream()
                    .map(NbcFlupSpect::getN)
                    .collect(Collectors.toList());
            nbcFlupSpectDataDao.deleteByNbcFlupSpectId(ids);
        });
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
            NbcFlupSpectData sphereData = sphere.getSpectData();
            sphereData.setContour_size(1L);
            sphereData.setContour_type(SPHERE.getName());
            // Изолиния 10
            NbcFlupSpectData isolyne10Data = isolyne10.getSpectData();
            isolyne10Data.setContour_size(10L);
            isolyne10Data.setContour_type(ContourType.ISOLYNE10.getName());
            // Изолиния 25
            NbcFlupSpectData isolyne25Data = isolyne25.getSpectData();
            isolyne25Data.setContour_size(25L);
            isolyne25Data.setContour_type(ContourType.ISOLYNE25.getName());
            spectDataList.add(sphereData);
            spectDataList.add(isolyne10Data);
            spectDataList.add(isolyne25Data);
            return spectDataList;
        }

        public void setListOfData(NbcFlupSpectData sphere,
                                  NbcFlupSpectData isolyne10,
                                  NbcFlupSpectData isolyne25) {
            this.sphere.setSpectData(sphere);
            this.isolyne10.setSpectData(isolyne10);
            this.isolyne25.setSpectData(isolyne25);
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

        public NbcFlupSpectData getSpectData() {
            return NbcFlupSpectData.builder()
                    .volume(castFromObject(volume.getConvertedValue()))
                    .early_phase(castFromObject(earlyPhase.getConvertedValue()))
                    .late_phase(castFromObject(latePhase.getConvertedValue()))
                    .build();
        }

        public void setSpectData(NbcFlupSpectData hypData) {
            volume.setConvertedValue(hypData.getVolume());
            earlyPhase.setConvertedValue(hypData.getEarly_phase());
            latePhase.setConvertedValue(hypData.getLate_phase());
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

    @Getter
    @AllArgsConstructor
    @Builder
    private static class DataToDelete {
        private final List<NbcFollowUp> nbcFollowUps;
        private final NbcStud nbcStud;
        private final List<NbcFlupSpect> nbcFlupSpects;
    }
}
