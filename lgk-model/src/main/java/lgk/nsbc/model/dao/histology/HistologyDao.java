package lgk.nsbc.model.dao.histology;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.HistologyRecord;
import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Stud;
import lgk.nsbc.model.histology.Histology;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static lgk.nsbc.generated.tables.Histology.HISTOLOGY;
import static org.jooq.impl.DSL.val;

@Service
public class HistologyDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public List<Histology> findByPatient(Patients patients) {
        Result<HistologyRecord> result = context.fetch(HISTOLOGY, HISTOLOGY.PATIENTS_N.eq(patients.getN()));
        return result.stream()
                .map(Histology::buildFromRecord)
                .collect(Collectors.toList());
    }

    public List<Histology> findByPatientsId(List<Long> patientsId) {
        return context.fetch(HISTOLOGY, HISTOLOGY.PATIENTS_N.in(patientsId))
                .stream()
                .map(Histology::buildFromRecord)
                .collect(Collectors.toList());
    }

    public List<Histology> findByStudy(Stud stud) {
        Result<HistologyRecord> result = context.fetch(HISTOLOGY, HISTOLOGY.STUD_N.eq(stud.getN()));
        return result.stream()
                .map(Histology::buildFromRecord)
                .collect(Collectors.toList());
    }

    public void deleteHistology(Histology histology) {
        context.deleteFrom(HISTOLOGY)
                .where(HISTOLOGY.N.eq(histology.getN()))
                .execute();
    }

    public void saveHistology(Histology histology) {
        HistologyRecord value = context.insertInto(HISTOLOGY)
                .columns(HISTOLOGY.N,
                        HISTOLOGY.OP_CREATE,
                        HISTOLOGY.STUD_N,
                        HISTOLOGY.KI67_FROM,
                        HISTOLOGY.KI67_TO,
                        HISTOLOGY.COMMENTARY,
                        HISTOLOGY.HIST_VERIF_BURD,
                        HISTOLOGY.PATIENTS_N,
                        HISTOLOGY.TARGET_N,
                        HISTOLOGY.CANCER_HISTOLOGY_CON,
                        HISTOLOGY.PG,
                        HISTOLOGY.PG_B,
                        HISTOLOGY.PATIENTS_DIAGNOSIS_N,
                        HISTOLOGY.ER,
                        HISTOLOGY.ER_B,
                        HISTOLOGY.FISH,
                        HISTOLOGY.HER2NEU,
                        HISTOLOGY.ONCO_DIAG_CON,
                        HISTOLOGY.DIAG_VERIF)
                .values(
                        Sequences.HISTOLOGY_N.nextval(),
                        Sequences.SYS_OPERATION_N.nextval(),
                        val(histology.getStudN()),
                        val(histology.getKi67From()),
                        val(histology.getKi67To()),
                        val(histology.getCommentary()),
                        val(histology.getHistVerifBurd()),
                        val(histology.getPatientsN()),
                        val(histology.getTargetN()),
                        val(histology.getCancerHistologyCon()),
                        val(histology.getPg()),
                        val(histology.getPgB()),
                        val(histology.getPatientsDiagnosisN()),
                        val(histology.getEr()),
                        val(histology.getErB()),
                        val(histology.getFish()),
                        val(histology.getHer2neu()),
                        val(histology.getOncoDiagCon()),
                        val(histology.getDiagVerif())
                )
                .returning(HISTOLOGY.N)
                .fetchOne();
        histology.setN(value.getN());
    }

    public void updateHistology(Histology histology) {
        // Обновляем так, чтобы не убить что то, заданное в основном интерфейсе
        context.update(HISTOLOGY)
                .set(HISTOLOGY.STUD_N, histology.getStudN())
                .set(HISTOLOGY.COMMENTARY, histology.getCommentary())
                .set(HISTOLOGY.KI67_FROM, histology.getKi67From())
                .set(HISTOLOGY.KI67_TO, histology.getKi67To())
                .where(HISTOLOGY.N.eq(histology.getN()))
                .execute();
    }

    public boolean isExist(Long n) {
        return context.fetchExists(HISTOLOGY, HISTOLOGY.N.eq(n));
    }
}
