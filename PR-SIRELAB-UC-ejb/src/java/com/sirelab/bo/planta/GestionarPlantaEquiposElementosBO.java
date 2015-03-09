/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.bo.planta;

import com.sirelab.bo.interfacebo.GestionarPlantaEquiposElementosBOInterface;
import com.sirelab.dao.interfacedao.AreaProfundizacionDAOInterface;
import com.sirelab.dao.interfacedao.EquipoElementoDAOInterface;
import com.sirelab.dao.interfacedao.EstadoEquipoDAOInterface;
import com.sirelab.dao.interfacedao.LaboratorioDAOInterface;
import com.sirelab.dao.interfacedao.ModuloLaboratorioDAOInterface;
import com.sirelab.dao.interfacedao.ProveedorDAOInterface;
import com.sirelab.dao.interfacedao.SalaLaboratorioDAOInterface;
import com.sirelab.dao.interfacedao.TipoActivoDAOInterface;
import com.sirelab.entidades.AreaProfundizacion;
import com.sirelab.entidades.EquipoElemento;
import com.sirelab.entidades.EstadoEquipo;
import com.sirelab.entidades.Laboratorio;
import com.sirelab.entidades.ModuloLaboratorio;
import com.sirelab.entidades.Proveedor;
import com.sirelab.entidades.SalaLaboratorio;
import com.sirelab.entidades.TipoActivo;
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
public class GestionarPlantaEquiposElementosBO implements GestionarPlantaEquiposElementosBOInterface {

    @EJB
    ModuloLaboratorioDAOInterface moduloLaboratorioDAO;
    @EJB
    LaboratorioDAOInterface laboratorioDAO;
    @EJB
    AreaProfundizacionDAOInterface areaProfundizacionDAO;
    @EJB
    SalaLaboratorioDAOInterface salaLaboratorioDAO;
    @EJB
    TipoActivoDAOInterface tipoActivoDAO;
    @EJB
    EstadoEquipoDAOInterface estadoEquipoDAO;
    @EJB
    EquipoElementoDAOInterface equipoElementoDAO;
    @EJB
    ProveedorDAOInterface proveedorDAO;

    //@Override
    public List<Laboratorio> consultarLaboratoriosRegistrados() {
        try {
            List<Laboratorio> lista = laboratorioDAO.consultarLaboratorios();
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaElementosEquiposBO consultarLaboratoriosPorIDDepartamento : " + e.toString());
            return null;
        }
    }

    @Override
    public List<EstadoEquipo> consultarEstadosEquiposRegistrados() {
        try {
            List<EstadoEquipo> lista = estadoEquipoDAO.consultarEstadosEquipos();
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaElementosEquiposBO consultarEstadosEquiposRegistrados : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TipoActivo> consultarTiposActivosRegistrador() {
        try {
            List<TipoActivo> lista = tipoActivoDAO.consultarTiposActivos();
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaElementosEquiposBO consultarTiposActivosRegistrador : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Proveedor> consultarProveedoresRegistrados() {
        try {
            List<Proveedor> lista = proveedorDAO.consultarProveedores();
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaElementosEquiposBO consultarProveedoresRegistrados : " + e.toString());
            return null;
        }
    }

    @Override
    public List<AreaProfundizacion> consultarAreasProfundizacionPorIDLaboratorio(BigInteger laboratorio) {
        try {
            List<AreaProfundizacion> lista = areaProfundizacionDAO.buscarAreaProfundizacionPorIDLaboratorio(laboratorio);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaElementosEquiposBO consultarProveedoresRegistrados : " + e.toString());
            return null;
        }
    }

    @Override
    public List<SalaLaboratorio> consultarSalasLaboratorioPorIDAreaProfundizacion(BigInteger areaProfundizacion) {
        try {
            List<SalaLaboratorio> lista = salaLaboratorioDAO.buscarSalasLaboratoriosPorAreaProfundizacion(areaProfundizacion);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaElementosEquiposBO consultarSalasLaboratorioPorIDAreaProfundizacion : " + e.toString());
            return null;
        }
    }

    @Override
    public List<SalaLaboratorio> consultarSalasLaboratorioPorIDLaboratorio(BigInteger laboratorio) {
        try {
            List<SalaLaboratorio> lista = salaLaboratorioDAO.buscarSalasLaboratoriosPorLaboratorio(laboratorio);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaElementosEquiposBO consultarSalasLaboratorioPorIDLaboratorio : " + e.toString());
            return null;
        }
    }

    @Override
    public List<ModuloLaboratorio> consultarModulosLaboratorioPorIDSalaLaboratorio(BigInteger salaLaboratorio) {
        try {
            List<ModuloLaboratorio> lista = moduloLaboratorioDAO.buscarModuloLaboratorioPorIDSalaLaboratorio(salaLaboratorio);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaElementosEquiposBO consultarModulosLaboratorioPorIDSalaLaboratorio : " + e.toString());
            return null;
        }
    }

    @Override
    public List<EquipoElemento> consultarEquiposElementosPorParametro(Map<String, String> filtros) {
        try {
            List<EquipoElemento> lista = equipoElementoDAO.buscarEquiposElementosPorFiltrado(filtros);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaElementosEquiposBO consultarEquiposElementosPorParametro : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearNuevoEquipoElemento(EquipoElemento equipoElemento) {
        try {
            equipoElementoDAO.crearEquipoElemento(equipoElemento);
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaElementosEquiposBO crearNuevoEquipoElemento : " + e.toString());
        }
    }

    @Override
    public void modificarInformacionEquipoElemento(EquipoElemento equipoElemento) {
        try {
            equipoElementoDAO.editarEquipoElemento(equipoElemento);
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaElementosEquiposBO modificarInformacionEquipoElemento : " + e.toString());
        }
    }

    @Override
    public EquipoElemento obtenerEquipoElementoPorIDEquipoElemento(BigInteger idEquipoElemento) {
        try {
            EquipoElemento registro = equipoElementoDAO.buscarEquipoElementoPorID(idEquipoElemento);
            return registro;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaElementosEquiposBO obtenerEquipoElementoPorIDEquipoElemento : " + e.toString());
            return null;
        }
    }
}
