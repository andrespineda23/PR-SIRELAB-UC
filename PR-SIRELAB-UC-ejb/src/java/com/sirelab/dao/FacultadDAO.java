package com.sirelab.dao;

import com.sirelab.dao.interfacedao.FacultadDAOInterface;
import com.sirelab.entidades.Facultad;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author ANDRES PINEDA
 */
@Stateless
public class FacultadDAO implements FacultadDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearFacultad(Facultad facultad) {
        try {
            em.persist(facultad);
        } catch (Exception e) {
            System.out.println("Error crearFacultad FacultadDAO : " + e.toString());
        }
    }

    @Override
    public void editarFacultad(Facultad facultad) {
        try {
            em.merge(facultad);
        } catch (Exception e) {
            System.out.println("Error editarFacultad FacultadDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarFacultad(Facultad facultad) {
        try {
            em.remove(em.merge(facultad));
        } catch (Exception e) {
            System.out.println("Error eliminarFacultad FacultadDAO : " + e.toString());
        }
    }

    @Override
    public List<Facultad> consultarFacultades() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Facultad p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Facultad> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarFacultades FacultadDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public Facultad buscarFacultadPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Facultad p WHERE p.idfacultad=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            Facultad registro = (Facultad) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarFacultadPorID FacultadDAO : " + e.toString());
            return null;
        }
    }
    
    
    @Override
    public List<Facultad> buscarFacultadesPorFiltrado(Map<String, String> filters) {
        try {
            final String alias = "a";
            final StringBuilder jpql = new StringBuilder();
            String jpql2;
            jpql.append("SELECT a FROM ").append(Facultad.class.getSimpleName()).append(" " + alias);
            //
            jpql2 = adicionarFiltros(jpql.toString(), filters, alias);
            //
            System.out.println("jpql2.toString() : " + jpql2.toString());
            TypedQuery<Facultad> tq = em.createQuery(jpql2.toString(), Facultad.class);
            tq = asignarValores(tq, filters);
            return tq.getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarFacultadesPorFiltrado FacultadDAO : " + e.toString());
            return null;
        }
    }

    private String adicionarFiltros(String jpql, Map<String, String> filters, String alias) {
        final StringBuilder wheres = new StringBuilder();
        int camposFiltro = 0;
        if (null != filters && !filters.isEmpty()) {
            wheres.append(" WHERE ");
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                if (null != entry.getValue() && !entry.getValue().isEmpty()) {
                    if (camposFiltro > 0) {
                        wheres.append(" AND ");
                    }
                    if ("parametroNombre".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".nombrefacultad")
                                .append(") Like :parametroNombre");
                        camposFiltro++;
                    }
                    if ("parametroCodigo".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".codigofacultad")
                                .append(") Like :parametroCodigo");
                        camposFiltro++;
                    }
                }
            }
        }
        jpql = jpql + wheres /*+ " ORDER BY " + alias + ".id ASC"*/;

        System.out.println(jpql);

        if (jpql.trim()
                .endsWith("WHERE")) {
            jpql = jpql.replace("WHERE", "");
        }
        return jpql;
    }

    private TypedQuery<Facultad> asignarValores(TypedQuery<Facultad> tq, Map<String, String> filters) {

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (null != entry.getValue() && !entry.getValue().isEmpty()) {
                if (("parametroNombre".equals(entry.getKey()))
                        || ("parametroCodigo".equals(entry.getKey()))) {
                    //
                    tq.setParameter(entry.getKey(), "%" + entry.getValue().toUpperCase() + "%");
                }
            }
        }
        return tq;
    }

}
