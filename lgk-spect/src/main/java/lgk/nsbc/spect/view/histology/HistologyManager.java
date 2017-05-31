package lgk.nsbc.spect.view.histology;

import lgk.nsbc.model.DateUtils;
import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Proc;
import lgk.nsbc.model.Stud;
import lgk.nsbc.model.dao.ProcDao;
import lgk.nsbc.model.dao.StudDao;
import lgk.nsbc.model.dao.histology.HistologyDao;
import lgk.nsbc.model.dao.histology.MutationsDao;
import lgk.nsbc.model.histology.Histology;
import lgk.nsbc.model.histology.Mutation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static lgk.nsbc.model.dao.dictionary.StudTypeDao.getStudTypeMap;

@Service
public class HistologyManager {
    @Autowired
    private StudDao studDao;
    @Autowired
    private HistologyDao histologyDao;
    @Autowired
    private MutationsDao mutationsDao;
    @Autowired
    private ProcDao procDao;

    public List<HistologyBind> getHistology(Patients patients) {
        List<Histology> histologyList = histologyDao.findByPatient(patients);
        Map<Long, List<Mutation>> mutationsMap = mutationsDao.findMutationsByHistologyList(histologyList);
        List<Long> studId = histologyList.stream()
                .map(Histology::getStudN)
                .filter(Objects::nonNull)
                .collect(toList());
        Map<Long, Stud> studMap = studDao.findById(studId)
                .stream()
                .collect(toMap(Stud::getN, identity()));
        List<HistologyBind> histologyBindList = histologyList.stream()
                .map(this::toHistologyBind)
                .collect(toList());
        histologyBindList.forEach(histologyBind -> {
            Histology histology = histologyBind.getHistology();
            Set<Mutation> mutationsTemplate = HistologyBind.getMutationsTemplate();
            List<Mutation> mutations = mutationsMap.get(histology.getN());
            if (mutations != null) {
                mutationsTemplate.removeAll(mutations);
                mutationsTemplate.addAll(mutations);
            }
            histologyBind.setMutations(mutationsTemplate);
            Stud stud = studMap.get(histology.getStudN());
            histologyBind.setStud(stud);
            if (stud != null && stud.getStudyDateTime() != null)
                histologyBind.setHistologyDate(DateUtils.asLocalDate(stud.getStudyDateTime()));
        });
        return histologyBindList;
    }

    private HistologyBind toHistologyBind(Histology histology) {
        return HistologyBind.builder()
                .histology(histology)
                .burdenkoVerification(HistologyBind.getBooleanBurdenkoVerification(histology.getHistVerifBurd()))
                .ki67From(histology.getKi67From())
                .ki67To(histology.getKi67To())
                .comment(histology.getCommentary())
                .build();
    }

    private Histology toHistology(HistologyBind histologyBind, Stud stud) {
        return Histology.builder()
                .studN(stud.getN())
                .histVerifBurd(HistologyBind.getStringBurdenkoVerification(histologyBind.getBurdenkoVerification()))
                .ki67From(histologyBind.getKi67From())
                .ki67To(histologyBind.getKi67To())
                .commentary(histologyBind.getComment())
                .build();
    }

    public void deleteHistology(HistologyBind value) {
        Histology histology = value.getHistology();
        if (histology == null) return;
        if (value.getStud() != null)
            studDao.deleteStudy(value.getStud());
        if (histology.getN() != null) {
            histologyDao.deleteHistology(histology);
            mutationsDao.deleteMutationsByHistology(histology);
        }
    }

    public void updateHistology(HistologyBind histologyBind, Patients patients) {
        Stud stud = getStud(histologyBind, patients);
        histologyBind.setStud(stud);
        Histology histology = toHistology(histologyBind, stud);
        histology.setN(histologyBind.getHistology().getN());
        histologyDao.updateHistology(histology);
        mutationsDao.deleteMutationsByHistology(histologyBind.getHistology());
        saveMutations(histologyBind, stud, histologyBind.getHistology());
    }

    public void createNewHistology(HistologyBind histologyBind, Patients patients) {
        Stud stud = getStud(histologyBind, patients);
        histologyBind.setStud(stud);
        Histology histology = toHistology(histologyBind, stud);
        histology.setPatientsN(patients.getN());
        histologyBind.setHistology(histology);
        histologyDao.saveHistology(histology);
        saveMutations(histologyBind, stud, histology);
    }

    private void saveMutations(HistologyBind histologyBind, Stud stud, Histology histology) {
        Set<Mutation> mutations = histologyBind.getMutations()
                .stream()
                .filter(mutation -> mutation.getMutationType() != null && mutation.getDicYesNo() != null)
                .peek(mutation -> mutation.setStudyN(stud.getN()))
                .peek(mutation -> mutation.setHistologyN(histology.getN()))
                .sorted()
                .collect(toSet());
        mutationsDao.saveMutations(mutations);
    }

    private Stud getStud(HistologyBind histologyBind, Patients patients) {
        Stud stud = Stud.builder()
                .patientsN(patients.getN())
                .studType(getStudTypeMap().get(8L))
                .studyDateTime(DateUtils.asDate(histologyBind.getHistologyDate()))
                .build();
        if (histologyBind.getStud() != null && histologyBind.getStud().getN() != null) {
            stud.setN(histologyBind.getStud().getN());
            studDao.updateStudy(stud);
            return stud;
        }
        if (histologyBind.getHistologyDate() != null) {
            // Пытаемся найти stud
            Optional<Stud> studyByDate = studDao.findStudyByDate(patients, DateUtils.asDate(histologyBind.getHistologyDate()), 8L);
            if (studyByDate.isPresent())
                return studyByDate.get();
        }
        studDao.createStud(stud);
        return stud;
    }

    public List<Stud> getPatientStudy(Patients patients) {
        return studDao.findPatientsStuds(patients);
    }

    public boolean checkHistologyExist(Long n) {
        return histologyDao.isExist(n);
    }

    public Collection<? extends Proc> getPatientsProc(Patients patients) {
        return procDao.findPatientProc(patients);
    }
}
