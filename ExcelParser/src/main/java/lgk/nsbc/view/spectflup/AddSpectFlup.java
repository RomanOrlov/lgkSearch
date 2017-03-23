package lgk.nsbc.view.spectflup;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import lgk.nsbc.template.dao.*;
import lgk.nsbc.template.model.*;
import lgk.nsbc.template.model.spect.TargetType;
import lgk.nsbc.util.DoubleTextField;
import lgk.nsbc.util.LongTextField;
import lgk.nsbc.util.SpectData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static lgk.nsbc.template.model.spect.ContourType.SPHERE;
import static lgk.nsbc.template.model.spect.TargetType.*;

@org.springframework.stereotype.Component
@Scope("prototype")
@NoArgsConstructor
public class AddSpectFlup extends Window {
    // Все мишени пациента
    private List<NbcTarget> targets;
    private List<TargetData> targetDataList = new ArrayList<>();
    private VerticalLayout targetsLayout = new VerticalLayout();

    private DateField dateField = new DateField("Дата исследования");
    private DoubleTextField dozeField = new DoubleTextField("Доза");
    private TextField spectNumField = new LongTextField("Номер ОФЕКТ");
    private DataUnit hypField = new DataUnit(HYP.getName());
    private DataBlock hizField = new DataBlock(HIZ);

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
    private NbcFlupSpectDao nbcFlupSpectDao;
    @Autowired
    private DSLContext context;

    private SpectData spectData;
    // Выбранный пациент
    private NbcPatients nbcPatients;
    // В случае редактирования выбранная id строчки с flup
    private Long selectedRowId;
    private DataToDelete dataToDelete;

    public AddSpectFlup(NbcPatients nbcPatients, SpectData spectData) {
        this.spectData = spectData;
        this.nbcPatients = nbcPatients;
    }

    public AddSpectFlup(NbcPatients nbcPatients, SpectData spectData, Long selectedRowId) {
        this.nbcPatients = nbcPatients;
        this.selectedRowId = selectedRowId;
        this.spectData = spectData;
    }

    @PostConstruct
    public void init() {
        VerticalLayout content = new VerticalLayout();
        this.targets = nbcTargetDao.getPatientsTargets(nbcPatients);
        dateField.setRequired(true);

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

        targetsLayout.setWidth("100%");
        targetsLayout.setHeightUndefined();
        targetsLayout.setSpacing(true);
        targetsLayout.addComponent(blank);
        targetsLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        addTargetData();

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


        HorizontalLayout buttons = initAcceptCancelButtons();

        content.setSizeFull();
        content.setSpacing(true);
        content.setMargin(new MarginInfo(false, true, true, true));
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

    /**
     * Выгрузка информации из базы и выставление даных интерфейса
     */
    private void fillWithData() {
        // Собираем данные
        Optional<NbcFlupSpect> nbcFlupSpect = nbcFlupSpectDao.findById(selectedRowId);
        if (!nbcFlupSpect.isPresent()) return;
        NbcFollowUp nbcFollowUp = nbcFollowUpDao.findById(nbcFlupSpect.get().getNbc_followup_n());
        NbcStud nbcStud = nbcStudDao.findById(nbcFollowUp.getNbc_stud_n());
        NbcStudInj nbcStudInj = nbcStudInjDao.findByStudy(nbcStud);
        List<NbcFollowUp> nbcFollowUps = nbcFollowUpDao.findByStudy(nbcStud);
        List<NbcFlupSpect> nbcFlupSpects = new ArrayList<>();
        dataToDelete = new DataToDelete(nbcFollowUps, nbcStud, nbcFlupSpects, nbcStudInj);

        // Выставляем дозу
        dozeField.setConvertedValue(nbcStudInj.getInj_activity_bq());
        // Добавляем необходимое количество строк в мишенях
        for (int i = 0; i < nbcFollowUps.size() - 1; i++) {
            addTargetData();
        }
        for (int i = 0; i < nbcFollowUps.size(); i++) {
            NbcFollowUp followUp = nbcFollowUps.get(i);
            TargetData targetDataFields = targetDataList.get(i);
            // Выставляем выбранную мишень
            NbcTarget target = targets.stream()
                    .filter(nbcTarget -> Objects.equals(nbcTarget.getN(), followUp.getNbc_target_n()))
                    .findFirst().orElseThrow(RuntimeException::new);
            targetDataList.get(i).setSelectedTarfet(target);

            Optional<NbcFlupSpect> flupSpect = nbcFlupSpectDao.findByFollowUp(followUp);
            nbcFlupSpects.add(flupSpect.get());
            List<NbcFlupSpectData> nbcFlupSpectData = nbcFlupSpectDataDao.findBySpectFlup(flupSpect.get());
            // Заполняем строчку с данными опухоли
            targetDataFields.setListOfData(nbcFlupSpectData);
            // Заполняем один раз
            if (i == 0) {
                // Заполняем гипофиз
                NbcFlupSpectData hypSphereData = SPHERE.getDataOfContour(HYP.getSublistOfTarget(nbcFlupSpectData));
                hypField.setSpectData(hypSphereData);
                // Заполняем хороидальное сплетение
                hizField.setListOfData(nbcFlupSpectData);
                spectNumField.setConvertedValue(flupSpect.get().getSpect_num());
            }
        }
        dateField.setValue(nbcStud.getStudydatetime());
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
        context.transaction(configuration -> {
            if (!nbcStudDao.isSpectStudyExist(nbcStud)) {
                nbcStudDao.createNbcStud(nbcStud);
            }
            // Информация о дозе
            NbcStudInj nbcStudInj = NbcStudInj.builder()
                    .nbc_stud_n(nbcStud.getN())
                    .inj_activity_bq(dose)
                    .build();
            nbcStudInjDao.insertStudInj(nbcStudInj);
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
                NbcFlupSpect nbcFlupSpect = NbcFlupSpect.builder()
                        .spect_num(Long.class.cast(spectNumField.getConvertedValue()))
                        .build();
                List<NbcFlupSpectData> listOfData = targetData.getListOfData();
                listOfData.forEach(data -> data.setStructure_type(TargetType.TARGET.getName()));
                listOfData.add(hyp);
                listOfData.addAll(hiz);

                nbcFlupSpectDataDao.createSpectFollowUpData(nbcFollowUp, nbcFlupSpect, listOfData);
            }
        });
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

    private void deleteOldData() {
        context.transaction(configuration -> {
            nbcStudDao.deleteStudy(dataToDelete.getNbcStud());
            nbcFollowUpDao.deleteFollowUp(dataToDelete.getNbcFollowUps());
            List<Long> ids = dataToDelete.getNbcFlupSpects()
                    .stream()
                    .map(NbcFlupSpect::getN)
                    .collect(Collectors.toList());
            nbcFlupSpectDataDao.deleteByNbcFlupSpectId(ids);
            nbcStudInjDao.deleteStudInj(dataToDelete.getNbcStudInj());
        });
    }

    @Getter
    @AllArgsConstructor
    @Builder
    private static class DataToDelete {
        private final List<NbcFollowUp> nbcFollowUps;
        private final NbcStud nbcStud;
        private final List<NbcFlupSpect> nbcFlupSpects;
        private final NbcStudInj nbcStudInj;
    }
}
