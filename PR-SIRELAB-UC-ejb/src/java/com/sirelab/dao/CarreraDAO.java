package com.sirelab.dao;

import com.sirelab.dao.interfacedao.CarreraDAOInterface;
import com.sirelab.entidades.Carrera;
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
public class CarreraDAO implements CarreraDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearCarrera(Carrera carrera) {
        try {
            em.persist(carrera);
        } catch (Exception e) {
            System.out.println("Error crearCarrera CarreraDAO : " + e.toString());
        }
    }

    @Override
    public void editarCarrera(Carrera carrera) {
        try {
            em.merge(carrera);
        } catch (Exception e) {
            System.out.println("Error editarCarrera CarreraDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarCarrera(Carrera carrera) {
        try {
            em.remove(em.merge(carrera));
        } catch (Exception e) {
            System.out.println("Error eliminarCarrera CarreraDAO : " + e.toString());
        }
    }

    @Override
    public List<Carrera> consultarCarreras() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Carrera p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Carrera> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.err.println("Error consultarCarreras CarreraDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public Carrera buscarCarreraPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Carrera p WHERE p.idcarrera=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            Carrera registro = (Carrera) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.err.println("Error buscarCarreraPorID CarreraDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Carrera> consultarCarrerasPorDepartamento(BigInteger idDepartamento) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Carrera p WHERE p.departamento.iddepartamento=:idDepartamento");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idDepartamento", idDepartamento);
            List<Carrera> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.err.println("Error consultarCarreras CarreraDAO : " + e.toString());
            return null;
        }
    }

}
