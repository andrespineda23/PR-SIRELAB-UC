/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.Asignatura;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface AsignaturaDAOInterface {

    public void crearAsignatura(Asignatura asignatura);

    public void editarAsignatura(Asignatura asignatura);

    public void eliminarAsignatura(Asignatura asignatura);

    public List<Asignatura> consultarAsignaturas();

    public Asignatura buscarAsignaturaPorID(BigInteger idRegistro);

    public List<Asignatura> buscarAsignaturasPorFiltrado(Map<String, String> filters);

}
