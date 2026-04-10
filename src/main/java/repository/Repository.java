package repository;

import java.util.List;

public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    void save(T entity);
    void delete(ID id);
}

