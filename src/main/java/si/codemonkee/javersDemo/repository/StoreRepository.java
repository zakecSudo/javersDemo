package si.codemonkee.javersDemo.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.CrudRepository;
import si.codemonkee.javersDemo.model.Store;

@JaversSpringDataAuditable
public interface StoreRepository extends CrudRepository<Store, Integer> {
}
