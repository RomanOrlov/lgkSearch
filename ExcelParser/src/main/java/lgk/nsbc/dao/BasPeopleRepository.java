package lgk.nsbc.dao;

import lgk.nsbc.backend.entity.BasPeople;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface BasPeopleRepository extends CrudRepository<BasPeople, Integer> {
    List<BasPeople> findBySurnameAndNameAndPatronymic(String surname, String name, String patronymic);
    List<BasPeople> findBySurnameIn(List<String> surname);
}