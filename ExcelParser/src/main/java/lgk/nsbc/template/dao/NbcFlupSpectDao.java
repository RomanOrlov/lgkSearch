package lgk.nsbc.template.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.NbcFlupSpectRecord;
import lgk.nsbc.template.model.NbcFlupSpect;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static lgk.nsbc.generated.tables.NbcFlupSpect.NBC_FLUP_SPECT;
import static org.jooq.impl.DSL.val;

@Service
public class NbcFlupSpectDao {
    @Autowired
    private DSLContext context;

    public void createSpectFollowUp(NbcFlupSpect nbcFlupSpect) {
        Result<NbcFlupSpectRecord> fetch = context.insertInto(NBC_FLUP_SPECT)
                .columns(
                        NBC_FLUP_SPECT.N,
                        NBC_FLUP_SPECT.NBC_FOLLOWUP_N,
                        NBC_FLUP_SPECT.SPECT_NUM,
                        NBC_FLUP_SPECT.DIAGNOSIS
                )
                .values(Sequences.NBC_FLUP_SPECT_N.nextval(),
                        val(nbcFlupSpect.getNbc_followup_n()),
                        val(nbcFlupSpect.getSpect_num()),
                        val(nbcFlupSpect.getDiagnosis())
                )
                .returning(NBC_FLUP_SPECT.N)
                .fetch();
        nbcFlupSpect.setN(fetch.get(0).getN());
    }
}
