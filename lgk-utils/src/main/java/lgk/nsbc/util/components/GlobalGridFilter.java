package lgk.nsbc.util.components;

import java.util.ArrayList;
import java.util.List;

public class GlobalGridFilter<U> {
    private List<FilterChain<?, ?, U>> allFilters = new ArrayList<>();

    public void addFilter(FilterChain<?, ?, U> filterChain) {
        allFilters.add(filterChain);
    }

    public boolean test(U u) {
        for (FilterChain<?,?,U> chain : allFilters) {
            if (!chain.test(u))
                return false;
        }
        return true;
    }
}
