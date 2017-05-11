package lgk.nsbc.histology;

import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Stud;
import lgk.nsbc.model.dao.PatientsDao;
import lgk.nsbc.model.dao.StudDao;
import lgk.nsbc.model.dao.dictionary.DicYesNoDao;
import lgk.nsbc.model.dao.dictionary.GenesDao;
import lgk.nsbc.model.dao.dictionary.MutationTypesDao;
import lgk.nsbc.model.dao.histology.HistologyDao;
import lgk.nsbc.model.dao.histology.MutationsDao;
import lgk.nsbc.model.histology.Histology;
import lgk.nsbc.model.histology.Mutation;
import lgk.nsbc.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class HistologyManager {
    @Autowired
    private PatientsDao patientsDao;
    @Autowired
    private StudDao studDao;
    @Autowired
    private GenesDao genesDao;
    @Autowired
    private DicYesNoDao dicYesNoDao;
    @Autowired
    private MutationTypesDao mutationTypesDao;
    @Autowired
    private HistologyDao histologyDao;
    @Autowired
    private MutationsDao mutationsDao;

    public List<HistologyBind> getHistology(Patients patients) {
        List<Histology> histologyList = histologyDao.findByPatient(patients);
        Map<Long, List<Mutation>> mutationsMap = mutationsDao.findMutationsByHistologyList(histologyList);
        Map<Long, List<MutationBind>> mutationsBindMap = mutationsMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, list -> list.getValue()
                        .stream()
                        .map(this::toMutationBind)
                        .collect(toList()))
                );
        List<Long> studId = histologyList.stream()
                .map(Histology::getNbcStudN)
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
            histologyBind.setMutations(mutationsMap.get(histology.getN()));
            histologyBind.setMutationBinds(mutationsBindMap.get(histology.getN()));
            Stud stud = studMap.get(histology.getNbcStudN());
            histologyBind.setStud(stud);
            if (stud != null && stud.getStudydatetime() != null)
                histologyBind.setHistologyDate(DateUtils.asLocalDate(stud.getStudydatetime()));
        });
        return histologyBindList;
    }

    private MutationBind toMutationBind(Mutation mutation) {
        return MutationBind.builder()
                .genes(genesDao.getGenes().get(mutation.getGeneN()))
                .dicYesNo(dicYesNoDao.getDicYesNo().get(mutation.getYesNoN()))
                .mutationTypes(mutationTypesDao.getMutationTypes().get(mutation.getMutationTypeN()))
                .build();
    }

    private HistologyBind toHistologyBind(Histology histology) {
        return HistologyBind.builder()
                .histology(histology)
                .burdenkoVerification(histology.getHistVerifBurd() == null ? null : histology.getHistVerifBurd().equals("Y"))
                .ki67From(histology.getKi67From())
                .ki67To(histology.getKi67To())
                .comment(histology.getCommentary())
                .build();
    }

    public void saveHistology() {

    }
}
