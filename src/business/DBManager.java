package business;

import javax.persistence.*;
import java.io.File;
import java.util.List;

public class DBManager {

    private static EntityManager em;

    private static EntityManagerFactory factory;

    public static boolean createDbDirectory(String dbLocation) {
        File dbFolder = new File(dbLocation);
        return dbFolder.exists() || dbFolder.mkdirs();
    }

    public static boolean init(String persistence_name){
        try {
            factory = Persistence.createEntityManagerFactory(persistence_name);
            em = factory.createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void begin(){
        em.getTransaction().begin();
    }

    public static void commit(){
        em.getTransaction().commit();
    }

    public static <T> T save(T o){
        begin();
        em.persist(o);
        commit();

        return o;

    }

    public static <T> T save(Class<T> type, T o){
        begin();
        em.persist(o);
        commit();

        return o;
    }

    public static <T> T merge(T o) {
        begin();
        o = em.merge(o);
        commit();

        return o;
    }

    public static void delete(Object o){
        begin();
        em.remove(o);
        commit();
    }

    public static <T> T find(Class<T> type, Object id){
        return em.find(type, id);
    }

    public static List<Object[]> query(String query, Object... params){
        TypedQuery<Object[]> typedQuery = em.createQuery(query, Object[].class);
        for (int i = 0; i < params.length; i++) typedQuery.setParameter(i + 1, params[i]);

        return typedQuery.getResultList();
    }

    public static <T> List<T> fromTable(Class<T> tClass) {
        return listAll(tClass);
    }

    public static <T> List<T> fromOrderedTable(Class<T> tClass, String orderBy) {
        String query = "select t from " + tClass.getSimpleName() + " t order by " + orderBy;
        return query(tClass, query);
    }

    public static <T> List<T> fromTable(Class<T> tClass, String where, Object... params) {
        String query = "select t from " + tClass.getSimpleName() + " t where " + where;
        return query(tClass, query, params);
    }

    public static <T> List<T> fromTable(Class<T> tClass, String where, String orderBy, Object... params) {
        String query = "select t from " + tClass.getSimpleName() + " t where " + where + " order by " + orderBy;
        return query(tClass, query, params);
    }

    public static <T> List<T> query(Class<T> type, String query, Object... params){
        TypedQuery<T> typedQuery = em.createQuery(query, type);
        for (int i = 0; i < params.length; i++) typedQuery.setParameter(i + 1, params[i]);

        return typedQuery.getResultList();
    }

    public static <T> List<T> listAll(Class<T> type) {
        String name = type.getSimpleName();
        TypedQuery<T> typedQuery = em.createQuery("select e from " + name + " e", type);

        return typedQuery.getResultList();
    }

    public static <T> T queryForSingleResult(Class<T> type, String query, Object... params) {
        TypedQuery<T> typedQuery = em.createQuery(query, type);
        for (int i = 0; i < params.length; i++) typedQuery.setParameter(i + 1, params[i]);

        return typedQuery.getSingleResult();
    }

    public static long update(String query, Object... params){
        begin();
        Query q = em.createQuery(query);
        for (int i = 0; i < params.length; i++) q.setParameter(i + 1, params[i]);
        long i = q.executeUpdate();
        commit();

        return i;
    }

    public static boolean close(){
        try {
            em.close();
            factory.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
