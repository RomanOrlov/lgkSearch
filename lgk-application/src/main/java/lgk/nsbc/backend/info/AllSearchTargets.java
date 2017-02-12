package lgk.nsbc.backend.info;

import lgk.nsbc.backend.info.searchable.SearchTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AllSearchTargets {
    @Autowired
    private SearchTarget[] searchTargets;
    private List<SearchTarget> searchTargetsList;

    public SearchTarget[] getSearchTargets() {
        return searchTargets;
    }

    @PostConstruct
    private void init() {
        searchTargetsList = new ArrayList<>(Arrays.asList(searchTargets));
    }

    public List<SearchTarget> getSearchTargetsAsList() {
        return searchTargetsList;
    }

    public SearchTarget getTargetByName(String name) {
        for (SearchTarget searchTarget : searchTargetsList)
            if (searchTarget.getRusName().equals(name))
                return searchTarget;
        throw new RuntimeException("SearchTarget with name " + name + " doesn't exists");
    }
}
