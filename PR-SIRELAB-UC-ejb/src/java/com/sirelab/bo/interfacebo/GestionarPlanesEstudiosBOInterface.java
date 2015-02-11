/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.bo.interfacebo;

import com.sirelab.entidades.Carrera;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.PlanEstudios;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface GestionarPlanesEstudiosBOInterface {

    public List<Facultad> consultarFacultadesRegistradas();

    public List<Departamento> consultarDepartamentosPorIDFacultad(BigInteger facultad);

    public List<Carrera> consultarCarrerasPorIDDepartamento(BigInteger departamentos);

    public List<PlanEstudios> consultarPlanesEstudiosPorParametro(Map<String, String> filtros);

    public void crearNuevoPlanEstudio(PlanEstudios planEstudio);

    public void modificarInformacionPlanEstudios(PlanEstudios planEstudio);

    public PlanEstudios obtenerPlanEstudiosPorIDCarrera(BigInteger idPlan);
}
