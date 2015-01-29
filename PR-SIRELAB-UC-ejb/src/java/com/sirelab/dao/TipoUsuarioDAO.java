package com.sirelab.dao;

import com.sirelab.dao.interfacedao.TipoUsuarioDAOInterface;
import com.sirelab.entidades.TipoUsuario;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ANDRES PINEDA
 */
@Stateless
public class TipoUsuarioDAO implements TipoUsuarioDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearTipoUsuario(TipoUsuario tipoUsuario) {
        try {
            em.persist(tipoUsuario);
        } catch (Exception e) {
            System.out.println("Error crearTipoUsuario TipoUsuarioDAO : " + e.toString());
        }
    }

    @Override
    public void editarTipoUsuario(TipoUsuario tipoUsuario) {
        try {
            em.merge(tipoUsuario);
        } catch (Exception e) {
            System.out.println("Error editarTipoUsuario TipoUsuarioDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarTipoUsuario(TipoUsuario tipoUsuario) {
        try {
            em.remove(em.merge(tipoUsuario));
        } catch (Exception e) {
            System.out.println("Error eliminarTipoUsuario TipoUsuarioDAO : " + e.toString());
        }
    }

    @Override
    public List<TipoUsuario> consultarTiposUsuarios() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM TipoUsuario p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TipoUsuario> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarTiposUsuarios TipoUsuarioDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public TipoUsuario buscarTipoUsuarioPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM TipoUsuario p WHERE p.idtipousuario=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            TipoUsuario registro = (TipoUsuario) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarTipoUsuarioPorID TipoUsuarioDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public TipoUsuario buscarTipoUsuarioPorNombre(String nombreTipoUsuario) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM TipoUsuario p WHERE p.nombretipousuario=:nombreTipoUsuario");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("nombreTipoUsuario", nombreTipoUsuario);
            TipoUsuario registro = (TipoUsuario) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarTipoUsuarioPorNombre TipoUsuarioDAO : " + e.toString());
            return null;
        }
    }
}
