package lgk.nsbc.backend.info.searchable;

import lgk.nsbc.backend.info.DisplayedInfo;
import lgk.nsbc.backend.info.ViewableInfo;
import lgk.nsbc.backend.info.criteria.Criteria;
import lgk.nsbc.generated.tables.NbcTarget;
import lgk.nsbc.generated.tables.records.NbcTargetRecord;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class Target extends SearchTarget<NbcTargetRecord> {
    public Target() {
        super("Мишень", NbcTarget.NBC_TARGET, NbcTarget.NBC_TARGET.N);
    }

    @Override
    public List<Criteria> getCriteriaList() {
        return Collections.emptyList();
    }

    @Override
    public List<DisplayedInfo> getDisplayedInfoList() {
        return Collections.emptyList();
    }

    public interface TargetInfo extends ViewableInfo {
    }
}
