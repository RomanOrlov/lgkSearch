package lgk.nsbc.view.spectflup;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.shared.ui.ContentMode;
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
import static lgk.nsbc.model.spect.TargetType.*;
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
        setCaption("Добавление данных ОФЕКТ - " + nbcPatients.toString());
        setModal(true);
        setClosable(false);
        setSizeFull();


        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setSpacing(false);

        Label hypLabel = new Label("<h4>" + HYP.getName() + ":</h4>", ContentMode.HTML);
        hypLabel.setHeightUndefined();
        Label hizLabel = new Label("<h4>" + HIZ.getName() + "</h4>", ContentMode.HTML);
        hizLabel.setHeightUndefined();
        Label targetLabel = new Label("<h4>" + TARGET.getName() + "</h4>", ContentMode.HTML);
        targetLabel.setHeightUndefined();
        DateField dateField = new DateField("Дата исследования");
        bind.forField(dateField)
                .asRequired("Выставите дату исследования")
                .bind("studyDate");

        TextField dozeField = new TextField("Доза");
        bind.forField(dozeField)
                .asRequired("Выставите дозу")
                .withConverter(new StringToDoubleConverter("Неверное значение"))
                .bind("dose");
        DataUnit hypField = new DataUnit(bind, SPHERE, HYP);
        DataBlock hizField = new DataBlock(bind, HIZ);

        hypField.addComponent(hypLabel, 0);
        hypField.addComponent(dateField, 0);
        hypField.addComponent(dozeField, 0);

        HorizontalLayout buttons = initAcceptCancelButtons();
        content.addComponents(hypField, hizLabel, hizField, targetLabel);
        content.setExpandRatio(hypField, 1);
        content.setComponentAlignment(hizLabel, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(targetLabel, Alignment.MIDDLE_CENTER);
        List<NbcTarget> patientsTargets = nbcTargetDao.getPatientsTargets(nbcPatients);
        for (int i = 0; i < MAX_TARGETS; i++) {
            TargetData targetData = new TargetData(bind, patientsTargets, i);
            content.addComponent(targetData);
            content.setExpandRatio(targetData, 1);
        }
        content.addComponent(buttons);
        content.setExpandRatio(buttons,1);
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
        commit.setWidth("200px");
        cancel.setWidth("200px");
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
        HorizontalLayout components = new HorizontalLayout();
        components.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        components.addComponents(commit, cancel);
        components.setSizeFull();
        return components;
    }
}
