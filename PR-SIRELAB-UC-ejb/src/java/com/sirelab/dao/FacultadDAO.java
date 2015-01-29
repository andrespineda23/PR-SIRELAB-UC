package com.sirelab.dao;

import com.sirelab.dao.interfacedao.FacultadDAOInterface;
import com.sirelab.entidades.Facultad;
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
public class FacultadDAO implements FacultadDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearFacultad(Facultad facultad) {
        try {
            em.persist(facultad);
        } catch (Exception e) {
            System.out.println("Error crearFacultad FacultadDAO : " + e.toString());
        }
    }

    @Override
    public void editarFacultad(Facultad facultad) {
        try {
            em.merge(facultad);
        } catch (Exception e) {
            System.out.println("Error editarFacultad FacultadDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarFacultad(Facultad facultad) {
        try {
            em.remove(em.merge(facultad));
        } catch (Exception e) {
            System.out.println("Error eliminarFacultad FacultadDAO : " + e.toString());
        }
    }

    @Override
    public List<Facultad> consultarFacultades() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Facultad p");
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Facultad> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.err.println("Error consultarFacultades FacultadDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public Facultad buscarFacultadPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Facultad p WHERE p.idfacultad=:idRegistro");
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            Facultad registro = (Facultad) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.err.println("Error buscarFacultadPorID FacultadDAO : " + e.toString());
            return null;
        }
    }
}
