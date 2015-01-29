package com.sirelab.dao;

import com.sirelab.dao.interfacedao.DepartamentoDAOInterface;
import com.sirelab.entidades.Departamento;
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
public class DepartamentoDAO implements DepartamentoDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearDepartamento(Departamento departamento) {
        try {
            em.persist(departamento);
        } catch (Exception e) {
            System.out.println("Error crearDepartamento DepartamentoDAO : " + e.toString());
        }
    }

    @Override
    public void editarDepartamento(Departamento departamento) {
        try {
            em.merge(departamento);
        } catch (Exception e) {
            System.out.println("Error editarDepartamento DepartamentoDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarDepartamento(Departamento departamento) {
        try {
            em.remove(em.merge(departamento));
        } catch (Exception e) {
            System.out.println("Error eliminarDepartamento DepartamentoDAO : " + e.toString());
        }
    }

    @Override
    public List<Departamento> consultarDepartamentos() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Departamento p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Departamento> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.err.println("Error consultarDepartamentos DepartamentoDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public Departamento buscarDepartamentoPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Departamento p WHERE p.iddepartamento=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            Departamento registro = (Departamento) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.err.println("Error buscarDepartamentoPorID DepartamentoDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Departamento> buscarDepartamentosPorIDFacultad(BigInteger idFacultad) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Departamento p WHERE p.facultad.idfacultad=:idFacultad");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idFacultad", idFacultad);
            List<Departamento> registro = query.getResultList();
            return registro;
        } catch (Exception e) {
            System.err.println("Error buscarDepartamentosPorIDFacultad DepartamentoDAO : " + e.toString());
            return null;
        }
    }
}
