package lgk.nsbc.dao;

import lgk.nsbc.backend.entity.NbcPatients;
import lgk.nsbc.backend.entity.target.NbcTarget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface NbcTargetRepository extends CrudRepository<NbcTarget, Integer> {
    Long countByNbcPatients(NbcPatients nbcPatients);
}
