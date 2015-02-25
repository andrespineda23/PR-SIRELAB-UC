/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.ModuloLaboratorio;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface ModuloLaboratorioDAOInterface {

    public void crearModuloLaboratorio(ModuloLaboratorio modulolaboratorio);

    public void editarModuloLaboratorio(ModuloLaboratorio modulolaboratorio);

    public void eliminarModuloLaboratorio(ModuloLaboratorio modulolaboratorio);

    public List<ModuloLaboratorio> consultarModulosLaboratorios();

    public ModuloLaboratorio buscarModuloLaboratorioPorID(BigInteger idRegistro);

    public List<ModuloLaboratorio> buscarModuloLaboratorioPorIDSalaLaboratorio(BigInteger idSala);

    public List<ModuloLaboratorio> buscarModulosLaboratoriosPorFiltrado(Map<String, String> filters);

}
