package lgk.nsbc.model.dao.dvh;

import lgk.nsbc.model.dvh.DvhLines;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static lgk.nsbc.generated.tables.DvhLines.DVH_LINES;

@Service
public class DvhLinesDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public List<DvhLines> findDvhLines(Long dvhId) {
        if (dvhId == null) return Collections.emptyList();
        return context.fetch(DVH_LINES, DVH_LINES.DVH_N.eq(dvhId))
                .stream()
                .map(DvhLines::buildFromRecord)
                .collect(Collectors.toList());
    }

    public List<DvhLines> findDvhLines(List<Long> dvhId) {
        return context.fetch(DVH_LINES, DVH_LINES.DVH_N.in(dvhId))
                .stream()
                .map(DvhLines::buildFromRecord)
                .collect(Collectors.toList());
    }
}
