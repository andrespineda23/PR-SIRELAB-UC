/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.Sede;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface SedeDAOInterface {

    public void crearSede(Sede sede);

    public void editarSede(Sede sede);

    public void eliminarSede(Sede sede);

    public List<Sede> consultarSedes();

    public Sede buscarSedePorID(BigInteger idRegistro);

    public List<Sede> buscarSedesPorFiltrado(Map<String, String> filters);
}
