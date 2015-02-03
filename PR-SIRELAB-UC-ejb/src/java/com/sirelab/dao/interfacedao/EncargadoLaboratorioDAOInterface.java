/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.EncargadoLaboratorio;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface EncargadoLaboratorioDAOInterface {

    public void crearEncargadoLaboratorio(EncargadoLaboratorio encargadolaboratorio);

    public void editarEncargadoLaboratorio(EncargadoLaboratorio encargadolaboratorio);

    public void eliminarEncargadoLaboratorio(EncargadoLaboratorio encargadolaboratorio);

    public List<EncargadoLaboratorio> consultarEncargadosLaboratorios();

    public EncargadoLaboratorio buscarEncargadoLaboratorioPorID(BigInteger idRegistro);

    public EncargadoLaboratorio buscarEncargadoLaboratorioPorIDPersona(BigInteger idPersona);

    public EncargadoLaboratorio buscarEncargadoLaboratorioPorPorCorreoNumDocumento(String correo, String documento);

    public List<EncargadoLaboratorio> buscarEncargadosLaboratoriosPorFiltrado(Map<String, String> filters);
}
