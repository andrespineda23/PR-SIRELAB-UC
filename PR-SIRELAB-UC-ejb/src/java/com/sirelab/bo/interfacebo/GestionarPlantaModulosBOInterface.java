/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.bo.interfacebo;

import com.sirelab.entidades.AreaProfundizacion;
import com.sirelab.entidades.Edificio;
import com.sirelab.entidades.Laboratorio;
import com.sirelab.entidades.ModuloLaboratorio;
import com.sirelab.entidades.SalaLaboratorio;
import com.sirelab.entidades.Sede;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface GestionarPlantaModulosBOInterface {

    public List<Laboratorio> consultarLaboratoriosRegistrados();

    public List<AreaProfundizacion> consultarAreasProfundizacionPorIDLaboratorio(BigInteger laboratorio);

    public List<Sede> consultarSedesRegistradas();

    public List<Edificio> consultarEdificiosPorIDSede(BigInteger sede);

    public List<SalaLaboratorio> consultarSalasLaboratorioPorParametroFiltrado(Map<String, String> filtros);

    public List<ModuloLaboratorio> consultarModulosLaboratorioPorParametro(Map<String, String> filtros);

    public void crearNuevoModuloLaboratorio(ModuloLaboratorio moduloLaboratorio);

    public void modificarInformacionModuloLaboratorio(ModuloLaboratorio moduloLaboratorio);

    public ModuloLaboratorio obtenerModuloLaboratorioPorIDModuloLaboratorio(BigInteger idModuloLaboratorio);

}
