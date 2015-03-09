/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.bo.recursoslab;

import com.sirelab.bo.interfacebo.GestionarRecursoInsumosBOInterface;
import com.sirelab.dao.interfacedao.InsumoDAOInterface;
import com.sirelab.dao.interfacedao.ProveedorDAOInterface;
import com.sirelab.entidades.Insumo;
import com.sirelab.entidades.Proveedor;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ANDRES PINEDA
 */
@Stateless
public class GestionarRecursoInsumosBO implements GestionarRecursoInsumosBOInterface {

    @EJB
    ProveedorDAOInterface proveedorDAO;
    @EJB
    InsumoDAOInterface insumoDAO;

    @Override
    public List<Insumo> consultarInsumosPorParametro(Map<String, String> filtros) {
        try {
            List<Insumo> lista = insumoDAO.buscarInsumosPorFiltrado(filtros);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarRecursoInsumosBO consultarInsumosPorParametro : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearNuevoInsumo(Insumo insumo) {
        try {
            insumoDAO.crearInsumo(insumo);
        } catch (Exception e) {
            System.out.println("Error GestionarRecursoInsumosBO crearNuevoInsumo : " + e.toString());
        }
    }

    @Override
    public void modificarInformacionInsumo(Insumo insumo) {
        try {
            insumoDAO.editarInsumo(insumo);
        } catch (Exception e) {
            System.out.println("Error GestionarRecursoInsumosBO modificarInformacionInsumo : " + e.toString());
        }
    }

    @Override 
    public Insumo obtenerInsumoPorIDInsumo(BigInteger idInsumo) {
        try {
            Insumo registro = insumoDAO.buscarInsumoPorID(idInsumo);
            return registro;
        } catch (Exception e) {
            System.out.println("Error GestionarRecursoInsumosBO obtenerInsumoPorIDProveedor : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Proveedor> consultarProveedoresRegistrados() {
        try {
            List<Proveedor> lista = proveedorDAO.consultarProveedores();
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarRecursoInsumosBO consultarProveedoresRegistrados : " + e.toString());
            return null;
        }
    }

}
