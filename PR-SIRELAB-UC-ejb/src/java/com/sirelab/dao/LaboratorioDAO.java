package com.sirelab.dao;

import com.sirelab.dao.interfacedao.LaboratorioDAOInterface;
import com.sirelab.entidades.Laboratorio;
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
public class LaboratorioDAO implements LaboratorioDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearLaboratorio(Laboratorio laboratorio) {
        try {
            em.persist(laboratorio);
        } catch (Exception e) {
            System.out.println("Error crearLaboratorio LaboratorioDAO : " + e.toString());
        }
    }

    @Override
    public void editarLaboratorio(Laboratorio laboratorio) {
        try {
            em.merge(laboratorio);
        } catch (Exception e) {
            System.out.println("Error editarLaboratorio LaboratorioDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarLaboratorio(Laboratorio laboratorio) {
        try {
            em.remove(em.merge(laboratorio));
        } catch (Exception e) {
            System.out.println("Error eliminarLaboratorio LaboratorioDAO : " + e.toString());
        }
    }

    @Override
    public List<Laboratorio> consultarLaboratorios() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Laboratorio p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Laboratorio> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarLaboratorios LaboratorioDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public Laboratorio buscarLaboratorioPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Laboratorio p WHERE p.idlaboratorio=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            Laboratorio registro = (Laboratorio) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarLaboratorioPorID LaboratorioDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Laboratorio> buscarLaboratorioPorIDDepartamento(BigInteger idDepartamento) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Laboratorio p WHERE p.departamento.iddepartamento=:idDepartamento");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idDepartamento", idDepartamento);
            List<Laboratorio> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error buscarLaboratorioPorIDDepartamento LaboratorioDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Laboratorio> buscarLaboratoriosPorFiltrado(Map<String, String> filters) {
        try {
            final String alias = "a";
            final StringBuilder jpql = new StringBuilder();
            String jpql2;
            jpql.append("SELECT a FROM ").append(Laboratorio.class.getSimpleName()).append(" " + alias);
            //
            jpql2 = adicionarFiltros(jpql.toString(), filters, alias);
            //
            System.out.println("jpql2.toString() : " + jpql2.toString());
            TypedQuery<Laboratorio> tq = em.createQuery(jpql2.toString(), Laboratorio.class);
            tq = asignarValores(tq, filters);
            return tq.getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarLaboratoriosPorFiltrado LaboratorioDAO : " + e.toString());
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
                                .append(".nombrelaboratorio")
                                .append(") Like :parametroNombre");
                        camposFiltro++;
                    }
                    if ("parametroDepartamento".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "departamento.iddepartamento");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroFacultad".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "departamento.facultad.idfacultad");
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

    private TypedQuery<Laboratorio> asignarValores(TypedQuery<Laboratorio> tq, Map<String, String> filters) {

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (null != entry.getValue() && !entry.getValue().isEmpty()) {
                if ("parametroNombre".equals(entry.getKey())) {
                    tq.setParameter(entry.getKey(), "%" + entry.getValue().toUpperCase() + "%");
                }
                if (("parametroDepartamento".equals(entry.getKey()))
                        || ("parametroFacultad".equals(entry.getKey()))) {
                    //
                    tq.setParameter(entry.getKey(), new BigInteger(entry.getValue()));
                }

            }
        }
        return tq;
    }

}
