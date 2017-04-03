package lgk.nsbc.view.spectflup;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import lgk.nsbc.dao.NbcTargetDao;
import lgk.nsbc.model.NbcPatients;
import lgk.nsbc.model.NbcTarget;
import lgk.nsbc.util.DateUtils;
import lgk.nsbc.view.spectflup.bind.SpectDBData;
import lgk.nsbc.view.spectflup.bind.SpectDataPropertySet;
import lgk.nsbc.view.spectflup.bind.SpectFlupData;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static lgk.nsbc.model.spect.ContourType.SPHERE;
import static lgk.nsbc.model.spect.TargetType.HIZ;
import static lgk.nsbc.model.spect.TargetType.HYP;
import static lgk.nsbc.view.spectflup.bind.SpectDataPropertySet.MAX_TARGETS;

@SpringComponent
@Scope("prototype")
@NoArgsConstructor
public class AddSpectFlup extends Window {
    private SpectDataPropertySet propertySet = new SpectDataPropertySet();
    private Binder<SpectFlupData> bind = Binder.withPropertySet(propertySet);
    private SpectDBData spectDBData;

    private NbcPatients nbcPatients;
    // В случае редактирования
    private Date studyDate;
    @Autowired
    private NbcTargetDao nbcTargetDao;
    @Autowired
    private BeanFactory beanFactory;

    public AddSpectFlup(NbcPatients nbcPatients) {
        this.nbcPatients = nbcPatients;
    }

    public AddSpectFlup(NbcPatients nbcPatients, Date studyDate) {
        this(nbcPatients);
        this.studyDate = studyDate;
    }

    public AddSpectFlup(NbcPatients nbcPatients, LocalDate studyDate) {
        this(nbcPatients);
        this.studyDate = DateUtils.asDate(studyDate);
    }

    @PostConstruct
    public void init() {
        setCaption("Добавление данных ОФЕКТ");
        setModal(true);
        setClosable(false);
        setSizeFull();

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        Label shortPatientInfo = new Label(nbcPatients.toString());
        DateField dateField = new DateField("Дата исследования");
        bind.forField(dateField)
                .asRequired("Выставите дату исследования")
                .bind("studyDate");
        TextField dozeField = new TextField("Доза");
        bind.forField(dateField)
                .asRequired("Выставите дозу")
                .bind("dose");
        DataUnit hypField = new DataUnit(bind, SPHERE, HYP);
        DataBlock hizField = new DataBlock(bind, HIZ);

        HorizontalLayout firstLine = new HorizontalLayout(dateField, dozeField);
        firstLine.setWidth("100%");

        List<NbcTarget> patientsTargets = nbcTargetDao.getPatientsTargets(nbcPatients);
        VerticalLayout targetsLayout = new VerticalLayout();
        targetsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        for (int i = 0; i < MAX_TARGETS; i++) {
            TargetData targetData = new TargetData(bind, patientsTargets, i);
            targetsLayout.addComponent(targetData);
        }

        HorizontalLayout buttons = initAcceptCancelButtons();
        content.addComponents(shortPatientInfo, firstLine, hypField, hizField, targetsLayout, buttons);
        content.setExpandRatio(targetsLayout, 1);

        setContent(content);

        if (studyDate == null)
            spectDBData = beanFactory.getBean(SpectDBData.class, nbcPatients);
        else {
            spectDBData = beanFactory.getBean(SpectDBData.class, nbcPatients, studyDate);
            spectDBData.readFromDB(propertySet.getSpectData());
            bind.readBean(propertySet.getSpectData());
        }
    }

    private HorizontalLayout initAcceptCancelButtons() {
        Button commit = new Button("Принять");
        Button cancel = new Button("Отмена", event -> close());
        commit.setStyleName("primary");
        cancel.setStyleName("primary");
        commit.addClickListener(event -> {
            if (bind.isValid() && bind.hasChanges()) {
                try {
                    bind.writeBean(propertySet.getSpectData());
                    spectDBData.writeToDB(propertySet.getSpectData());
                    close();
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }
        });
        return new HorizontalLayout(commit, cancel);
    }
}
