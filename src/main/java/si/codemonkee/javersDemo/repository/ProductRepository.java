package si.codemonkee.javersDemo.repository;

import org.javers.spring.annotation.JaversAuditable;
import org.springframework.data.repository.CrudRepository;
import si.codemonkee.javersDemo.model.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    @Override
    @JaversAuditable
    <S extends Product> S save(S s);
}
