package lgk.nsbc.backend.info.searchable;

import lgk.nsbc.backend.info.DisplayedInfo;
import lgk.nsbc.backend.info.ViewableInfo;
import lgk.nsbc.backend.info.criteria.Criteria;
import lgk.nsbc.generated.tables.NbcFollowup;
import lgk.nsbc.generated.tables.records.NbcFollowupRecord;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class FollowUp extends SearchTarget<NbcFollowupRecord> {
    public FollowUp() {
        super("Контроль", NbcFollowup.NBC_FOLLOWUP, NbcFollowup.NBC_FOLLOWUP.N);
    }

    @Override
    public List<Criteria> getCriteriaList() {
        return Collections.emptyList();
    }

    @Override
    public List<DisplayedInfo> getDisplayedInfoList() {
        return Collections.emptyList();
    }

    public interface FollowUpInfo extends ViewableInfo {
    }
}
