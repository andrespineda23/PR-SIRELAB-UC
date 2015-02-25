package com.sirelab.dao;

import com.sirelab.dao.interfacedao.ModuloLaboratorioDAOInterface;
import com.sirelab.entidades.ModuloLaboratorio;
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
public class ModuloLaboratorioDAO implements ModuloLaboratorioDAOInterface {

     /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearModuloLaboratorio(ModuloLaboratorio modulolaboratorio) {
        try {
            em.persist(modulolaboratorio);
        } catch (Exception e) {
            System.out.println("Error crearModuloLaboratorio ModuloLaboratorioDAO : " + e.toString());
        }
    }

    @Override
    public void editarModuloLaboratorio(ModuloLaboratorio modulolaboratorio) {
        try {
            em.merge(modulolaboratorio);
        } catch (Exception e) {
            System.out.println("Error editarModuloLaboratorio ModuloLaboratorioDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarModuloLaboratorio(ModuloLaboratorio modulolaboratorio) {
        try {
            em.remove(em.merge(modulolaboratorio));
        } catch (Exception e) {
            System.out.println("Error eliminarModuloLaboratorio ModuloLaboratorioDAO : " + e.toString());
        }
    }

    @Override
    public List<ModuloLaboratorio> consultarModulosLaboratorios() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM ModuloLaboratorio p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<ModuloLaboratorio> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarModulosLaboratorios ModuloLaboratorioDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public ModuloLaboratorio buscarModuloLaboratorioPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM ModuloLaboratorio p WHERE p.idmodulolaboratorio=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            ModuloLaboratorio registro = (ModuloLaboratorio) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarModuloLaboratorioPorID ModuloLaboratorioDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<ModuloLaboratorio> buscarModuloLaboratorioPorIDSalaLaboratorio(BigInteger idSala) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM ModuloLaboratorio p WHERE p.modulolaboratorio.idmodulolaboratorio=:idSala");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idSala", idSala);
            List<ModuloLaboratorio> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error buscarModuloLaboratorioPorIDDepartamento ModuloLaboratorioDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<ModuloLaboratorio> buscarModulosLaboratoriosPorFiltrado(Map<String, String> filters) {
        try {
            final String alias = "a";
            final StringBuilder jpql = new StringBuilder();
            String jpql2;
            jpql.append("SELECT a FROM ").append(ModuloLaboratorio.class.getSimpleName()).append(" " + alias);
            //
            jpql2 = adicionarFiltros(jpql.toString(), filters, alias);
            //
            System.out.println("jpql2.toString() : " + jpql2.toString());
            TypedQuery<ModuloLaboratorio> tq = em.createQuery(jpql2.toString(), ModuloLaboratorio.class);
            tq = asignarValores(tq, filters);
            return tq.getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarModulosLaboratoriosPorFiltrado ModuloLaboratorioDAO : " + e.toString());
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
                                .append(".nombremodulo")
                                .append(") Like :parametroNombre");
                        camposFiltro++;
                    }
                    if ("parametroDetalle".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".detallemodulo")
                                .append(") Like :parametroDetalle");
                        camposFiltro++;
                    }
                    if ("parametroEstado".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "estadomodulo");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroSalaLaboratorio".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "salalaboratorio.idsalalaboratorio");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroLaboratorio".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "salalaboratorio.areaprofundizacion.laboratorio.idlaboratorio");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroAreaProfundizacion".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "salalaboratorio.areaprofundizacion.idareaprofundizacion");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroSede".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "salalaboratorio.edificio.sede.idsede");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroEdificio".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "salalaboratorio.edificio.idedificio");
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

    private TypedQuery<ModuloLaboratorio> asignarValores(TypedQuery<ModuloLaboratorio> tq, Map<String, String> filters) {
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (null != entry.getValue() && !entry.getValue().isEmpty()) {
                if (("parametroNombre".equals(entry.getKey()))
                        || ("parametroDetalle".equals(entry.getKey()))) {
                    tq.setParameter(entry.getKey(), "%" + entry.getValue().toUpperCase() + "%");
                }
                if ("parametroEstado".equals(entry.getKey())) {
                    tq.setParameter(entry.getKey(), Boolean.valueOf(entry.getValue()));
                }
                if (("parametroEdificio".equals(entry.getKey()))
                        || ("parametroSede".equals(entry.getKey()))
                        || ("parametroLaboratorio".equals(entry.getKey()))
                        || ("parametroAreaProfundizacion".equals(entry.getKey()))
                        || ("parametroSalaLaboratorio".equals(entry.getKey()))) {
                    //
                    tq.setParameter(entry.getKey(), new BigInteger(entry.getValue()));
                }
            }
        }
        return tq;
    }
}
