/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.bo.interfacebo;

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

/**
 *
 * @author ANDRES PINEDA
 */
public interface GestionarPlantaEquiposElementosBOInterface {

    public EquipoElemento obtenerEquipoElementoPorIDEquipoElemento(BigInteger idEquipoElemento);

    public void modificarInformacionEquipoElemento(EquipoElemento equipoElemento);

    public void crearNuevoEquipoElemento(EquipoElemento equipoElemento);

    public List<EquipoElemento> consultarEquiposElementosPorParametro(Map<String, String> filtros);

    public List<ModuloLaboratorio> consultarModulosLaboratorioPorIDSalaLaboratorio(BigInteger salaLaboratorio);

    public List<SalaLaboratorio> consultarSalasLaboratorioPorIDLaboratorio(BigInteger laboratorio);

    public List<Proveedor> consultarProveedoresRegistrados();

    public List<TipoActivo> consultarTiposActivosRegistrador();

    public List<EstadoEquipo> consultarEstadosEquiposRegistrados();

    public List<Laboratorio> consultarLaboratoriosRegistrados();

    public List<AreaProfundizacion> consultarAreasProfundizacionPorIDLaboratorio(BigInteger laboratorio);

    public List<SalaLaboratorio> consultarSalasLaboratorioPorIDAreaProfundizacion(BigInteger areaProfundizacion);
}
