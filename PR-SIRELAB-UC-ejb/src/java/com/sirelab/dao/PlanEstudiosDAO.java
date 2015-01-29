package com.sirelab.dao;

import com.sirelab.dao.interfacedao.PlanEstudiosDAOInterface;
import com.sirelab.entidades.PlanEstudios;
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
public class PlanEstudiosDAO implements PlanEstudiosDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearPlanEstudios(PlanEstudios planestudios) {
        try {
            em.persist(planestudios);
        } catch (Exception e) {
            System.out.println("Error crearPlanEstudios PlanEstudiosDAO : " + e.toString());
        }
    }

    @Override
    public void editarPlanEstudios(PlanEstudios planestudios) {
        try {
            em.merge(planestudios);
        } catch (Exception e) {
            System.out.println("Error editarPlanEstudios PlanEstudiosDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarPlanEstudios(PlanEstudios planestudios) {
        try {
            em.remove(em.merge(planestudios));
        } catch (Exception e) {
            System.out.println("Error eliminarPlanEstudios PlanEstudiosDAO : " + e.toString());
        }
    }

    @Override
    public List<PlanEstudios> consultarPlanesEstudios() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM PlanEstudios p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<PlanEstudios> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.err.println("Error consultarPlanesEstudios PlanEstudiosDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public PlanEstudios buscarPlanEstudiosPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM PlanEstudios p WHERE p.idplanestudios=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            PlanEstudios registro = (PlanEstudios) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.err.println("Error buscarPlanEstudiosPorID PlanEstudiosDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<PlanEstudios> consultarPlanesEstudiosPorCarrera(BigInteger idCarrera) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM PlanEstudios p WHERE p.carrera.idcarrera=:idCarrera");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idCarrera", idCarrera);
            List<PlanEstudios> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.err.println("Error consultarPlanesEstudiosPorCarrera PlanEstudiosDAO : " + e.toString());
            return null;
        }
    }
}
