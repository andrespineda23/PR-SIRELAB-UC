package com.sirelab.dao;

import com.sirelab.dao.interfacedao.EncargadoLaboratorioDAOInterface;
import com.sirelab.entidades.EncargadoLaboratorio;
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
public class EncargadoLaboratorioDAO implements EncargadoLaboratorioDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearEncargadoLaboratorio(EncargadoLaboratorio encargadolaboratorio) {
        try {
            em.persist(encargadolaboratorio);
        } catch (Exception e) {
            System.out.println("Error crearEncargadoLaboratorio EncargadoLaboratorioDAO : " + e.toString());
        }
    }

    @Override
    public void editarEncargadoLaboratorio(EncargadoLaboratorio encargadolaboratorio) {
        try {
            em.merge(encargadolaboratorio);
        } catch (Exception e) {
            System.out.println("Error editarEncargadoLaboratorio EncargadoLaboratorioDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarEncargadoLaboratorio(EncargadoLaboratorio encargadolaboratorio) {
        try {
            em.remove(em.merge(encargadolaboratorio));
        } catch (Exception e) {
            System.out.println("Error eliminarEncargadoLaboratorio EncargadoLaboratorioDAO : " + e.toString());
        }
    }

    @Override
    public List<EncargadoLaboratorio> consultarEncargadosLaboratorios() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM EncargadoLaboratorio p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<EncargadoLaboratorio> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarEncargadoLaboratorios EncargadoLaboratorioDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public EncargadoLaboratorio buscarEncargadoLaboratorioPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM EncargadoLaboratorio p WHERE p.idencargadolaboratorio=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            EncargadoLaboratorio registro = (EncargadoLaboratorio) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarEncargadoLaboratorioPorID EncargadoLaboratorioDAO : " + e.toString());
            return null;
        }
    }
    
    @Override
    public EncargadoLaboratorio buscarEncargadoLaboratorioPorIDPersona(BigInteger idPersona) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM EncargadoLaboratorio p WHERE p.persona.idpersona=:idPersona");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idPersona", idPersona);
            EncargadoLaboratorio registro = (EncargadoLaboratorio) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarEncargadoLaboratorioPorIDPersona EncargadoLaboratorioDAO : " + e.toString());
            return null;
        }
    }
}
