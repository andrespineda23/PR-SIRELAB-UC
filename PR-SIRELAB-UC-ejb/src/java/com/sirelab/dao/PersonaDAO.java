package com.sirelab.dao;

import com.sirelab.dao.interfacedao.PersonaDAOInterface;
import com.sirelab.entidades.Persona;
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
public class PersonaDAO implements PersonaDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearPersona(Persona persona) {
        try {
            em.persist(persona);
            em.flush();
        } catch (Exception e) {
            System.out.println("Error crearPersona PersonaDAO : " + e.toString());
        }
    }

    @Override
    public void editarPersona(Persona persona) {
        try {
            em.merge(persona);
            em.flush();
        } catch (Exception e) {
            System.out.println("Error editarPersona PersonaDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarPersona(Persona persona) {
        try {
            em.remove(em.merge(persona));
            em.flush();
        } catch (Exception e) {
            System.out.println("Error eliminarPersona PersonaDAO : " + e.toString());
        }
    }

    @Override
    public List<Persona> consultarPersonas() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Persona p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Persona> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarPersonas PersonaDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public Persona buscarPersonaPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Persona p WHERE p.idpersona=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            Persona registro = (Persona) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarPersonaPorID PersonaDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public Persona obtenerUltimaPersonaRegistrada() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Persona p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Persona> registros = query.getResultList();
            if (registros != null) {
                int tam = registros.size();
                Persona ultimoRegistro = registros.get(tam - 1);
                return ultimoRegistro;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error obtenerUltimaPersonaRegistrada PersonaDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public Persona buscarPersonaPorCorreoYNumeroIdentificacion(String correo, String identificacion) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Persona p WHERE p.emailpersona=:correo AND p.identificacionpersona=:identificacion");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("correo", correo);
            query.setParameter("identificacion", identificacion);
            Persona registro = (Persona) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarPersonaPorID PersonaDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public Persona obtenerPersonaLoginUserPassword(String usuario, String password) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Persona p WHERE p.usuario.nombreusuario=:usuario AND p.usuario.passwordusuario=:password AND p.usuario.estado=true");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("usuario", usuario);
            query.setParameter("password", password);
            Persona registro = (Persona) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error obtenerPersonaLoginUserPassword PersonaDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Persona> buscarAdministradoresPorFiltrado(Map<String, String> filters) {
        try {
            final String alias = "a";
            final StringBuilder jpql = new StringBuilder();
            String jpql2;
            jpql.append("SELECT a FROM ").append(Persona.class.getSimpleName()).append(" " + alias);
            //
            jpql2 = adicionarFiltros(jpql.toString(), filters, alias);
            //
            System.out.println("jpql2.toString() : " + jpql2.toString());
            TypedQuery<Persona> tq = em.createQuery(jpql2.toString(), Persona.class);
            tq = asignarValores(tq, filters);
            return tq.getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarPersonasPorFiltrado PersonaDAO : " + e.toString());
            return null;
        }
    }

    private String adicionarFiltros(String jpql, Map<String, String> filters, String alias) {
        final StringBuilder wheres = new StringBuilder();
        wheres.append(" WHERE a.usuario.tipousuario.nombretipousuario = 'ADMINISTRADOR'");
        int camposFiltro = 1;
        if (null != filters && !filters.isEmpty()) {
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                if (null != entry.getValue() && !entry.getValue().isEmpty()) {
                    if (camposFiltro > 0) {
                        wheres.append(" AND ");
                    }
                    if ("parametroEstado".equals(entry.getKey())) {
                        wheres.append(alias).append("." + "usuario.estado");
                        wheres.append("= :").append(entry.getKey());
                        camposFiltro++;
                    }
                    if ("parametroNombre".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".nombrespersona")
                                .append(") Like :parametroNombre");
                        camposFiltro++;
                    }
                    if ("parametroApellido".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".apellidospersona")
                                .append(") Like :parametroApellido");
                        camposFiltro++;
                    }
                    if ("parametroDocumento".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".identificacionpersona")
                                .append(") Like :parametroDocumento");
                        camposFiltro++;
                    }
                    if ("parametroCorreo".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".emailpersona")
                                .append(") Like :parametroCorreo");
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

    private TypedQuery<Persona> asignarValores(TypedQuery<Persona> tq, Map<String, String> filters) {

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (null != entry.getValue() && !entry.getValue().isEmpty()) {
                if (("parametroCorreo".equals(entry.getKey()))
                        || ("parametroDocumento".equals(entry.getKey()))
                        || ("parametroNombre".equals(entry.getKey()))
                        || ("parametroCargo".equals(entry.getKey()))
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
