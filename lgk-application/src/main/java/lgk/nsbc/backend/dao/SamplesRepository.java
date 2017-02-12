package lgk.nsbc.backend.dao;

import lgk.nsbc.backend.entity.sample.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface SamplesRepository extends CrudRepository<Sample, Integer>, JpaRepository<Sample, Integer> {
    List<Sample> findByUserId(Integer userId);
}
