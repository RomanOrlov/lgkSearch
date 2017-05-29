package lgk.nsbc.util.components;

import com.vaadin.data.HasValue;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.SerializableBiFunction;
import com.vaadin.server.SerializableFunction;
import com.vaadin.server.SerializablePredicate;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.function.BiFunction;
import java.util.function.Predicate;

@AllArgsConstructor
@Builder
public class FilterChain<T, V, U> {
    private final HasValue<T> hasValue;
    private final Predicate<T> hasValueFilter;
    private final SerializableFunction<T, V> converter;
    private final ValueProvider<U, V> valueProvider;
    private final BiFunction<V, V, Boolean> filter;


    public boolean test(U u) {
        T value = hasValue.getValue();
        if (!hasValueFilter.test(value))
            return true;
        V hasValueConverted = converter.apply(value);
        V providerValue = valueProvider.apply(u);
        return filter.apply(hasValueConverted, providerValue);
    }
}
