package lgk.nsbc.histology;

import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Setter;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.*;
import lgk.nsbc.model.dao.dictionary.DicYesNoDao;
import lgk.nsbc.model.dao.PatientsDao;
import lgk.nsbc.model.dao.dictionary.GenesDao;
import lgk.nsbc.model.dao.dictionary.MutationTypesDao;
import lgk.nsbc.model.dictionary.Genes;
import lgk.nsbc.util.components.SuggestionCombobox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Service
@VaadinSessionScope
public class HistologyView extends VerticalLayout implements View {
    @Autowired
    private PatientsDao patientsDao;
    @Autowired
    private GenesDao genesDao;
    @Autowired
    private DicYesNoDao dicYesNoDao;
    @Autowired
    private MutationTypesDao mutationTypesDao;

    private SuggestionCombobox suggestionCombobox;

    @PostConstruct
    public void init() {
        Button lookUp = new Button("Просмотр");
        Button add = new Button("Добавить");
        Button remove = new Button("Удалить");
        Button edit = new Button("Редактировать");
        suggestionCombobox = new SuggestionCombobox(patientsDao::getPatientsWithDifferetNames);
        HorizontalLayout tools = new HorizontalLayout(suggestionCombobox, lookUp, add, remove, edit);
        Label patientsName = new Label();
        suggestionCombobox.addValueChangeListener(event -> patientsName.setValue(event.getValue().toString()));

        Grid<HistologyBind> histologyGrid = new Grid<>();

        DateField dateField = new DateField("Дата гистологии");

        AbstractOrderedLayout mutationGrid = initMutationGrid();
        addComponents(tools, patientsName, mutationGrid);
    }

    private AbstractOrderedLayout initMutationGrid() {
        Button addMutation = new Button("Добавить мутацию");
        Button removeMutation = new Button("Удалить мутацию");
        VerticalLayout mutationButtons = new VerticalLayout(addMutation, removeMutation);
        Grid<MutationBind> mutationGrid = new Grid<>("Мутации");
        mutationGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        Grid.Column<MutationBind, Genes> genesColumn = mutationGrid.addColumn(MutationBind::getGenes)
                .setCaption("Ген")
                .setEditorBinding(getEditorBind(mutationGrid,
                        new NativeSelect<>("Ген", genesDao.getGenes().values()),
                        MutationBind::getGenes,
                        MutationBind::setGenes)
                );
        Grid.Column<MutationBind, Genes> mutationTypesColumn = mutationGrid.addColumn(MutationBind::getGenes)
                .setCaption("Тип мутации")
                .setEditorBinding(getEditorBind(mutationGrid,
                        new NativeSelect<>("Тип мутации", mutationTypesDao.getMutationTypes().values()),
                        MutationBind::getMutationTypes,
                        MutationBind::setMutationTypes)
                );
        Grid.Column<MutationBind, Genes> yesNoColumn = mutationGrid.addColumn(MutationBind::getGenes)
                .setCaption("Наличие")
                .setEditorBinding(getEditorBind(mutationGrid,
                        new NativeSelect<>("Наличие", dicYesNoDao.getDicYesNo().values()),
                        MutationBind::getDicYesNo,
                        MutationBind::setDicYesNo)
                );
        HorizontalLayout mutation = new HorizontalLayout(mutationGrid, mutationButtons);
        return mutation;
    }

    private <T> Binder.Binding<MutationBind, T> getEditorBind(Grid<MutationBind> mutationGrid, NativeSelect<T> editSelect, ValueProvider<MutationBind, T> getter, Setter<MutationBind, T> setter) {
        editSelect.setEmptySelectionAllowed(true);
        return mutationGrid.getEditor()
                .getBinder()
                .forField(editSelect)
                .asRequired("Значение должно быть выбрано")
                .withValidator(Objects::nonNull, "Значение должно быть выбрано")
                .bind(getter, setter);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
