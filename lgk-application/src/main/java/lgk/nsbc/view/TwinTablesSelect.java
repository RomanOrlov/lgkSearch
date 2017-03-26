package lgk.nsbc.view;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.v7.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.*;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.v7.ui.*;
import lgk.nsbc.backend.info.SelectableInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Класс, при помощи котрого можно удобно оперерировать списками (Выбирать что то, удалять)
 * Внешне это выглядит как две таблицы. Которое будут в HorizontalLayout
 * Из-за убогости TwinColumnsSelect пришлось делать собственный мини компонент
 * Created by Роман on 11.06.2016.
 */
public class TwinTablesSelect<T extends SelectableInfo> extends CustomComponent {
    private Table optionsTable = new Table();
    private Table selectionsTable = new Table();
    private Label caption = new Label();
    private HorizontalLayout twoTables = new HorizontalLayout(optionsTable, selectionsTable);
    private VerticalLayout layout = new VerticalLayout(caption, twoTables);

    private BeanItemContainer<T> options;
    private BeanItemContainer<T> selections;
    private final Consumer<List<T>> saveToDbSelected;
    // Возможные действия для работы над элементами
    private static final Action ADD_TO_SELECTIONS = new Action("Добавить");
    private static final Action REMOVE_FROM_SELECTIONS = new Action("Убрать");

    /**
     * @param options          контейнер
     * @param selections       контейнер
     * @param saveToDbSelected Consumer делегирующий логику сохранения в базу данных
     */
    public TwinTablesSelect(Class<T> t, List<T> options, List<T> selections, Consumer<List<T>> saveToDbSelected) {
        this.options = new BeanItemContainer<>(t,options);
        this.selections = new BeanItemContainer<>(t,selections);
        this.saveToDbSelected = saveToDbSelected;
        setSizeFull();
        layout.setSizeFull();
        twoTables.setSizeFull();
        configureTable(optionsTable, "Доступно", this.options);
        configureTable(selectionsTable, "Выбрано", this.selections);
        setCompositionRoot(twoTables);
    }

    private void configureTable(Table table, String header, BeanItemContainer<T> container) {
        table.setContainerDataSource(container);
        table.setVisibleColumns("rusName");
        table.setColumnHeader("rusName", header);
        table.setMultiSelect(true);
        table.setDragMode(Table.TableDragMode.MULTIROW);
        table.setSelectable(true);
        table.setEditable(false);
        table.addActionHandler(handler);
        table.setDropHandler(dropHandler);
        table.setSizeFull();
    }

    public void refreshData(List<T> data) {
        options.removeAllItems();
        selections.removeAllItems();
        options.addAll(data);
        // Автоматически переставит всё куда надо
        discardChanges();
    }

    /**
     * В случае, если нажали кнопку Принять, подтверждаем все изменения
     * (B выставляем флаги у критериев)
     */
    public void acceptChanges() {
        selections.getItemIds().forEach(t -> t.setSelected(true));
        options.getItemIds().forEach(t -> t.setSelected(false));
        // Неочевидно обновляем текстовое отображение item
        selectionsTable.markAsDirtyRecursive();
        saveToDbSelected.accept(getOrderedSelections());
    }

    /**
     * В случае, если нажали кнопку Отмена, то возвращемся в исходное состояние
     * По сути перетаскивает компоненты так,
     */
    public void discardChanges() {
        checkFlagsAndColumns(selections, options, t -> !t.isSelected());
        checkFlagsAndColumns(options, selections, T::isSelected);
    }

