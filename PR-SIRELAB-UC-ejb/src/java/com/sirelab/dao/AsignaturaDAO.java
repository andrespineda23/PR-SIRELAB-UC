package com.sirelab.dao;

import com.sirelab.dao.interfacedao.AsignaturaDAOInterface;
import com.sirelab.entidades.Asignatura;
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
public class AsignaturaDAO implements AsignaturaDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearAsignatura(Asignatura asignatura) {
        try {
            em.clear();
            em.persist(asignatura);
        } catch (Exception e) {
            System.out.println("Error crearAsignatura AsignaturaDAO : " + e.toString());
        }
    }

    @Override
    public void editarAsignatura(Asignatura asignatura) {
        try {
            em.clear();
            em.merge(asignatura);
        } catch (Exception e) {
            System.out.println("Error editarAsignatura AsignaturaDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarAsignatura(Asignatura asignatura) {
        try {
            em.clear();
            em.remove(em.merge(asignatura));
        } catch (Exception e) {
            System.out.println("Error eliminarAsignatura AsignaturaDAO : " + e.toString());
        }
    }

    @Override
    public List<Asignatura> consultarAsignaturas() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Asignatura p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Asignatura> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarAsignaturas AsignaturaDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public Asignatura buscarAsignaturaPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Asignatura p WHERE p.idasignatura=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            Asignatura registro = (Asignatura) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarAsignaturaPorID AsignaturaDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Asignatura> buscarAsignaturasPorFiltrado(Map<String, String> filters) {
        try {
            final String alias = "a";
            final StringBuilder jpql = new StringBuilder();
            String jpql2;
            jpql.append("SELECT a FROM ").append(Asignatura.class.getSimpleName()).append(" " + alias);
            //
            jpql2 = adicionarFiltros(jpql.toString(), filters, alias);
            //
            System.out.println("jpql2.toString() : " + jpql2.toString());
            TypedQuery<Asignatura> tq = em.createQuery(jpql2.toString(), Asignatura.class);
            tq = asignarValores(tq, filters);
            return tq.getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarAsignaturasPorFiltrado AsignaturaDAO : " + e.toString());
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
                                .append(".nombreasignatura")
                                .append(") Like :parametroNombre");
                        camposFiltro++;
                    }
                    if ("parametroCreditos".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "numerocreditos");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroPlanEstudio".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "planestudios.idplanestudios");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroCarrera".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "planestudios.carrera.idcarrera");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroDepartamento".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "planestudios.carrera.departamento.iddepartamento");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroFacultad".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "planestudios.carrera.departamento.facultad.idfacultad");
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

    private TypedQuery<Asignatura> asignarValores(TypedQuery<Asignatura> tq, Map<String, String> filters) {

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (null != entry.getValue() && !entry.getValue().isEmpty()) {
                if ("parametroNombre".equals(entry.getKey())) {
                    tq.setParameter(entry.getKey(), "%" + entry.getValue().toUpperCase() + "%");
                }
                if ("parametroCreditos".equals(entry.getKey())) {
                    tq.setParameter(entry.getKey(), Integer.valueOf(entry.getValue()));
                }
                if (("parametroDepartamento".equals(entry.getKey()))
                        || ("parametroPlanEstudio".equals(entry.getKey()))
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
