package lgk.nsbc.backend.dao;

import lgk.nsbc.backend.entity.SysAgents;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface SysAgentsRepository extends CrudRepository<SysAgents,Integer> {
    @EntityGraph(value = "allSysAgents",type = EntityGraph.EntityGraphType.FETCH)
    List<SysAgents> findAll();
}
