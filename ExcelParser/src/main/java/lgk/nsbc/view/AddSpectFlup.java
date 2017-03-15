package lgk.nsbc.view;

import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import lgk.nsbc.template.dao.NbcTargetDao;
import lgk.nsbc.template.model.NbcPatients;
import lgk.nsbc.template.model.NbcTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Component
@Scope("prototype")
public class AddSpectFlup extends Window {
    private final List<NbcTarget> targets;
    private VerticalLayout content = new VerticalLayout();
    private VerticalLayout targetsLayout = new VerticalLayout();
    private DateField dateField = new DateField("Дата исследования");
    private TextField dozeField = new DoubleTextField("Доза");

    private DataUnit hypField = new DataUnit("Гипофиз");
    private DataBlock hizField = new DataBlock("Хориоидальное сплетение");

    private List<TargetData> targetDataList = new ArrayList<>();

    @Autowired
    private NbcTargetDao nbcTargetDao;

    public AddSpectFlup(NbcPatients nbcPatients) {
        this.targets = nbcTargetDao.getPatientsTargets(nbcPatients);
        Label label = new Label(nbcPatients.toString());
        label.setSizeUndefined();
        setCaption("Добавление данных ОФЕКТ");
        setModal(true);
        setClosable(false);
        setWidth("900px");
        setHeight("900px");
        hizField.setMargin(new MarginInfo(false, false, true, false));

        Panel panel = new Panel(targetsLayout);
        panel.setSizeFull();
        targetsLayout.setWidth("100%");
        targetsLayout.setHeightUndefined();
        targetsLayout.setSpacing(true);
        Label blank = new Label();
        blank.setHeight("55px");
        blank.setWidth("100%");
        targetsLayout.addComponent(blank);

        Button addTargets = new Button("Добавить мишень");
        Button removeSelectedTargets = new Button("Удалить выбранные");
        HorizontalLayout firstLine = new HorizontalLayout(dateField, dozeField, addTargets, removeSelectedTargets);
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
                    .filter(TargetData::isTargetSelected)
                    .forEach(components -> targetsLayout.removeComponent(components));
            targetDataList.removeIf(TargetData::isTargetSelected);
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
        setContent(content);
    }

    private HorizontalLayout initAcceptCancelButtons() {
        Button commit = new Button("Принять");
        Button cancel = new Button("Отмена");
        commit.setSizeFull();
        cancel.setSizeFull();
        commit.addClickListener(event -> {
            targetDataList.forEach(components -> components.validate());
        });
        cancel.addClickListener(event -> close());
        HorizontalLayout buttons = new HorizontalLayout(commit, cancel);
        buttons.setWidth("100%");
        buttons.setHeight("40px");
        buttons.setSpacing(true);
        return buttons;
    }

    public AddSpectFlup(NbcPatients nbcPatients, Long selectedRowId) {

    }

    /**
     * Данные для мишени
     */
    private static class TargetData extends DataBlock {
        private CheckBox isSelected = new CheckBox();
        private ComboBox selectedTarget;

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

        public boolean isTargetSelected() {
            return isSelected.getValue();
        }

        public void validate() {
            selectedTarget.validate();
        }
    }

    /**
     * DataUnit для сферы и всех изолиний
     */
    private static class DataBlock extends HorizontalLayout {
        private DataUnit sphere = new DataUnit("Сфера");
        private DataUnit isolyne10 = new DataUnit("Изолиния 10");
        private DataUnit isolyne25 = new DataUnit("Изолиния 25");

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
    }

    /**
     * Данные об объеме, ранней и поздней фазы
     */
    private static class DataUnit extends HorizontalLayout {
        private final TextField volume = new DoubleTextField("Объем");
        private final TextField earlyPhase = new DoubleTextField("30 мин");
        private final TextField latePhase = new DoubleTextField("60 мин");

        public DataUnit(String caption) {
            super();
            setWidth("100%");
            setHeight("100%");
            setCaption(caption);
            addComponents(volume, earlyPhase, latePhase);
            setSpacing(true);
        }

        public Double[] getData() {
            Object[] data = new Object[]{volume.getConvertedValue(),
                    earlyPhase.getConvertedValue(),
                    latePhase.getConvertedValue()
            };
            return (Double[]) data;
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
