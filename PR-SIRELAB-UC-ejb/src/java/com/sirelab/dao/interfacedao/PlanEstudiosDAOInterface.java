/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.PlanEstudios;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface PlanEstudiosDAOInterface {

    public void crearPlanEstudios(PlanEstudios planestudios);

    public void editarPlanEstudios(PlanEstudios planestudios);

    public void eliminarPlanEstudios(PlanEstudios planestudios);

    public List<PlanEstudios> consultarPlanesEstudios();

    public PlanEstudios buscarPlanEstudiosPorID(BigInteger idRegistro);

    public List<PlanEstudios> consultarPlanesEstudiosPorCarrera(BigInteger idCarrera);

    public List<PlanEstudios> buscarPlanesEstudiosPorFiltrado(Map<String, String> filters);
}
