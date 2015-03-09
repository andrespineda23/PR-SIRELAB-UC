/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao;

import com.sirelab.dao.interfacedao.TipoActivoDAOInterface;
import com.sirelab.entidades.TipoActivo;
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
public class TipoActivoDAO implements TipoActivoDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearTipoActivo(TipoActivo tipoActivo) {
        try {
            em.persist(tipoActivo);
        } catch (Exception e) {
            System.out.println("Error crearTipoActivo TipoActivoDAO : " + e.toString());
        }
    }

    @Override
    public void editarTipoActivo(TipoActivo tipoActivo) {
        try {
            em.merge(tipoActivo);
        } catch (Exception e) {
            System.out.println("Error editarTipoActivo TipoActivoDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarTipoActivo(TipoActivo tipoActivo) {
        try {
            em.remove(em.merge(tipoActivo));
        } catch (Exception e) {
            System.out.println("Error eliminarTipoActivo TipoActivoDAO : " + e.toString());
        }
    }

    @Override
    public List<TipoActivo> consultarTiposActivos() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM TipoActivo p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TipoActivo> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarTiposActivos TipoActivoDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public TipoActivo buscarTipoActivoPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM TipoActivo p WHERE p.idtipoactivo=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            TipoActivo registro = (TipoActivo) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarTipoActivoPorID TipoActivoDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public TipoActivo buscarTipoActivoPorNombre(String nombreTipoActivo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM TipoActivo p WHERE UPPER(p.nombretipoactivo) Like :nombreTipoActivo");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("nombreTipoActivo", "%" + nombreTipoActivo + "%");
            TipoActivo registro = (TipoActivo) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarTipoActivoPorNombre TipoActivoDAO : " + e.toString());
            return null;
        }
    }
}
