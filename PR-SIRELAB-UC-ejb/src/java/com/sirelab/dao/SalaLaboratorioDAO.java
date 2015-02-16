package com.sirelab.dao;

import com.sirelab.dao.interfacedao.SalaLaboratorioDAOInterface;
import com.sirelab.entidades.SalaLaboratorio;
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
public class SalaLaboratorioDAO implements SalaLaboratorioDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearSalaLaboratorio(SalaLaboratorio salalaboratorio) {
        try {
            em.persist(salalaboratorio);
        } catch (Exception e) {
            System.out.println("Error crearSalaLaboratorio SalaLaboratorioDAO : " + e.toString());
        }
    }

    @Override
    public void editarSalaLaboratorio(SalaLaboratorio salalaboratorio) {
        try {
            em.merge(salalaboratorio);
        } catch (Exception e) {
            System.out.println("Error editarSalaLaboratorio SalaLaboratorioDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarSalaLaboratorio(SalaLaboratorio salalaboratorio) {
        try {
            em.remove(em.merge(salalaboratorio));
        } catch (Exception e) {
            System.out.println("Error eliminarSalaLaboratorio SalaLaboratorioDAO : " + e.toString());
        }
    }

    @Override
    public List<SalaLaboratorio> consultarSalasLaboratorios() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM SalaLaboratorio p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<SalaLaboratorio> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarSalasLaboratorios SalaLaboratorioDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public SalaLaboratorio buscarSalaLaboratorioPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM SalaLaboratorio p WHERE p.idsalalaboratorio=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            SalaLaboratorio registro = (SalaLaboratorio) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarSalaLaboratorioPorID SalaLaboratorioDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<SalaLaboratorio> buscarSalaLaboratorioPorIDDepartamento(BigInteger idDepartamento) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM SalaLaboratorio p WHERE p.departamento.iddepartamento=:idDepartamento");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idDepartamento", idDepartamento);
            List<SalaLaboratorio> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error buscarSalaLaboratorioPorIDDepartamento SalaLaboratorioDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<SalaLaboratorio> buscarSalasLaboratoriosPorFiltrado(Map<String, String> filters) {
        try {
            final String alias = "a";
            final StringBuilder jpql = new StringBuilder();
            String jpql2;
            jpql.append("SELECT a FROM ").append(SalaLaboratorio.class.getSimpleName()).append(" " + alias);
            //
            jpql2 = adicionarFiltros(jpql.toString(), filters, alias);
            //
            System.out.println("jpql2.toString() : " + jpql2.toString());
            TypedQuery<SalaLaboratorio> tq = em.createQuery(jpql2.toString(), SalaLaboratorio.class);
            tq = asignarValores(tq, filters);
            return tq.getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarSalasLaboratoriosPorFiltrado SalaLaboratorioDAO : " + e.toString());
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
                                .append(".nombresala")
                                .append(") Like :parametroNombre");
                        camposFiltro++;
                    }
                    if ("parametroCodigo".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".codigosala")
                                .append(") Like :parametroCodigo");
                        camposFiltro++;
                    }
                    if ("parametroEstado".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "estadosala");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroCapacidad".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "capacidadsala");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroArea ".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "areaprofundizacion.idareaprofundizacion");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroLaboratorio".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "areaprofundizacion.laboratorio.idlaboratorio");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroDepartamento".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "areaprofundizacion.laboratorio.departamento.iddepartamento");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroSede".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "edificio.sede.idsede");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroEdificio".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "edificio.idedificio");
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

    private TypedQuery<SalaLaboratorio> asignarValores(TypedQuery<SalaLaboratorio> tq, Map<String, String> filters) {
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (null != entry.getValue() && !entry.getValue().isEmpty()) {
                if (("parametroNombre".equals(entry.getKey()))
                        || ("parametroCodigo".equals(entry.getKey()))) {
                    tq.setParameter(entry.getKey(), "%" + entry.getValue().toUpperCase() + "%");
                }
                if ("parametroEstado".equals(entry.getKey())) {
                    tq.setParameter(entry.getKey(), Boolean.valueOf(entry.getValue()));
                }
                if ("parametroCapacidad".equals(entry.getKey())) {
                    tq.setParameter(entry.getKey(), Integer.valueOf(entry.getValue()).intValue());
                }
                if (("parametroEdificio".equals(entry.getKey()))
                        || ("parametroSede".equals(entry.getKey()))
                        || ("parametroLaboratorio".equals(entry.getKey()))
                        || ("parametroArea".equals(entry.getKey()))
                        || ("parametroDepartamento".equals(entry.getKey()))) {
                    //
                    tq.setParameter(entry.getKey(), new BigInteger(entry.getValue()));
                }
            }
        }
        return tq;
    }
}
