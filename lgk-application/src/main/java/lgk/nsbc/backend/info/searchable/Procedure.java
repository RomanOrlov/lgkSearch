package lgk.nsbc.backend.info.searchable;

import lgk.nsbc.backend.info.DisplayedInfo;
import lgk.nsbc.backend.info.ViewableInfo;
import lgk.nsbc.backend.info.criteria.Criteria;
import lgk.nsbc.generated.tables.NbcProc;
import lgk.nsbc.generated.tables.records.NbcProcRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Component
public class Procedure extends SearchTarget<NbcProcRecord> {

    public Procedure() {
        super("Процедура", NbcProc.NBC_PROC, NbcProc.NBC_PROC.N);
    }

    @Override
    public List<Criteria> getCriteriaList() {
        return Collections.emptyList();
    }

    @Override
    public List<DisplayedInfo> getDisplayedInfoList() {
        return Collections.emptyList();
    }

    public interface ProcedureInfo extends ViewableInfo {
    }
}
