package uz.pdp.library.DAO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import uz.pdp.library.entity.Auditable;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class BaseDao<T extends Auditable, ID extends Serializable> {
    protected static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("default");
    protected static final EntityManager em = emf.createEntityManager();
    private final Class<T> clazz;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();


    @SuppressWarnings("unchecked")
    protected BaseDao() {
        this.clazz = (Class<T>) (((ParameterizedType)
                getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public T update(T entity) {
        begin();
        em.merge(entity);
        commit();
        return entity;
    }

    public T create(T entity) {
        begin();
        em.persist(entity);
        commit();
        return entity;
    }

    public T delete(T entity) {
        begin();
        em.remove(entity);
        commit();
        return entity;
    }

    public boolean deleteById(ID id) {
        begin();
        em.createQuery("delete from " + clazz.getSimpleName()
                        + " t where t.id = :id")
                .setParameter("id", id)
                .executeUpdate();
        commit();
        return true;
    }

    protected void begin() {
        em.getTransaction().begin();
    }

    protected void commit() {
        em.getTransaction().commit();
    }


    public T findById(ID id) {
        T entity = em.find(clazz, id);
        return entity;
    }

    public List<T> findAll() {
        TypedQuery<T> query = em.createQuery("from " + clazz.getSimpleName(), clazz);
        return query.getResultList();
    }
}
