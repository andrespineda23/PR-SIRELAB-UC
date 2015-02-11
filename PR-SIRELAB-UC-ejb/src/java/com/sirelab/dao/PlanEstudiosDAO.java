package com.sirelab.dao;

import com.sirelab.dao.interfacedao.PlanEstudiosDAOInterface;
import com.sirelab.entidades.PlanEstudios;
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
public class PlanEstudiosDAO implements PlanEstudiosDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearPlanEstudios(PlanEstudios planestudios) {
        try {
            em.persist(planestudios);
        } catch (Exception e) {
            System.out.println("Error crearPlanEstudios PlanEstudiosDAO : " + e.toString());
        }
    }

    @Override
    public void editarPlanEstudios(PlanEstudios planestudios) {
        try {
            em.merge(planestudios);
        } catch (Exception e) {
            System.out.println("Error editarPlanEstudios PlanEstudiosDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarPlanEstudios(PlanEstudios planestudios) {
        try {
            em.remove(em.merge(planestudios));
        } catch (Exception e) {
            System.out.println("Error eliminarPlanEstudios PlanEstudiosDAO : " + e.toString());
        }
    }

    @Override
    public List<PlanEstudios> consultarPlanesEstudios() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM PlanEstudios p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<PlanEstudios> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.err.println("Error consultarPlanesEstudios PlanEstudiosDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public PlanEstudios buscarPlanEstudiosPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM PlanEstudios p WHERE p.idplanestudios=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            PlanEstudios registro = (PlanEstudios) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.err.println("Error buscarPlanEstudiosPorID PlanEstudiosDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<PlanEstudios> consultarPlanesEstudiosPorCarrera(BigInteger idCarrera) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM PlanEstudios p WHERE p.carrera.idcarrera=:idCarrera");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idCarrera", idCarrera);
            List<PlanEstudios> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.err.println("Error consultarPlanesEstudiosPorCarrera PlanEstudiosDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<PlanEstudios> buscarPlanesEstudiosPorFiltrado(Map<String, String> filters) {
        try {
            final String alias = "a";
            final StringBuilder jpql = new StringBuilder();
            String jpql2;
            jpql.append("SELECT a FROM ").append(PlanEstudios.class.getSimpleName()).append(" " + alias);
            //
            jpql2 = adicionarFiltros(jpql.toString(), filters, alias);
            //
            System.out.println("jpql2.toString() : " + jpql2.toString());
            TypedQuery<PlanEstudios> tq = em.createQuery(jpql2.toString(), PlanEstudios.class);
            tq = asignarValores(tq, filters);
            return tq.getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarPlanesEstudiosPorFiltrado PlanEstudiosDAO : " + e.toString());
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
                                .append(".nombreplanestudio")
                                .append(") Like :parametroNombre");
                        camposFiltro++;
                    }
                    if ("parametroCodigo".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".codigoplanestudio")
                                .append(") Like :parametroCodigo");
                        camposFiltro++;
                    }
                    if ("parametroCodigo".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "departamento.iddepartamento");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroCarrera".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "carrera.idcarrera");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroDepartamento".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "carrera.departamento.iddepartamento");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroFacultad".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "carrera.departamento.facultad.idfacultad");
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

    private TypedQuery<PlanEstudios> asignarValores(TypedQuery<PlanEstudios> tq, Map<String, String> filters) {

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (null != entry.getValue() && !entry.getValue().isEmpty()) {
                if (("parametroNombre".equals(entry.getKey()))
                        || ("parametroCodigo".equals(entry.getKey()))) {
                    //
                    tq.setParameter(entry.getKey(), "%" + entry.getValue().toUpperCase() + "%");
                }
                if (("parametroDepartamento".equals(entry.getKey()))
                        || ("parametroCarrera".equals(entry.getKey()))
                        || ("parametroFacultad".equals(entry.getKey()))) {
                    //
                    tq.setParameter(entry.getKey(), new BigInteger(entry.getValue()));
                }

            }
        }
        return tq;
    }

}
