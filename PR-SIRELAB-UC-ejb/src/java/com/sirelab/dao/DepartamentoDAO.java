package com.sirelab.dao;

import com.sirelab.dao.interfacedao.DepartamentoDAOInterface;
import com.sirelab.entidades.Departamento;
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
public class DepartamentoDAO implements DepartamentoDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearDepartamento(Departamento departamento) {
        try {
            em.persist(departamento);
        } catch (Exception e) {
            System.out.println("Error crearDepartamento DepartamentoDAO : " + e.toString());
        }
    }

    @Override
    public void editarDepartamento(Departamento departamento) {
        try {
            em.merge(departamento);
        } catch (Exception e) {
            System.out.println("Error editarDepartamento DepartamentoDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarDepartamento(Departamento departamento) {
        try {
            em.remove(em.merge(departamento));
        } catch (Exception e) {
            System.out.println("Error eliminarDepartamento DepartamentoDAO : " + e.toString());
        }
    }

    @Override
    public List<Departamento> consultarDepartamentos() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Departamento p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Departamento> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.err.println("Error consultarDepartamentos DepartamentoDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public Departamento buscarDepartamentoPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Departamento p WHERE p.iddepartamento=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            Departamento registro = (Departamento) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.err.println("Error buscarDepartamentoPorID DepartamentoDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Departamento> buscarDepartamentosPorIDFacultad(BigInteger idFacultad) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Departamento p WHERE p.facultad.idfacultad=:idFacultad");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idFacultad", idFacultad);
            List<Departamento> registro = query.getResultList();
            return registro;
        } catch (Exception e) {
            System.err.println("Error buscarDepartamentosPorIDFacultad DepartamentoDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Departamento> buscarDepartamentosPorFiltrado(Map<String, String> filters) {
        try {
            final String alias = "a";
            final StringBuilder jpql = new StringBuilder();
            String jpql2;
            jpql.append("SELECT a FROM ").append(Departamento.class.getSimpleName()).append(" " + alias);
            //
            jpql2 = adicionarFiltros(jpql.toString(), filters, alias);
            //
            System.out.println("jpql2.toString() : " + jpql2.toString());
            TypedQuery<Departamento> tq = em.createQuery(jpql2.toString(), Departamento.class);
            tq = asignarValores(tq, filters);
            return tq.getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarDepartamentosPorFiltrado DepartamentoDAO : " + e.toString());
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
                                .append(".direccion")
                                .append(") Like :nombredepartamento");
                        camposFiltro++;
                    }
                    if ("parametroFacultad".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "facultad.idfacultad");
                        wheres.append("= :").append(entry.getKey());
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

    private TypedQuery<Departamento> asignarValores(TypedQuery<Departamento> tq, Map<String, String> filters) {

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (null != entry.getValue() && !entry.getValue().isEmpty()) {
                if ("parametroNombre".equals(entry.getKey())) {
                    //
                    tq.setParameter(entry.getKey(), "%" + entry.getValue().toUpperCase() + "%");
                }
                if ("parametroFacultad".equals(entry.getKey())) {
                    tq.setParameter(entry.getKey(), new BigInteger(entry.getValue()));
                }
            }
        }
        return tq;
    }

}
