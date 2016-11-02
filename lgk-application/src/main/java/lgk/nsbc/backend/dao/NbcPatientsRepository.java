package lgk.nsbc.backend.dao;

import lgk.nsbc.backend.entity.NbcPatients;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface NbcPatientsRepository extends CrudRepository<NbcPatients, Integer> {
}
