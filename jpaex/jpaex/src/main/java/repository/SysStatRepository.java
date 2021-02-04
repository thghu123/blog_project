package repository;

import entity.SysStat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public interface SysStatRepository extends CrudRepository<SysStat,Long> {

}

