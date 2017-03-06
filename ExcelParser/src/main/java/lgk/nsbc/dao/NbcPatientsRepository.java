package lgk.nsbc.dao;

import lgk.nsbc.backend.entity.BasPeople;
import lgk.nsbc.backend.entity.NbcPatients;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NbcPatientsRepository extends CrudRepository<NbcPatients, Integer> {
    NbcPatients findByBasPeople(BasPeople basPeople);

    //@Query("SELECT p FROM NbcPatients p WHERE p.BAS_PEOPLE_N = :id" +
    //        "JOIN ON ")
    //NbcPatients findByPeopleId(@Param("id") Integer basPeopleId);
}
