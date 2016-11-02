package lgk.nsbc.backend.dao;

import lgk.nsbc.backend.entity.SysSessions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SysSessionsRepository extends CrudRepository<SysSessions, Integer> {
    SysSessions findBySid(String sid);
}
