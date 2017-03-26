package lgk.nsbc.util;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.filter.UnsupportedFilterException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

public class SuggestionContainer<T> extends BeanItemContainer<T> {
    private final Function<String, List<T>> suggestionFilter;

    public SuggestionContainer(Function<String, List<T>> suggestionFilter, Class<T> tClass) throws IllegalArgumentException {
        super(tClass);
        this.suggestionFilter = suggestionFilter;
    }

    @Override
    protected void addFilter(Filter filter) throws UnsupportedFilterException {
        SuggestionFilter suggestionFilter = (SuggestionFilter) filter;
        filterItems(suggestionFilter.getFilterString());
    }

    private void filterItems(String filterString) {
        removeAllItems();
        if (filterString.length() < 3) return;
        List<T> countries = suggestionFilter.apply(filterString);
        addAll(countries);
    }

    public void setSelectedBean(T bean) {
        removeAllItems();
        addBean(bean);
    }

    @AllArgsConstructor
    public static class SuggestionFilter implements Container.Filter {
        @Getter
        private String filterString;

        @Override
        public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
            return false;
        }

        @Override
        public boolean appliesToProperty(Object propertyId) {
            return false;
        }
    }
}