    /**
     * Небольшие сложности с ConcurrentModificationException и UnsupportedException толкнули
     * к такому решению. Позже разберусь как лучше.
     *
     * @param whereToRemove контейнер, по которому необходимо пройтись и удалить элементы,
     *                      не соответствующие флагам
     * @param whereToAdd    контейнер, куда необходимо вернуть не соответствующие флагам
     *                      элементы
     */
    private void checkFlagsAndColumns(BeanItemContainer<T> whereToRemove, BeanItemContainer<T> whereToAdd, Predicate<T> predicate) {
        Iterator<T> iterator = whereToRemove.getItemIds().iterator();
        ArrayList<T> removeList = new ArrayList<>();
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.test(t)) {
                removeList.add(t);
                whereToAdd.addBean(t);
            }
        }
        removeList.forEach(whereToRemove::removeItem);
    }

    public void addSelectionsValueChangeListener(Property.ValueChangeListener valueChangeEvent) {
        selectionsTable.addValueChangeListener(valueChangeEvent);
    }

    public void setCaption(String text) {
        caption.setValue(text);
    }

    public void setCaptionAlignment(Alignment alignment) {
        layout.setComponentAlignment(caption, alignment);
    }

    public List<T> getOrderedSelections() {
        return selections.getItemIds();
    }

    private final Action.Handler handler = new Action.Handler() {
        @Override
        public Action[] getActions(Object target, Object sender) {
            if (sender == optionsTable) {
                return new Action[]{ADD_TO_SELECTIONS};
            } else if (sender == selectionsTable) {
                return new Action[]{REMOVE_FROM_SELECTIONS};
            } else {
                throw new IllegalArgumentException("Выбрана ни одна из таблиц");
            }
        }

        @Override
        public void handleAction(Action action, Object sender, Object target) {
            Table from = (Table) sender;
            Table to = from == optionsTable ? selectionsTable : optionsTable;
            Collection selectedValues = (Collection) from.getValue();
            if (action == ADD_TO_SELECTIONS || action == REMOVE_FROM_SELECTIONS) {
                if (selectedValues.contains(target)) {
                    for (Object itemId : selectedValues) {
                        from.removeItem(itemId);
                        to.addItem(itemId);
                    }
                } else {
                    from.removeItem(target);
                    to.addItem(target);
                }
            } else {
                throw new IllegalArgumentException("Неизвестное действие");
            }
        }
    };
    private final DropHandler dropHandler = new DropHandler() {
        /**
         * Очень непростая логика, в первую очередь определяемая кривостью Vaaadin
         1) Определяем то, что мы собираемся перекинуть из таблицы в таблицу
         Либо это один объект, который не выбран в таблице. Либо целая группа.
         2) Определяем таблицу, куда перекинем данные и откуда их удалим
         3) Удаляем/добавляем (в соответствии с позицией дропа)
         * @param dragAndDropEvent s
         */
        @Override
        public void drop(DragAndDropEvent dragAndDropEvent) {
            Collection values = (Collection) ((Table) dragAndDropEvent.getTransferable().getSourceComponent()).getValue();
            DataBoundTransferable transferable = (DataBoundTransferable) dragAndDropEvent.getTransferable();
            ArrayList dropItems = new ArrayList();
            /*
            Если мы выбрали группу элементов, и тот элемент, который мы тянем, содержится в
            этой группе, значит мы тянем всю группу, в противном случае один элемент
             */
            if (values.contains(transferable.getItemId())) {
                dropItems.addAll(values);
            } else {
                dropItems.add(transferable.getItemId());
            }
            AbstractSelect.AbstractSelectTargetDetails target =
                    (AbstractSelect.AbstractSelectTargetDetails) dragAndDropEvent.getTargetDetails();
            // Запрещаем вставку элемента на самого себя.
            if (target.getItemIdOver() != null && dropItems.contains(target.getItemIdOver())) {
                return;
            }
            // Определяем откуда и куда вешаем элементы (При этом перемешения могут быть внутри одной таблицы)
            Table from = (Table) transferable.getSourceComponent();
            Table to = (Table) target.getTarget();
            BeanItemContainer fromContainer = (BeanItemContainer) from.getContainerDataSource();
            BeanItemContainer toContainer = (BeanItemContainer) to.getContainerDataSource();
            Object previsionsItemId = target.getItemIdOver();
            // Если пустой или добавляем в таблицу, не указав конкретно куда, то просто всё добавить
            if (toContainer.size() == 0 || previsionsItemId == null) {
                dropItems.forEach(fromContainer::removeItem);
                toContainer.addAll(dropItems);
                return;
            }
            // Cначала удаляем (чтобы не было конфликтов)
            dropItems.forEach(fromContainer::removeItem);
            // А вот теперь уже вставляем куда надо
            for (Object itemId : dropItems) {
                if (target.getDropLocation() == VerticalDropLocation.TOP) {
                    toContainer.addItemAt(toContainer.indexOfId(previsionsItemId), itemId);
                } else if (target.getDropLocation() == VerticalDropLocation.BOTTOM) {
                    toContainer.addItemAfter(previsionsItemId, itemId);
                    previsionsItemId = itemId;
                }
            }
        }

        @Override
        public AcceptCriterion getAcceptCriterion() {
            return new And(new Or(new SourceIs(optionsTable), new SourceIs(selectionsTable)),
                    new Not(AbstractSelect.VerticalLocationIs.MIDDLE));
        }
    };
}