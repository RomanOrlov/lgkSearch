package lgk.nsbc.model.dao.histology;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.NbcHistology_1Record;
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

import static lgk.nsbc.generated.tables.NbcHistology_1.NBC_HISTOLOGY_1;
import static org.jooq.impl.DSL.val;

@Service
public class HistologyDao implements Serializable {
    @Autowired
    private DSLContext context;

    public List<Histology> findByPatient(Patients patients) {
        Result<NbcHistology_1Record> result = context.fetch(NBC_HISTOLOGY_1, NBC_HISTOLOGY_1.NBC_PATIENTS_N.eq(patients.getN()));
        return result.stream()
                .map(Histology::buildFromRecord)
                .collect(Collectors.toList());
    }

    public List<Histology> findByStudy(Stud stud) {
        Result<NbcHistology_1Record> result = context.fetch(NBC_HISTOLOGY_1, NBC_HISTOLOGY_1.NBC_STUD_N.eq(stud.getN()));
        return result.stream()
                .map(Histology::buildFromRecord)
                .collect(Collectors.toList());
    }

    public void deleteHistology(Histology histology) {
        context.deleteFrom(NBC_HISTOLOGY_1)
                .where(NBC_HISTOLOGY_1.N.eq(histology.getN()))
                .execute();
    }

    public void saveHistology(Histology histology) {
        NbcHistology_1Record value = context.insertInto(NBC_HISTOLOGY_1)
                .columns(NBC_HISTOLOGY_1.N,
                        NBC_HISTOLOGY_1.OP_CREATE,
                        NBC_HISTOLOGY_1.NBC_STUD_N,
                        NBC_HISTOLOGY_1.KI67_FROM,
                        NBC_HISTOLOGY_1.KI67_TO,
                        NBC_HISTOLOGY_1.COMMENTARY,
                        NBC_HISTOLOGY_1.HIST_VERIF_BURD,
                        NBC_HISTOLOGY_1.NBC_PATIENTS_N,
                        NBC_HISTOLOGY_1.NBC_TARGET_N,
                        NBC_HISTOLOGY_1.CANCER_HISTOLOGY_1_CON,
                        NBC_HISTOLOGY_1.PG,
                        NBC_HISTOLOGY_1.PG_B,
                        NBC_HISTOLOGY_1.NBC_PATIENTS_DIAGNOSIS_N,
                        NBC_HISTOLOGY_1.ER,
                        NBC_HISTOLOGY_1.ER_B,
                        NBC_HISTOLOGY_1.FISH,
                        NBC_HISTOLOGY_1.HER2NEU,
                        NBC_HISTOLOGY_1.ONCO_DIAG_1_CON,
                        NBC_HISTOLOGY_1.DIAG_VERIF)
                .values(
                        Sequences.NBC_HISTOLOGY_1_N.nextval(),
                        Sequences.SYS_OPERATION_N.nextval(),
                        val(histology.getNbcStudN()),
                        val(histology.getKi67From()),
                        val(histology.getKi67To()),
                        val(histology.getCommentary()),
                        val(histology.getHistVerifBurd()),
                        val(histology.getNbcPatientsN()),
                        val(histology.getNbcTargetN()),
                        val(histology.getCancerHistologyCon()),
                        val(histology.getPg()),
                        val(histology.getPgB()),
                        val(histology.getNbcPatientsDiagnosisN()),
                        val(histology.getEr()),
                        val(histology.getErB()),
                        val(histology.getFish()),
                        val(histology.getHer2neu()),
                        val(histology.getOncoDiagCon()),
                        val(histology.getDiagVerif())
                )
                .returning(NBC_HISTOLOGY_1.N)
                .fetchOne();
        histology.setN(value.getN());
    }

    public void updateHistology(Histology histology) {
        // Обновляем так, чтобы не убить что то, заданное в основном интерфейсе
        context.update(NBC_HISTOLOGY_1)
                .set(NBC_HISTOLOGY_1.NBC_STUD_N, histology.getNbcStudN())
                .set(NBC_HISTOLOGY_1.COMMENTARY, histology.getCommentary())
                .set(NBC_HISTOLOGY_1.KI67_FROM, histology.getKi67From())
                .set(NBC_HISTOLOGY_1.KI67_TO, histology.getKi67To())
                .where(NBC_HISTOLOGY_1.N.eq(histology.getN()))
                .execute();
    }

    public boolean isExist(Long n) {
        return context.fetchExists(NBC_HISTOLOGY_1, NBC_HISTOLOGY_1.N.eq(n));
    }
}
