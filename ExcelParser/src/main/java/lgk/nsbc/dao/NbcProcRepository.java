package lgk.nsbc.dao;

import lgk.nsbc.backend.entity.NbcPatients;
import lgk.nsbc.backend.entity.NbcProc;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface NbcProcRepository extends CrudRepository<NbcProc, Integer> {
    Long countByNbcPatients(NbcPatients nbcPatients);
}
