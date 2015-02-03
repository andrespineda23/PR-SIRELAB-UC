package com.sirelab.dao;

import com.sirelab.dao.interfacedao.EntidadExternaDAOInterface;
import com.sirelab.entidades.EntidadExterna;
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
public class EntidadExternaDAO implements EntidadExternaDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearEntidadExterna(EntidadExterna entidadexterna) {
        try {
            em.persist(entidadexterna);
        } catch (Exception e) {
            System.out.println("Error crearEntidadExterna EntidadExternaDAO : " + e.toString());
        }
    }

    @Override
    public void editarEntidadExterna(EntidadExterna entidadexterna) {
        try {
            em.merge(entidadexterna);
        } catch (Exception e) {
            System.out.println("Error editarEntidadExterna EntidadExternaDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarEntidadExterna(EntidadExterna entidadexterna) {
        try {
            em.remove(em.merge(entidadexterna));
        } catch (Exception e) {
            System.out.println("Error eliminarEntidadExterna EntidadExternaDAO : " + e.toString());
        }
    }

    @Override
    public List<EntidadExterna> consultarEntidadesExternas() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM EntidadExterna p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<EntidadExterna> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarEntidadExternas EntidadExternaDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public EntidadExterna buscarEntidadExternaPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM EntidadExterna p WHERE p.identidadexterna=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            EntidadExterna registro = (EntidadExterna) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarEntidadExternaPorID EntidadExternaDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public EntidadExterna buscarEntidadExternaPorIDPersona(BigInteger idPersona) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM EntidadExterna p WHERE p.persona.idpersona=:idPersona");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idPersona", idPersona);
            EntidadExterna registro = (EntidadExterna) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarEntidadExternaPorIDPersona EntidadExternaDAO : " + e.toString());
            return null;
        }
    }
    
    @Override
    public EntidadExterna buscarEntidadExternaPorCorreoNumDocumento(String correo, String documento) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM EntidadExterna p WHERE p.persona.emailpersona=:correo AND p.persona.identificacionpersona=:documento");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("correo", correo);
            query.setParameter("documento", documento);
            EntidadExterna registro = (EntidadExterna) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarEntidadExternaPorCorreoNumDocumento EntidadExternaDAO : " + e.toString());
            return null;
        }
    }
    
    @Override
    public List<EntidadExterna> buscarEntidadesExternasPorFiltrado(Map<String, String> filters) {
        try {
            final String alias = "a";
            final StringBuilder jpql = new StringBuilder();
            String jpql2;
            jpql.append("SELECT a FROM ").append(EntidadExterna.class.getSimpleName()).append(" " + alias);
            //
            jpql2 = adicionarFiltros(jpql.toString(), filters, alias);
            //
            System.out.println("jpql2.toString() : " + jpql2.toString());
            TypedQuery<EntidadExterna> tq = em.createQuery(jpql2.toString(), EntidadExterna.class);
            tq = asignarValores(tq, filters);
            return tq.getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarEntidadesExternasPorFiltrado EntidadExternaDAO : " + e.toString());
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
                    if ("parametroEstado".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "persona.usuario.estado");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroNombre".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".persona.nombrespersona")
                                .append(") Like :parametroNombre");
                        camposFiltro++;
                    }
                    if ("parametroApellido".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".persona.apellidospersona")
                                .append(") Like :parametroApellido");
                        camposFiltro++;
                    }
                    if ("parametroDocumento".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".persona.identificacionpersona")
                                .append(") Like :parametroDocumento");
                        camposFiltro++;
                    }
                    if ("parametroCorreo".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".persona.emailpersona")
                                .append(") Like :parametroCorreo");
                        camposFiltro++;
                    }
                    if ("parametroCargo".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".cargodocente")
                                .append(") Like :parametroCargo");
                        camposFiltro++;
                    }
                    if ("parametroIDEntidad".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".identificacionentidad")
                                .append(") Like :parametroIDEntidad");
                        camposFiltro++;
                    }
                    if ("parametroNombreEntidad".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".nombreentidad")
                                .append(") Like :parametroCargo");
                        camposFiltro++;
                    }
                    if ("parametroEmailEntidad".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".emailentidad")
                                .append(") Like :parametroCargo");
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

    private TypedQuery<EntidadExterna> asignarValores(TypedQuery<EntidadExterna> tq, Map<String, String> filters) {

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (null != entry.getValue() && !entry.getValue().isEmpty()) {
                if (("parametroCorreo".equals(entry.getKey()))
                        || ("parametroDocumento".equals(entry.getKey()))
                        || ("parametroNombre".equals(entry.getKey()))
                        || ("parametroCargo".equals(entry.getKey()))
                        || ("parametroIDEntidad".equals(entry.getKey()))
                        || ("parametroNombreEntidad".equals(entry.getKey()))
                        || ("parametroEmailEntidad".equals(entry.getKey()))
                        || ("parametroApellido".equals(entry.getKey()))) {
                    //
                    tq.setParameter(entry.getKey(), "%" + entry.getValue().toUpperCase() + "%");
                }
                if (("parametroEstado".equals(entry.getKey()))) {
                    tq.setParameter(entry.getKey(), Boolean.valueOf(entry.getValue()));
                }
            }
        }
        return tq;
    }
}
