package com.sirelab.dao;

import com.sirelab.dao.interfacedao.LaboratorioDAOInterface;
import com.sirelab.entidades.Laboratorio;
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
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Laboratorio> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.err.println("Error consultarLaboratorios LaboratorioDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public Laboratorio buscarLaboratorioPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Laboratorio p WHERE p.idlaboratorio=:idRegistro");
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            Laboratorio registro = (Laboratorio) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.err.println("Error buscarLaboratorioPorID LaboratorioDAO : " + e.toString());
            return null;
        }
    }
}
