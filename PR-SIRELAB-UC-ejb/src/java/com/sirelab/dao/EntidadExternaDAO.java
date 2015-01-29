package com.sirelab.dao;

import com.sirelab.dao.interfacedao.EntidadExternaDAOInterface;
import com.sirelab.entidades.EntidadExterna;
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
}
